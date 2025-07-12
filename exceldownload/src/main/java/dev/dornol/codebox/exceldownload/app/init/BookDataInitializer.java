package dev.dornol.codebox.exceldownload.app.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookDataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(BookDataInitializer.class);
    private final JdbcTemplate jdbcTemplate;
    private final Long dummyCount;

    public BookDataInitializer(JdbcTemplate jdbcTemplate, @Value("${example.dummy-count:1000000}") Long dummyCount) {
        this.jdbcTemplate = jdbcTemplate;
        this.dummyCount = dummyCount;
    }

    @Override
    public void run(String... args) {
        log.info("book data initializing...");
        int batchSize = 1000;
        long total = dummyCount;
        for (int i = 0; i < total; i += batchSize) {
            List<Object[]> batch = new ArrayList<>();
            for (int j = 0; j < batchSize; j++) {
                long id = i + j + 1L;
                batch.add(new Object[]{
                        id,
                        "Title " + id,
                        "Subtitle " + id,
                        "Author " + id,
                        "Publisher " + id,
                        String.format("%013d", id),
                        "Description " + id
                });
            }
            jdbcTemplate.batchUpdate("""
                INSERT INTO book (id, title, subtitle, author, publisher, isbn, description)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """, batch);
        }
        log.warn("book data initialized: {}", total);
        log.warn("book data query: {}", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM book", Long.class));
    }
}
