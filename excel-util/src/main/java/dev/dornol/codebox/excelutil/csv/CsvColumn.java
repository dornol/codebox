package dev.dornol.codebox.excelutil.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CsvColumn<T> {
    private static final Logger log = LoggerFactory.getLogger(CsvColumn.class);

    private final String name;
    private final CsvRowFunction<T, Object> function;

    CsvColumn(String name, CsvRowFunction<T, Object> function) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (function == null) {
            throw new IllegalArgumentException("function must not be null");
        }
        this.name = name;
        this.function = function;
    }

    Object applyFunction(T rowData, CsvCursor cursor) {
        try {
            return function.apply(rowData, cursor);
        } catch (Exception e) {
            log.error("Failed to apply function for column '{}' at row {}", name, cursor.getRowOfSheet(), e);
            return null;
        }
    }

    String getName() {
        return name;
    }
}
