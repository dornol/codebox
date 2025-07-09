package dev.dornol.codebox.exceldownload.excel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static dev.dornol.codebox.exceldownload.excel.ExcelDataFormat.*;

@Slf4j
@Getter
class ExcelColumn<T> {

    private final String name;
    private ExcelDataType dataType = ExcelDataType.STRING;
    private String dataFormat;
    private HorizontalAlignment alignment = HorizontalAlignment.CENTER;
    private final ExcelRowFunction<T, Object> function;
    private CellStyle style;
    private ExcelColumnSetter columnSetter = (cell, value) -> cell.setCellValue(String.valueOf(value));
    private int columnWidth = 256 * 256;

    protected ExcelColumn(String name, ExcelRowFunction<T, Object> function) {
        this.name = name;
        this.function = function;
    }

    public ExcelColumn<T> type(ExcelDataType dataType) {
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

        return this;
    }

    public ExcelColumn<T> format(String dataFormat) {
        this.dataFormat = dataFormat;
        return this;
    }

    public ExcelColumn<T> alignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
        return this;
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
}
