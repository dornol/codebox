package dev.dornol.codebox.exceldownload.excel;

@FunctionalInterface
public interface CsvRowFunction<T, R> {
    R apply(T rowData, CsvCursor cursor);
}
