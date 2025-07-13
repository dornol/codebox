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

    public ExcelHandler(int r, int g, int b) {
        this.wb = new SXSSFWorkbook(10000);
        this.headerColor = new XSSFColor(new byte[]{(byte) r, (byte) g, (byte) b});
    }

    public ExcelHandler() {
        this(255, 255, 255);
    }

    void addColumn(ExcelColumn<T> column) {
        this.columns.add(column);
    }

    public ExcelColumn.ExcelColumnBuilder<T> column(String name, ExcelRowFunction<T, Object> function) {
        return new ExcelColumn.ExcelColumnBuilder<>(this, name, function);
    }

    public ExcelColumn.ExcelColumnBuilder<T> column(String name, Function<T, Object> function) {
        return new ExcelColumn.ExcelColumnBuilder<>(this, name, (r, c) -> function.apply(r));
    }

    public ExcelColumn.ExcelColumnBuilder<T> constColumn(String name, Object value) {
        return new ExcelColumn.ExcelColumnBuilder<>(this, name, (r, c) -> value);
    }

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

    public ExcelHandler<T> write(Stream<T> stream) {
        this.write(stream, (rowData, consumer) -> {
        });
        return this;
    }

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

    SXSSFWorkbook getWb() {
        return wb;
    }

    void setMaxRowsOfSheet(int maxRowsOfSheet) {
        this.maxRowsOfSheet = maxRowsOfSheet;
    }

}
