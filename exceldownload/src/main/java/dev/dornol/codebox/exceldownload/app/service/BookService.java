package dev.dornol.codebox.exceldownload.app.service;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.excel.ExcelHandler;

public interface BookService {

    ExcelHandler<BookDto> getExcelHandler();

}
