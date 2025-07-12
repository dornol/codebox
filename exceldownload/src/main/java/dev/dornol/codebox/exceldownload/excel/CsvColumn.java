package dev.dornol.codebox.exceldownload.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvColumn<T> {
    private static final Logger log = LoggerFactory.getLogger(CsvColumn.class);

    final String name;
    final CsvRowFunction<T, Object> function;

    public CsvColumn(String name, CsvRowFunction<T, Object> function) {
        this.name = name;
        this.function = function;
    }

    Object applyFunction(T rowData, CsvCursor cursor) {
        try {
            return function.apply(rowData, cursor);
        } catch (Exception e) {
            log.error("applyFunction exception caught : {}, {} \n", rowData, cursor, e);
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public CsvRowFunction<T, Object> getFunction() {
        return function;
    }
}
