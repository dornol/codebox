package dev.dornol.codebox.exceldownload.app.service;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.app.excel.BookCsvMapper;
import dev.dornol.codebox.exceldownload.app.excel.BookExcelMapper;
import dev.dornol.codebox.exceldownload.app.repository.BookRepository;
import dev.dornol.codebox.exceldownload.module.csv.CsvHandler;
import dev.dornol.codebox.exceldownload.module.excel.ExcelHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    /**
     * Constructs a BookServiceImpl with the specified BookRepository.
     *
     * @param bookRepository the repository used for accessing book data
     */
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Returns an ExcelHandler for exporting book data as Excel files.
     *
     * @return an ExcelHandler configured with a stream of book data
     */
    @Transactional(readOnly = true)
    @Override
    public ExcelHandler<BookDto> getExcelHandler() {
        return BookExcelMapper.getHandler(bookRepository.getStream());
    }

    /**
     * Creates a CSV handler for exporting book data.
     *
     * @return a {@link CsvHandler} for handling CSV export of {@link BookDto} objects
     */
    @Transactional(readOnly = true)
    @Override
    public CsvHandler<BookDto> getCsvHandler() {
        return BookCsvMapper.getHandler(bookRepository.getStream());
    }

}
