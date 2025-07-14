package dev.dornol.codebox.exceldownload.app.service;

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

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public ExcelHandler getExcelHandler() {
        return BookExcelMapper.getHandler(bookRepository.getStream());
    }

    @Transactional(readOnly = true)
    @Override
    public CsvHandler getCsvHandler() {
        return BookCsvMapper.getHandler(bookRepository.getStream());
    }

}
