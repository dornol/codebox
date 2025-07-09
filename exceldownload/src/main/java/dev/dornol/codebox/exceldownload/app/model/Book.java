package dev.dornol.codebox.exceldownload.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book {

    @Id
    private Long id;

    private String title;

    private String subtitle;

    private String author;

    private String publisher;

    private String isbn;

    private String description;

    public Book(Long id, String title, String subtitle, String author, String publisher, String isbn, String description) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.description = description;
    }
}
