package dev.dornol.codebox.exceltest.app.service;

import dev.dornol.codebox.exceltest.app.dto.TypeTestReadDto;
import dev.dornol.codebox.exceltest.app.excel.BookCsvMapper;
import dev.dornol.codebox.exceltest.app.excel.BookExcelMapper;
import dev.dornol.codebox.exceltest.app.excel.TypeTestExcelMapper;
import dev.dornol.codebox.exceltest.app.repository.BookRepository;
import dev.dornol.codebox.excelutil.csv.CsvHandler;
import dev.dornol.codebox.excelutil.excel.ExcelHandler;
import dev.dornol.codebox.excelutil.excel.ExcelReadHandler;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final Validator validator;

    public BookServiceImpl(BookRepository bookRepository, Validator validator) {
        this.bookRepository = bookRepository;
        this.validator = validator;
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

    @Override
    public void readExcel(InputStream inputStream) {
        ExcelReadHandler<TypeTestReadDto> readHandler = TypeTestExcelMapper.getReadHandler(inputStream, validator);
        readHandler.read(result ->
                log.info("success: {}, error: {}, dto: {}", result.success(), result.messages(), result.data()));
    }

}
