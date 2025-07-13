package dev.dornol.codebox.exceldownload.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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

    /**
     * Default constructor required by JPA for entity instantiation.
     */
    protected Book() {
    }

    /**
     * Constructs a new Book with the specified details.
     *
     * @param id          the unique identifier of the book
     * @param title       the title of the book
     * @param subtitle    the subtitle of the book
     * @param author      the author of the book
     * @param publisher   the publisher of the book
     * @param isbn        the ISBN of the book
     * @param description a description of the book
     */
    public Book(Long id, String title, String subtitle, String author, String publisher, String isbn, String description) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.description = description;
    }

    /**
     * Returns the unique identifier of the book.
     *
     * @return the book's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the title of the book.
     *
     * @return the book's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the subtitle of the book.
     *
     * @return the subtitle, or null if not set
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Returns the author of the book.
     *
     * @return the author's name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the publisher of the book.
     *
     * @return the publisher name
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Returns the ISBN of the book.
     *
     * @return the book's ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Returns the description of the book.
     *
     * @return the book's description
     */
    public String getDescription() {
        return description;
    }
}
