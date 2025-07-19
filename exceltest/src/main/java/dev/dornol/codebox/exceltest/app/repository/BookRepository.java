package dev.dornol.codebox.exceltest.app.repository;

import dev.dornol.codebox.exceltest.app.dto.BookDto;
import dev.dornol.codebox.exceltest.app.model.Book;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {

    @QueryHints(value = @QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "1000"))
    @Query("""
            select new dev.dornol.codebox.exceltest.app.dto.BookDto(b.id, b.title, b.subtitle, b.author, b.publisher, b.isbn, b.description)
            from Book b
            where b.id > 0
            order by b.id desc
            """)
    Stream<BookDto> getStream();

}
