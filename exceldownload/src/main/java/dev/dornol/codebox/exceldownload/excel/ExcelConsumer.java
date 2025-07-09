package dev.dornol.codebox.exceldownload.excel;

@FunctionalInterface
public interface ExcelConsumer<T> {

    void accept(T rowData, ExcelCursor cursor);

}