package dev.dornol.codebox.exceldownload.app.excel;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.module.excel.ExcelDataType;
import dev.dornol.codebox.exceldownload.module.excel.ExcelHandler;

import java.util.stream.Stream;

public class BookExcelMapper {

    private BookExcelMapper() {
        /* empty */
    }

    public static ExcelHandler<BookDto> getHandler(Stream<BookDto> stream) {
        var handler = new ExcelHandler<BookDto>();
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
