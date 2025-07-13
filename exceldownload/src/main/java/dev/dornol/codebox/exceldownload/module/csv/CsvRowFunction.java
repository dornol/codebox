package dev.dornol.codebox.exceldownload.module.csv;

@FunctionalInterface
public interface CsvRowFunction<T, R> {
    /**
 * Applies a function to a CSV row and its cursor context, producing a result.
 *
 * @param rowData the data of the current CSV row
 * @param cursor the cursor providing context for the current row
 * @return the result of processing the row and cursor
 */
R apply(T rowData, CsvCursor cursor);
}
