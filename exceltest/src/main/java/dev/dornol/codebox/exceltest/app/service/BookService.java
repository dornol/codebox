package dev.dornol.codebox.exceltest.app.service;

import dev.dornol.codebox.excelutil.csv.CsvHandler;
import dev.dornol.codebox.excelutil.excel.ExcelHandler;

import java.io.InputStream;

public interface BookService {

    ExcelHandler getExcelHandler();

    CsvHandler getCsvHandler();

    void readExcel(InputStream inputStream);

}
