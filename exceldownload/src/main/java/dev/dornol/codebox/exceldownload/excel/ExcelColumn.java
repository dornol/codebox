package dev.dornol.codebox.exceldownload.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static dev.dornol.codebox.exceldownload.excel.ExcelDataFormat.*;

final class ExcelColumn<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelColumn.class);
    final String name;
    ExcelDataType dataType = ExcelDataType.STRING;
    String dataFormat;
    HorizontalAlignment alignment = HorizontalAlignment.CENTER;
    final ExcelRowFunction<T, Object> function;
    CellStyle style;
    ExcelColumnSetter columnSetter = (cell, value) -> cell.setCellValue(String.valueOf(value));
    int columnWidth = 256 * 256;

    ExcelColumn(String name, ExcelRowFunction<T, Object> function) {
        this.name = name;
        this.function = function;
    }

    void type(ExcelDataType dataType) {
        this.dataType = dataType;
        if (dataFormat == null) {
            if (dataType.equals(ExcelDataType.LONG) || dataType.equals(dev.dornol.codebox.exceldownload.excel.ExcelDataType.INTEGER)) {
                this.format(NUMBER.getFormat());
            } else if (dataType.equals(ExcelDataType.DOUBLE_PERCENT) || dataType.equals(dev.dornol.codebox.exceldownload.excel.ExcelDataType.FLOAT_PERCENT)) {
                this.format(PERCENT.getFormat());
            } else if (dataType.equals(ExcelDataType.DATE)) {
                this.format(DATE.getFormat());
            } else if (dataType.equals(ExcelDataType.DATETIME)) {
                this.format(DATETIME.getFormat());
            } else if (dataType.equals(ExcelDataType.TIME)) {
                this.format(TIME.getFormat());
            }
        }

        columnSetter = switch (dataType) {
            case LONG -> (cell, value) -> cell.setCellValue((Long) value);
            case INTEGER -> (cell, value) -> cell.setCellValue((Integer) value);
            case DOUBLE, DOUBLE_PERCENT -> (cell, value) -> cell.setCellValue((Double) value);
            case FLOAT, FLOAT_PERCENT -> (cell, value) -> cell.setCellValue((Float) value);
            case DATETIME, TIME -> (cell, value) -> cell.setCellValue((LocalDateTime) value);
            case DATE -> (cell, value) -> cell.setCellValue((LocalDate) value);
            case BIGDECIMAL_DOUBLE -> (cell, value) -> cell.setCellValue(((BigDecimal) value).doubleValue());
            case BIGDECIMAL_LONG -> (cell, value) -> cell.setCellValue(((BigDecimal) value).longValue());
            default -> (cell, value) -> cell.setCellValue(String.valueOf(value));
        };
    }

    void format(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    void alignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
    }

    Object applyFunction(T rowData, ExcelCursor cursor) {
        try {
            return function.apply(rowData, cursor);
        } catch (Exception e) {
            log.error("applyFunction exception caught : {}, {} \n", rowData, cursor, e);
            return null;
        }
    }

    void setStyle(CellStyle style) {
        this.style = style;
    }

    void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public String getName() {
        return name;
    }

    public ExcelDataType getDataType() {
        return dataType;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    public ExcelRowFunction<T, Object> getFunction() {
        return function;
    }

    public CellStyle getStyle() {
        return style;
    }

    public ExcelColumnSetter getColumnSetter() {
        return columnSetter;
    }

    public int getColumnWidth() {
        return columnWidth;
    }
}
