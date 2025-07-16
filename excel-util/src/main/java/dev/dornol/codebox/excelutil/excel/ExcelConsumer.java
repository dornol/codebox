package dev.dornol.codebox.excelutil.excel;

@FunctionalInterface
public interface ExcelConsumer<T> {

    void accept(T rowData, ExcelCursor cursor);

}