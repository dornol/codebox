package dev.dornol.codebox.exceldownload.app.service;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.module.csv.CsvHandler;
import dev.dornol.codebox.exceldownload.module.excel.ExcelHandler;

public interface BookService {

    /**
 * Provides an Excel handler for processing BookDto objects.
 *
 * @return an ExcelHandler instance for BookDto entities
 */
ExcelHandler<BookDto> getExcelHandler();

    /**
 * Returns a CSV handler for processing {@code BookDto} objects.
 *
 * @return a {@code CsvHandler} instance for {@code BookDto}
 */
CsvHandler<BookDto> getCsvHandler();

}
