package dev.dornol.codebox.exceltest.app.excel;

import dev.dornol.codebox.exceltest.app.dto.BookDto;
import dev.dornol.codebox.excelutil.excel.ExcelDataType;
import dev.dornol.codebox.excelutil.excel.ExcelHandler;
import dev.dornol.codebox.excelutil.excel.ExcelWriter;

import java.util.stream.Stream;

public class BookExcelMapper {

    private BookExcelMapper() {
        /* empty */
    }

    public static ExcelHandler getHandler(Stream<BookDto> stream) {
        return new ExcelWriter<BookDto>(0xCC, 0xFF, 0x99)
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
