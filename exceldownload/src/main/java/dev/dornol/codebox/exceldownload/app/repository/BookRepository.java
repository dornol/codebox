package dev.dornol.codebox.exceldownload.app.repository;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.app.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            select new dev.dornol.codebox.exceldownload.app.dto.BookDto(b.id, b.title, b.subtitle, b.author, b.publisher, b.isbn, b.description)
            from Book b
            where b.id > 0
            order by b.id desc
            """)
    Stream<BookDto> getStream();

}
