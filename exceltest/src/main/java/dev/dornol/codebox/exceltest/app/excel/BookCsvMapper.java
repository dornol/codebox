package dev.dornol.codebox.exceltest.app.excel;

import dev.dornol.codebox.exceltest.app.dto.BookDto;
import dev.dornol.codebox.excelutil.csv.CsvHandler;
import dev.dornol.codebox.excelutil.csv.CsvWriter;

import java.util.stream.Stream;

public final class BookCsvMapper {
    private BookCsvMapper() {
        /* empty */
    }

    public static CsvHandler getHandler(Stream<BookDto> stream) {
        return new CsvWriter<BookDto>()
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
