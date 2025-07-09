package dev.dornol.codebox.exceldownload.excel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
}
