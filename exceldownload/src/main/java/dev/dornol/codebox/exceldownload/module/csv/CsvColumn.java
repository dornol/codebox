package dev.dornol.codebox.exceldownload.module.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CsvColumn<T> {
    private static final Logger log = LoggerFactory.getLogger(CsvColumn.class);

    private final String name;
    private final CsvRowFunction<T, Object> function;

    /**
     * Constructs a CsvColumn with the specified column name and value extraction function.
     *
     * @param name the name of the CSV column; must not be null
     * @param function the function used to extract or compute the column value from a row; must not be null
     * @throws IllegalArgumentException if name or function is null
     */
    public CsvColumn(String name, CsvRowFunction<T, Object> function) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (function == null) {
            throw new IllegalArgumentException("function must not be null");
        }
        this.name = name;
        this.function = function;
    }

    /**
     * Applies the column's function to the provided row data and cursor.
     *
     * If an exception occurs during function execution, logs the error and returns {@code null}.
     *
     * @param rowData the data object representing the current row
     * @param cursor the cursor indicating the current position in the CSV
     * @return the result of the function, or {@code null} if an exception was thrown
     */
    Object applyFunction(T rowData, CsvCursor cursor) {
        try {
            return function.apply(rowData, cursor);
        } catch (Exception e) {
            log.error("Failed to apply function for column '{}' at row {}", name, cursor.getRowOfSheet(), e);
            return null;
        }
    }

    /**
     * Returns the name of the CSV column.
     *
     * @return the column name
     */
    public String getName() {
        return name;
    }
}
