package dev.dornol.codebox.exceldownload.module.excel;

import org.apache.poi.xssf.streaming.SXSSFCell;

@FunctionalInterface
interface ExcelColumnSetter {

    void set(SXSSFCell cell, Object value);

}
