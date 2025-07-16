package dev.dornol.codebox.excelutil.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ExcelWriter<T> {
    private final SXSSFWorkbook wb;
    private final List<ExcelColumn<T>> columns = new ArrayList<>();
    private final XSSFColor headerColor;
    private final int maxRowsOfSheet;

    private SXSSFSheet sheet;
    private ExcelCursor cursor;


    public ExcelWriter(int r, int g, int b, int maxRowsOfSheet) {
        this.wb = new SXSSFWorkbook(10000);
        this.headerColor = new XSSFColor(new byte[]{(byte) r, (byte) g, (byte) b});
        this.maxRowsOfSheet = maxRowsOfSheet;
    }

    public ExcelWriter(int maxRowsOfSheet) {
        this(255, 255, 255, maxRowsOfSheet);
    }

    public ExcelWriter(int r, int g, int b) {
        this(r, g, b, 1_000_000);
    }

    public ExcelWriter() {
        this(255, 255, 255, 1_000_000);
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

    ExcelHandler write(Stream<T> stream, ExcelConsumer<T> consumer) {
        if (this.columns.size() < 2) {
            throw new IllegalStateException("columns setting required");
        }

        this.sheet = wb.createSheet();
        this.cursor = new ExcelCursor();

        setColumnHeaders();

        try (stream) {
            stream.forEach(rowData -> {
                this.handleRowData(rowData);
                consumer.accept(rowData, cursor);
            });
        }
        return new ExcelHandler(this.wb);
    }

    ExcelHandler write(Stream<T> stream) {
        return this.write(stream, (rowData, consumer) -> {});
    }

    private void setColumnHeaders() {
        CellStyle headerStyle = ExcelStyleSupporter.headerStyle(wb, this.headerColor);
        SXSSFRow headRow = sheet.createRow(cursor.getRowOfSheet());
        cursor.plusRow();
        for (int j = 0; j < this.columns.size(); j++) {
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

        for (int j = 0; j < this.columns.size(); j++) {
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

    SXSSFWorkbook getWb() {
        return wb;
    }
}
