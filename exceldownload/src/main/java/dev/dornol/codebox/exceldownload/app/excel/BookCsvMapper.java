package dev.dornol.codebox.exceldownload.app.excel;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.excel.CsvHandler;

import java.util.stream.Stream;

public final class BookCsvMapper {
    private BookCsvMapper() {
        /* empty */
    }

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
