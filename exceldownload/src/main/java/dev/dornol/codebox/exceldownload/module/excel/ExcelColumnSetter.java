package dev.dornol.codebox.exceldownload.module.excel;

import org.apache.poi.xssf.streaming.SXSSFCell;

@FunctionalInterface
interface ExcelColumnSetter {

    /**
 * Sets the value of the specified Excel cell.
 *
 * @param cell  the SXSSFCell to update
 * @param value the value to assign to the cell
 */
void set(SXSSFCell cell, Object value);

}
