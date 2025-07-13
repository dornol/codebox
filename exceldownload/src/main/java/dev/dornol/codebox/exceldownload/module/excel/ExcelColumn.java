package dev.dornol.codebox.exceldownload.module.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

import static dev.dornol.codebox.exceldownload.module.excel.ExcelDataFormat.*;

final class ExcelColumn<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelColumn.class);
    private static final int MAX_COLUMN_WIDTH = 255 * 256;
    private final String name;
    private final ExcelRowFunction<T, Object> function;
    private final CellStyle style;
    private final ExcelColumnSetter columnSetter;
    private int columnWidth = 1;

    ExcelColumn(String name, ExcelRowFunction<T, Object> function, CellStyle style, ExcelColumnSetter columnSetter) {
        this.name = name;
        this.function = function;
        this.style = style;
        this.columnSetter = columnSetter;
        this.columnWidth = getLogicalLength(name);
    }

    Object applyFunction(T rowData, ExcelCursor cursor) {
        try {
            return function.apply(rowData, cursor);
        } catch (Exception e) {
            log.error("applyFunction exception caught : {}, {} \n", rowData, cursor, e);
            return null;
        }
    }

    void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    void fitColumnWidthByValue(Object value) {
        int width = getLogicalLength(String.valueOf(value));
        this.setColumnWidth(Math.max(this.columnWidth, width));
    }

    void setColumnData(SXSSFCell cell, Object columnData) {
        if (columnData == null) {
            cell.setCellValue("");
            return;
        }
        try {
            this.columnSetter.set(cell, columnData);
        } catch (Exception e) {
            log.warn("cast error: {}", e.getMessage());
            cell.setCellValue(String.valueOf(columnData));
        }
    }

    private int getLogicalLength(String input) {
        int logicalLength = 0;
        for (char ch : input.toCharArray()) {
            logicalLength += (ch <= 0x7F) ? 1 : 2; // ASCII: 1, 한글 등: 2
        }
        return Math.min(MAX_COLUMN_WIDTH, logicalLength * 250 + 1024);
    }

    public String getName() {
        return name;
    }

    public CellStyle getStyle() {
        return style;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public static class ExcelColumnBuilder<T> {
        private final ExcelHandler<T> handler;
        private final String name;
        private final ExcelRowFunction<T, Object> function;
        private ExcelDataType dataType;
        private String dataFormat;
        private HorizontalAlignment alignment = HorizontalAlignment.CENTER;
        private CellStyle style;
        private ExcelColumnSetter columnSetter;

        public ExcelColumnBuilder(ExcelHandler<T> handler, String name, ExcelRowFunction<T, Object> function) {
            this.handler = handler;
            this.name = name;
            this.function = function;
        }

        public ExcelColumnBuilder<T> type(ExcelDataType dataType) {
            this.dataType = dataType;
            if (dataFormat == null) {
                if (dataType.equals(ExcelDataType.LONG) || dataType.equals(ExcelDataType.INTEGER)) {
                    this.format(NUMBER.getFormat());
                } else if (dataType.equals(ExcelDataType.DOUBLE_PERCENT) || dataType.equals(ExcelDataType.FLOAT_PERCENT)) {
                    this.format(PERCENT.getFormat());
                } else if (dataType.equals(ExcelDataType.DATE)) {
                    this.format(DATE.getFormat());
                } else if (dataType.equals(ExcelDataType.DATETIME)) {
                    this.format(DATETIME.getFormat());
                } else if (dataType.equals(ExcelDataType.TIME)) {
                    this.format(TIME.getFormat());
                }
            }

            columnSetter = switch (this.dataType) {
                case LONG -> (cell, value) -> cell.setCellValue((Long) value);
                case INTEGER -> (cell, value) -> cell.setCellValue((Integer) value);
                case DOUBLE, DOUBLE_PERCENT -> (cell, value) -> cell.setCellValue((Double) value);
                case FLOAT, FLOAT_PERCENT -> (cell, value) -> cell.setCellValue((Float) value);
                case DATETIME, TIME -> (cell, value) -> cell.setCellValue((LocalDateTime) value);
                case DATE -> (cell, value) -> cell.setCellValue((LocalDate) value);
                case BIG_DECIMAL_TO_DOUBLE -> (cell, value) -> cell.setCellValue(((BigDecimal) value).doubleValue());
                case BIG_DECIMAL_TO_LONG -> (cell, value) -> cell.setCellValue(((BigDecimal) value).longValue());
                default -> (cell, value) -> cell.setCellValue(String.valueOf(value));
            };
            return this;
        }

        public ExcelColumnBuilder<T> format(String dataFormat) {
            this.dataFormat = dataFormat;
            return this;
        }

        public ExcelColumnBuilder<T> alignment(HorizontalAlignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public ExcelColumnBuilder<T> style(CellStyle style) {
            this.style = style;
            return this;
        }

        private ExcelColumn<T> build() {
            if (this.dataType == null) {
                this.type(ExcelDataType.STRING);
            }
            if (this.style == null) {
                this.style(ExcelStyleSupporter.cellStyle(handler.getWb(), this.alignment, this.dataFormat));
            }
            return new ExcelColumn<>(this.name, this.function, this.style, this.columnSetter);
        }

        public ExcelColumnBuilder<T> column(String name, ExcelRowFunction<T, Object> function) {
            this.handler.addColumn(this.build());
            return new ExcelColumnBuilder<>(handler, name, function);
        }

        public ExcelColumnBuilder<T> column(String name, Function<T, Object> function) {
            this.handler.addColumn(this.build());
            return new ExcelColumnBuilder<>(handler, name, (r, c) -> function.apply(r));
        }

        public ExcelColumnBuilder<T> constColumn(String name, Object value) {
            this.handler.addColumn(this.build());
            return new ExcelColumnBuilder<>(handler, name, (r, c) -> value);
        }

        public ExcelColumnBuilder<T> maxRowsOfSheet(int maxRowsOfSheet) {
            this.handler.setMaxRowsOfSheet(maxRowsOfSheet);
            return this;
        }

        public ExcelHandler<T> write(Stream<T> stream, ExcelConsumer<T> consumer) {
            this.handler.addColumn(this.build());
            return this.handler.write(stream, consumer);
        }

        public ExcelHandler<T> write(Stream<T> stream) {
            this.handler.addColumn(this.build());
            return this.handler.write(stream);
        }

    }

}
