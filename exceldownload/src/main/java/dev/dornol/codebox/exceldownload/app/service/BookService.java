package dev.dornol.codebox.exceldownload.app.service;

import dev.dornol.codebox.excelutil.csv.CsvHandler;
import dev.dornol.codebox.excelutil.excel.ExcelHandler;

public interface BookService {

    ExcelHandler getExcelHandler();

    CsvHandler getCsvHandler();

}
