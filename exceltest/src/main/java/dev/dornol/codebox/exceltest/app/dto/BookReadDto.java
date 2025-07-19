package dev.dornol.codebox.exceltest.app.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class BookReadDto {
    @NotNull(message = "ID must not be null")
    private Long id;

    @NotBlank(message = "Title must not be blank")
    @Size(min = 3, max = 195, message = "Title must be between 3 and 195 characters")
    private String title;

    @NotBlank(message = "Subtitle must not be blank")
    @Size(min = 3, max = 195, message = "Subtitle must be between 3 and 195 characters")
    private String subtitle;

    @NotBlank(message = "Author must not be blank")
    @Size(min = 3, max = 195, message = "Author must be between 3 and 195 characters")
    private String author;

    @NotBlank(message = "Publisher must not be blank")
    @Size(min = 3, max = 195, message = "Publisher must be between 3 and 195 characters")
    private String publisher;

    @NotBlank(message = "ISBN must not be blank")
    @Size(min = 3, max = 195, message = "ISBN must be between 3 and 195 characters")
    private String isbn;

    @NotBlank(message = "Description must not be blank")
    @Size(min = 3, max = 195, message = "Description must be between 3 and 195 characters")
    private String description;

    @AssertTrue(message = "@@@@@ contains dhkim!! @@@@@")
    public boolean isCustomValidation() {
        return !title.contains("dhkim") && !subtitle.contains("dhkim") && !author.contains("dhkim") && !publisher.contains("dhkim") && !isbn.contains("dhkim") && !description.contains("dhkim");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
