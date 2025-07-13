package dev.dornol.codebox.exceldownload.module.excel;

@FunctionalInterface
public interface ExcelConsumer<T> {

    void accept(T rowData, ExcelCursor cursor);

}