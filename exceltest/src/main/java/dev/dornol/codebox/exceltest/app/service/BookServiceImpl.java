package dev.dornol.codebox.exceltest.app.service;

import dev.dornol.codebox.exceltest.app.dto.BookReadDto;
import dev.dornol.codebox.exceltest.app.dto.TypeTestReadDto;
import dev.dornol.codebox.exceltest.app.excel.BookCsvMapper;
import dev.dornol.codebox.exceltest.app.excel.BookExcelMapper;
import dev.dornol.codebox.exceltest.app.excel.TypeTestExcelMapper;
import dev.dornol.codebox.exceltest.app.model.Book;
import dev.dornol.codebox.exceltest.app.repository.BookRepository;
import dev.dornol.codebox.excelutil.csv.CsvHandler;
import dev.dornol.codebox.excelutil.excel.ExcelHandler;
import dev.dornol.codebox.excelutil.excel.ExcelReadHandler;
import jakarta.persistence.EntityManager;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final Validator validator;
    private final EntityManager em;

    public BookServiceImpl(BookRepository bookRepository, Validator validator, EntityManager em) {
        this.bookRepository = bookRepository;
        this.validator = validator;
        this.em = em;
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

    @Transactional
    @Override
    public void readAndSaveExcel(InputStream inputStream) {
        long start = System.currentTimeMillis();
        ExcelReadHandler<BookReadDto> handler = BookExcelMapper.getReadHandler(inputStream, validator);
        final int[] counts = {0};
        final int[] successCounts = {0};
        final int[] errorCounts = {0};
        List<Book> books = new ArrayList<>();
        handler.read(result -> {
            if (result.success()) {
                books.add(new Book(
                        null,
                        result.data().getTitle(),
                        result.data().getSubtitle(),
                        result.data().getAuthor(),
                        result.data().getPublisher(),
                        result.data().getIsbn(),
                        result.data().getDescription()
                ));
                successCounts[0]++;
            } else {
                errorCounts[0]++;
                log.warn("failed to read excel: {}", result.messages());
            }
            counts[0]++;
            if (counts[0] > 1000) {
                log.info("read excel finished");
                bookRepository.saveAll(books);
                em.flush();
                em.clear();
                books.clear();
                log.info("flush and clear finished");
                counts[0] = 0;
            }
        });
        long end = System.currentTimeMillis();
        log.info("read and save excel finished: {}ms, success: {}, error: {}", end - start, successCounts[0], errorCounts[0]);
    }

}
