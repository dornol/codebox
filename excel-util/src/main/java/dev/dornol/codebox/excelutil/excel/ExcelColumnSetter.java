package dev.dornol.codebox.excelutil.excel;

import org.apache.poi.xssf.streaming.SXSSFCell;

@FunctionalInterface
interface ExcelColumnSetter {

    void set(SXSSFCell cell, Object value);

}
