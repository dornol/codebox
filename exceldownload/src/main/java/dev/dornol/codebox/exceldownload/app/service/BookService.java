package dev.dornol.codebox.exceldownload.app.service;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.module.csv.CsvHandler;
import dev.dornol.codebox.exceldownload.module.excel.ExcelHandler;

public interface BookService {

    ExcelHandler<BookDto> getExcelHandler();

    CsvHandler<BookDto> getCsvHandler();

}
