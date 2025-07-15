package dev.dornol.codebox.excelutil.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

class ExcelStyleSupporter {
    private ExcelStyleSupporter() {
        /* empty */
    }

    static CellStyle headerStyle(SXSSFWorkbook wb, XSSFColor headerColor) {
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(headerColor);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerFont.setBold(true);
        headerFont.setFontHeight((short) (11 * 20));

        if (isDarkColor(headerColor)) {
            headerFont.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        } else {
            headerFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        }

        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    private static boolean isDarkColor(XSSFColor color) {
        byte[] rgb = color.getRGB();
        if (rgb == null || rgb.length != 3) return false;

        int r = Byte.toUnsignedInt(rgb[0]);
        int g = Byte.toUnsignedInt(rgb[1]);
        int b = Byte.toUnsignedInt(rgb[2]);

        // ✅ W3C 표준 밝기 공식 (Y = 0.299R + 0.587G + 0.114B)
        double luminance = 0.299 * r + 0.587 * g + 0.114 * b;
        return luminance < 128; // 밝기 기준: 0~255 중 128 미만은 어둡다고 판단
    }

    static CellStyle cellStyle(SXSSFWorkbook wb, HorizontalAlignment alignment, String format) {
        CellStyle nowStyle = wb.createCellStyle();

        nowStyle.setAlignment(alignment);
        if (format != null) {
            DataFormat dataFormat = wb.createDataFormat();
            nowStyle.setDataFormat(dataFormat.getFormat(format));
        }
        nowStyle.setBorderTop(BorderStyle.THIN);
        nowStyle.setBorderBottom(BorderStyle.THIN);
        nowStyle.setBorderLeft(BorderStyle.THIN);
        nowStyle.setBorderRight(BorderStyle.THIN);
        nowStyle.setWrapText(true);
        return nowStyle;
    }

}
