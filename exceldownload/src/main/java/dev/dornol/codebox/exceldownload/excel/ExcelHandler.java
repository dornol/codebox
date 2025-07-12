package dev.dornol.codebox.exceldownload.excel;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 엑셀 다운로드 모듈
 * 
 * db조회 시 JPA 엔티티를 조회하는 경우 Memory 사용량이 크게 증가할 수 있으니 주의가 필요함
 * 
 * @param <T>
 */
@Slf4j
public class ExcelHandler<T> {
    private final SXSSFWorkbook wb;
    private SXSSFSheet sheet;
    private final List<ExcelColumn<T>> columns = new ArrayList<>();
    private ExcelCursor cursor;

    @Setter
    private int maxRowsOfSheet = 1_000_000;

    public ExcelHandler() {
        this.wb = new SXSSFWorkbook(10000);
    }

    public ExcelColumnBuilder<T> column(String name, ExcelRowFunction<T, Object> function) {
        var column = new ExcelColumn<>(name, function);
        this.columns.add(column);
        return new ExcelColumnBuilder<>(this, column);
    }

    public ExcelColumnBuilder<T> column(String name, Function<T, Object> function) {
        var column = new ExcelColumn<T>(name, ((rowData, cursor1) -> function.apply(rowData)));
        this.columns.add(column);
        return new ExcelColumnBuilder<>(this, column);
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
        this.write(stream, (rowData, consumer) -> {});
        return this;
    }

    private void setColumnHeaders() {
        CellStyle headerStyle = ExcelStyleSupporter.headerStyle(wb);
        SXSSFRow headRow = sheet.createRow(cursor.getRowOfSheet());
        cursor.plusRow();
        for (int j = 0; j < cursor.getColumnCount(); j++) {
            SXSSFCell cell = headRow.createCell(j);
            ExcelColumn<T> column = columns.get(j);
            cell.setCellValue(column.getName());
            CellStyle cellStyle = ExcelStyleSupporter.cellStyle(wb, columns.get(j));
            columns.get(j).setStyle(cellStyle);
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
            Object columnData = columns.get(j).applyFunction(rowData, cursor);
            setColumnData(cell, column, columnData);
            cell.setCellStyle(column.getStyle());
            if (cursor.getCurrentTotal() < 100) {
                int width = Math.min(255 * 256, getLogicalLength(String.valueOf(columnData)) * 250 + 1024);
                columns.get(j).setColumnWidth(Math.max(sheet.getColumnWidth(j), width));
            }
            if (cursor.getRowOfSheet() < 100) {
                sheet.setColumnWidth(j, columns.get(j).getColumnWidth());
            }
        }
    }

    private int getLogicalLength(String input) {
        int logicalLength = 0;
        for (char ch : input.toCharArray()) {
            logicalLength += (ch <= 0x7F) ? 1 : 2; // ASCII: 1, 한글 등: 2
        }
        return logicalLength;
    }

    private void setColumnData(SXSSFCell cell, ExcelColumn<T> column, Object columnData) {
        if (columnData == null) {
            cell.setCellValue("");
            return;
        }
        try {
            column.getColumnSetter().set(cell, columnData);
        } catch (Exception e) {
            log.warn("cast error: {}", e.getMessage());
            cell.setCellValue(String.valueOf(columnData));
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

    public static class ExcelColumnBuilder<T> {
        private final ExcelHandler<T> parent;
        private final ExcelColumn<T> column;

        ExcelColumnBuilder(ExcelHandler<T> parent, ExcelColumn<T> column) {
            this.parent = parent;
            this.column = column;
        }

        public ExcelColumnBuilder<T> type(ExcelDataType dataType) {
            column.type(dataType);
            return this;
        }

        /**
         * 엑셀의 데이터 포맷
         */
        public ExcelColumnBuilder<T> format(String dataFormat) {
            column.format(dataFormat);
            return this;
        }

        public ExcelColumnBuilder<T> format(ExcelDataFormat dataFormat) {
            column.format(dataFormat.getFormat());
            return this;
        }

        public ExcelColumnBuilder<T> alignment(HorizontalAlignment alignment) {
            column.alignment(alignment);
            return this;
        }

        public ExcelColumnBuilder<T> column(String name, ExcelRowFunction<T, Object> function) {
            return parent.column(name, function);
        }

        public ExcelColumnBuilder<T> column(String name, Function<T, Object> function) {
            return parent.column(name, function);
        }

        public ExcelColumnBuilder<T> constColumn(String name, Object value) {
            return parent.column(name, rowData -> value);
        }

        public ExcelHandler<T> write(Stream<T> stream, ExcelConsumer<T> consumer) {
            return parent.write(stream, consumer);
        }

        public ExcelHandler<T> write(Stream<T> stream) {
            return parent.write(stream);
        }

        public ExcelColumnBuilder<T> maxRowsOfSheet(int maxRowsOfSheet) {
            parent.maxRowsOfSheet = maxRowsOfSheet;
            return this;
        }

    }

}
