package dev.dornol.codebox.exceldownload.app.service;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.app.excel.BookCsvMapper;
import dev.dornol.codebox.exceldownload.app.excel.BookExcelMapper;
import dev.dornol.codebox.exceldownload.app.repository.BookRepository;
import dev.dornol.codebox.exceldownload.excel.CsvHandler;
import dev.dornol.codebox.exceldownload.excel.ExcelHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public ExcelHandler<BookDto> getExcelHandler() {
        return BookExcelMapper.getHandler(bookRepository.getStream());
    }

    @Transactional(readOnly = true)
    @Override
    public CsvHandler<BookDto> getCsvHandler() {
        return BookCsvMapper.getHandler(bookRepository.getStream());
    }

}
