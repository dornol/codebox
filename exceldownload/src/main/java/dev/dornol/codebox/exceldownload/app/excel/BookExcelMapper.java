package dev.dornol.codebox.exceldownload.app.excel;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.module.excel.ExcelDataType;
import dev.dornol.codebox.exceldownload.module.excel.ExcelHandler;

import java.util.stream.Stream;

public class BookExcelMapper {

    /**
     * Prevents instantiation of the {@code BookExcelMapper} utility class.
     */
    private BookExcelMapper() {
        /* empty */
    }

    /**
     * Creates and configures an {@link ExcelHandler} for exporting a stream of {@link BookDto} objects to Excel format.
     *
     * The handler is set up with a specific background color and columns mapped to the fields of {@code BookDto}, including a row index ("no") and all major book attributes.
     *
     * @param stream the stream of {@code BookDto} objects to be written to the Excel handler
     * @return an {@code ExcelHandler<BookDto>} configured with columns and populated with the provided data
     */
    public static ExcelHandler<BookDto> getHandler(Stream<BookDto> stream) {
        var handler = new ExcelHandler<BookDto>(0xCC, 0xFF, 0x99);
        return handler
                .column("no", (rowData, cursor) -> cursor.getCurrentTotal()).type(ExcelDataType.INTEGER)
                .column("id", BookDto::id).type(ExcelDataType.LONG)
                .column("title", BookDto::title)
                .column("subtitle", BookDto::subtitle)
                .column("author", BookDto::author)
                .column("publisher", BookDto::publisher)
                .column("isbn", BookDto::isbn)
                .column("description", BookDto::description)
                .write(stream);
    }

}
