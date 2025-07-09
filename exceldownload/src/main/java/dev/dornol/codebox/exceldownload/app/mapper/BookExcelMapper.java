package dev.dornol.codebox.exceldownload.app.mapper;

import dev.dornol.codebox.exceldownload.app.dto.BookDto;
import dev.dornol.codebox.exceldownload.excel.ExcelDataType;
import dev.dornol.codebox.exceldownload.excel.ExcelHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookExcelMapper {

    public static ExcelHandler<BookDto> getExcelHandler(Stream<BookDto> stream) {
        var handler = new ExcelHandler<BookDto>();
        return handler
                .column("no", (rowData, cursor) -> cursor.getCurrentTotal()).type(ExcelDataType.INTEGER)
                .column("id", ((rowData, cursor) -> rowData.id())).type(ExcelDataType.LONG)
                .column("title", ((rowData, cursor) -> rowData.title()))
                .column("subtitle", ((rowData, cursor) -> rowData.subtitle()))
                .column("author", ((rowData, cursor) -> rowData.author()))
                .column("publisher", ((rowData, cursor) -> rowData.publisher()))
                .column("isbn", ((rowData, cursor) -> rowData.isbn()))
                .column("description", ((rowData, cursor) -> rowData.description()))
                .write(stream);
    }

}
