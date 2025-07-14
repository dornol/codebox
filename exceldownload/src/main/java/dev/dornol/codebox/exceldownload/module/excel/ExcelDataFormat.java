package dev.dornol.codebox.exceldownload.module.excel;

public enum ExcelDataFormat {

    NUMBER("#,##0"),
    NUMBER_1("#,##0.0"),         // 소수점 1자리
    NUMBER_2("#,##0.00"),        // 소수점 2자리
    NUMBER_4("#,##0.0000"),      // 소수점 4자리

    PERCENT("0.00%"),
    DATETIME("yyyy-mm-dd hh:mm:ss"),
    DATE("yyyy-mm-dd"),
    TIME("hh:mm:ss"),

    CURRENCY_KRW("#,##0\"원\""),
    CURRENCY_USD("\"$\"#,##0.00"),
    ;

    /**
     * 엑셀의 포맷
     */
    private final String format;

    ExcelDataFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
