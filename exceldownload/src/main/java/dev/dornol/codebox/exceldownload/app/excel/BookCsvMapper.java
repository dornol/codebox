package dev.dornol.codebox.exceldownload.app.excel;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.module.csv.CsvHandler;

import java.util.stream.Stream;

public final class BookCsvMapper {
    /**
     * Prevents instantiation of the {@code BookCsvMapper} utility class.
     */
    private BookCsvMapper() {
        /* empty */
    }

    /**
     * Creates and configures a {@code CsvHandler} for {@code BookDto} objects with predefined columns and writes the provided stream to it.
     *
     * The columns included are: "no" (row number), "id", "title", "subtitle", "author", "publisher", "isbn", and "description".
     *
     * @param stream the stream of {@code BookDto} objects to be written to the CSV handler
     * @return a configured {@code CsvHandler<BookDto>} containing the mapped data
     */
    public static CsvHandler<BookDto> getHandler(Stream<BookDto> stream) {
        var handler = new CsvHandler<BookDto>();
        return handler
                .column("no", (rowData, cursor) -> cursor.getCurrentTotal())
                .column("id", BookDto::id)
                .column("title", BookDto::title)
                .column("subtitle", BookDto::subtitle)
                .column("author", BookDto::author)
                .column("publisher", BookDto::publisher)
                .column("isbn", BookDto::isbn)
                .column("description", BookDto::description)
                .write(stream);
    }

}
