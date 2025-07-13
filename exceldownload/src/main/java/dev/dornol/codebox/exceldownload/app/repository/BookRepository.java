package dev.dornol.codebox.exceldownload.app.repository;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.app.model.Book;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Returns a stream of {@link BookDto} objects representing books with IDs greater than zero, ordered by ID in descending order.
     *
     * <p>The query constructs each {@code BookDto} from the corresponding {@code Book} entity fields. The fetch size is set to 1000 to optimize streaming large result sets.
     *
     * @return a stream of {@code BookDto} instances matching the query criteria
     */
    @QueryHints(value = @QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "1000"))
    @Query("""
            select new dev.dornol.codebox.exceldownload.app.dto.BookDto(b.id, b.title, b.subtitle, b.author, b.publisher, b.isbn, b.description)
            from Book b
            where b.id > 0
            order by b.id desc
            """)
    Stream<BookDto> getStream();

}
