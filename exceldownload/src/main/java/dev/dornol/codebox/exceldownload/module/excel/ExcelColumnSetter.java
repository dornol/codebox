package dev.dornol.codebox.exceldownload.module.excel;

import org.apache.poi.xssf.streaming.SXSSFCell;

interface ExcelColumnSetter {

    void set(SXSSFCell cell, Object value);

}
