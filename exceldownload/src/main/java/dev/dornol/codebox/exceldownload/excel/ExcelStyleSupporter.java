package dev.dornol.codebox.exceldownload.excel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ExcelStyleSupporter {

    static CellStyle headerStyle(SXSSFWorkbook wb) {
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerFont.setBold(true);
        headerFont.setFontHeight((short) (11 * 20));
        headerFont.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    static CellStyle cellStyle(SXSSFWorkbook wb, ExcelColumn<?> column) {
        CellStyle nowStyle = wb.createCellStyle();

        nowStyle.setAlignment(column.getAlignment() == null ? HorizontalAlignment.CENTER : column.getAlignment());
        if (column.getDataFormat() != null) {
            DataFormat dataFormat = wb.createDataFormat();
            nowStyle.setDataFormat(dataFormat.getFormat(column.getDataFormat()));
        }
        nowStyle.setBorderTop(BorderStyle.THIN);
        nowStyle.setBorderBottom(BorderStyle.THIN);
        nowStyle.setBorderLeft(BorderStyle.THIN);
        nowStyle.setBorderRight(BorderStyle.THIN);
        nowStyle.setWrapText(true);
        return nowStyle;
    }

}
