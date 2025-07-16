package dev.dornol.codebox.excelutil.csv;

@FunctionalInterface
public interface CsvRowFunction<T, R> {
    R apply(T rowData, CsvCursor cursor);
}
