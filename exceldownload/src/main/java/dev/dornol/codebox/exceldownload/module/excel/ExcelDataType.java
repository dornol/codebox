package dev.dornol.codebox.exceldownload.module.excel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static dev.dornol.codebox.exceldownload.module.excel.ExcelDataFormat.*;

public enum ExcelDataType {

    STRING((cell, value) -> cell.setCellValue(String.valueOf(value)), null),

    BOOLEAN_TO_YN((cell, value) -> cell.setCellValue(Boolean.TRUE.equals(value) ? "Y" : "N"), null),

    LONG((cell, value) -> cell.setCellValue((Long) value), NUMBER.getFormat()),
    INTEGER((cell, value) -> cell.setCellValue((Integer) value), NUMBER.getFormat()),

    DOUBLE((cell, value) -> cell.setCellValue((Double) value), NUMBER_2.getFormat()),
    FLOAT((cell, value) -> cell.setCellValue((Float) value), NUMBER_2.getFormat()),
    DOUBLE_PERCENT((cell, value) -> cell.setCellValue((Double) value), PERCENT.getFormat()),
    FLOAT_PERCENT((cell, value) -> cell.setCellValue((Float) value), PERCENT.getFormat()),

    DATETIME((cell, value) -> cell.setCellValue((LocalDateTime) value), ExcelDataFormat.DATETIME.getFormat()),
    DATE((cell, value) -> cell.setCellValue((LocalDate) value), ExcelDataFormat.DATE.getFormat()),
    TIME((cell, value) -> cell.setCellValue((LocalDateTime) value), ExcelDataFormat.TIME.getFormat()),

    BIG_DECIMAL_TO_DOUBLE((cell, value) -> cell.setCellValue(((BigDecimal) value).doubleValue()), NUMBER_2.getFormat()),
    BIG_DECIMAL_TO_LONG((cell, value) -> cell.setCellValue(((BigDecimal) value).longValue()), LONG.defaultFormat)
    ;

    private final ExcelColumnSetter setter;
    private final String defaultFormat;

    ExcelDataType(ExcelColumnSetter setter, String defaultFormat) {
        this.setter = setter;
        this.defaultFormat = defaultFormat;
    }

    ExcelColumnSetter getSetter() {
        return setter;
    }

    String getDefaultFormat() {
        return defaultFormat;
    }
}
