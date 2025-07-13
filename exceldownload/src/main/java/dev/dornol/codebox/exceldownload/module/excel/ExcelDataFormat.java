package dev.dornol.codebox.exceldownload.module.excel;

public enum ExcelDataFormat {

    NUMBER("#,##0"),
    PERCENT("0.00%"),
    DATETIME("yyyy-mm-dd hh:mm:ss"),
    DATE("yyyy-mm-dd"),
    TIME("hh:mm:ss"),
    ;

    /**
     * 엑셀의 포맷
     */
    private final String format;

    /**
     * Constructs an ExcelDataFormat enum constant with the specified Excel format string.
     *
     * @param format the Excel format string associated with the enum constant
     */
    ExcelDataFormat(String format) {
        this.format = format;
    }

    /**
     * Returns the Excel format string associated with this data format.
     *
     * @return the Excel format string
     */
    public String getFormat() {
        return format;
    }
}
