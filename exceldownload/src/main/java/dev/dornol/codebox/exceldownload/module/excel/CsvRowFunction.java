package dev.dornol.codebox.exceldownload.module.excel;

import dev.dornol.codebox.exceldownload.module.csv.CsvCursor;

@FunctionalInterface
public interface CsvRowFunction<T, R> {
    R apply(T rowData, CsvCursor cursor);
}
