package dev.dornol.codebox.exceldownload.module.excel;

import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 엑셀 다운로드 모듈
 * <p>
 * db조회 시 JPA 엔티티를 조회하는 경우 Memory 사용량이 크게 증가할 수 있으니 주의가 필요함
 *
 * @param <T>
 */
public class ExcelHandler<T> {
    private final SXSSFWorkbook wb;
    private final List<ExcelColumn<T>> columns = new ArrayList<>();
    private final XSSFColor headerColor;

    private SXSSFSheet sheet;
    private ExcelCursor cursor;
    private int maxRowsOfSheet = 1_000_000;

    /**
     * Constructs an ExcelHandler with a custom header color specified by RGB values.
     *
     * @param r the red component of the header color (0-255)
     * @param g the green component of the header color (0-255)
     * @param b the blue component of the header color (0-255)
     */
    public ExcelHandler(int r, int g, int b) {
        this.wb = new SXSSFWorkbook(10000);
        this.headerColor = new XSSFColor(new byte[]{(byte) r, (byte) g, (byte) b});
    }

    /**
     * Constructs an ExcelHandler with a default white header color.
     */
    public ExcelHandler() {
        this(255, 255, 255);
    }

    /**
     * Adds a column definition to the list of columns for the Excel sheet.
     *
     * @param column the column configuration to add
     */
    void addColumn(ExcelColumn<T> column) {
        this.columns.add(column);
    }

    /**
     * Creates a column builder for defining a column with a custom function that receives both the row data and column index.
     *
     * @param name the name of the column to be displayed in the Excel header
     * @param function a function that maps the row data and column index to the cell value
     * @return a builder for further configuring the column
     */
    public ExcelColumn.ExcelColumnBuilder<T> column(String name, ExcelRowFunction<T, Object> function) {
        return new ExcelColumn.ExcelColumnBuilder<>(this, name, function);
    }

    /**
     * Creates a column builder for adding a column with the specified name, using a function to extract cell values from each row.
     *
     * @param name the column header name
     * @param function a function that maps a row object to the cell value for this column
     * @return a builder for configuring and adding the column
     */
    public ExcelColumn.ExcelColumnBuilder<T> column(String name, Function<T, Object> function) {
        return new ExcelColumn.ExcelColumnBuilder<>(this, name, (r, c) -> function.apply(r));
    }

    /**
     * Creates a column builder for a column with a constant value for all rows.
     *
     * @param name the name of the column
     * @param value the constant value to be used for every row in this column
     * @return a builder for configuring the constant value column
     */
    public ExcelColumn.ExcelColumnBuilder<T> constColumn(String name, Object value) {
        return new ExcelColumn.ExcelColumnBuilder<>(this, name, (r, c) -> value);
    }

    /**
     * Writes data from the provided stream to the Excel workbook, applying each row to the sheet and invoking the specified consumer for additional processing.
     *
     * @param stream   the stream of data rows to write
     * @param consumer a callback invoked for each row with the row data and current cursor position
     * @return this ExcelHandler instance for method chaining
     * @throws IllegalStateException if fewer than two columns are defined before writing
     */
    ExcelHandler<T> write(Stream<T> stream, ExcelConsumer<T> consumer) {
        if (this.columns.size() < 2) {
            throw new IllegalStateException("columns setting required");
        }

        this.sheet = wb.createSheet();
        this.cursor = new ExcelCursor(columns.size());

        setColumnHeaders();

        try (stream) {
            stream.forEach(rowData -> {
                this.handleRowData(rowData);
                consumer.accept(rowData, cursor);
            });
        }
        return this;
    }

    /**
     * Writes data from the provided stream to the Excel workbook without applying any additional row-level processing.
     *
     * @param stream the stream of data rows to write to the Excel sheet
     * @return this ExcelHandler instance for method chaining
     */
    public ExcelHandler<T> write(Stream<T> stream) {
        this.write(stream, (rowData, consumer) -> {
        });
        return this;
    }

    /**
     * Creates and writes the header row in the current Excel sheet, applying the configured header style and column names.
     */
    private void setColumnHeaders() {
        CellStyle headerStyle = ExcelStyleSupporter.headerStyle(wb, this.headerColor);
        SXSSFRow headRow = sheet.createRow(cursor.getRowOfSheet());
        cursor.plusRow();
        for (int j = 0; j < cursor.getColumnCount(); j++) {
            SXSSFCell cell = headRow.createCell(j);
            ExcelColumn<T> column = columns.get(j);
            cell.setCellValue(column.getName());
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Writes a single row of data to the current Excel sheet, applying column functions, styles, and adjusting column widths as needed.
     *
     * @param rowData the data object representing the row to be written
     */
    void handleRowData(T rowData) {
        cursor.plusTotal();
        if (isOverMaxRows()) {
            turnOverSheet();
            setColumnHeaders();
        }
        SXSSFRow row = sheet.createRow(cursor.getRowOfSheet());
        row.setHeightInPoints(20);
        cursor.plusRow();

        for (int j = 0; j < cursor.getColumnCount(); j++) {
            SXSSFCell cell = row.createCell(j);
            ExcelColumn<T> column = columns.get(j);
            Object columnData = column.applyFunction(rowData, cursor);
            column.setColumnData(cell, columnData);
            cell.setCellStyle(column.getStyle());
            if (cursor.getCurrentTotal() < 100) {
                column.fitColumnWidthByValue(columnData);
            }
            if (cursor.getRowOfSheet() < 100) {
                sheet.setColumnWidth(j, column.getColumnWidth());
            }
        }
    }

    /**
     * Creates a new sheet in the workbook and resets the row cursor to the initial position.
     */
    private void turnOverSheet() {
        this.sheet = wb.createSheet();
        this.cursor.initRow();
    }

    private boolean isOverMaxRows() {
        return cursor.getCurrentTotal() >= maxRowsOfSheet && cursor.getCurrentTotal() % maxRowsOfSheet == 1;
    }


    public void consumeOutputStream(OutputStream outputStream) throws IOException {
        try {
            wb.write(outputStream);
        } finally {
            wb.close();
        }
    }

    /**
     * Writes the Excel workbook to the provided output stream with password protection.
     *
     * The output file will be encrypted using the specified password. Closes the workbook after writing.
     *
     * @param outputStream the output stream to write the encrypted Excel file to
     * @param password the password to encrypt the Excel file with
     * @throws IOException if an I/O error occurs during writing
     * @throws IllegalStateException if a security exception occurs during encryption
     */
    public void consumeOutputStreamWithPassword(OutputStream outputStream, String password) throws IOException {
        try (POIFSFileSystem fs = new POIFSFileSystem();) {
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor enc = info.getEncryptor();
            enc.confirmPassword(password);

            try (OutputStream encOut = enc.getDataStream(fs)) {
                wb.write(encOut);
            } catch (GeneralSecurityException e) {
                throw new IllegalStateException(e);
            }

            fs.writeFilesystem(outputStream);
        } finally {
            wb.close();
        }
    }

    /**
     * Returns the underlying streaming workbook instance used for Excel file generation.
     *
     * @return the SXSSFWorkbook representing the current Excel workbook
     */
    SXSSFWorkbook getWb() {
        return wb;
    }

    /**
     * Sets the maximum number of rows allowed per sheet before a new sheet is created.
     *
     * @param maxRowsOfSheet the maximum number of rows per sheet
     */
    void setMaxRowsOfSheet(int maxRowsOfSheet) {
        this.maxRowsOfSheet = maxRowsOfSheet;
    }

}
