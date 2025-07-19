package dev.dornol.codebox.excelutil.excel;

import java.io.InputStream;
import java.util.function.BiConsumer;

/**
 * Represents a single Excel column binding for reading.
 * <p>
 * Holds a setter function that maps a {@link ExcelCellData} into a field of a row object.
 *
 * @param <T> The row data type
 *
 * @author dhkim
 * @since 2025-07-19
 */
record ExcelReadColumn<T>(BiConsumer<T, ExcelCellData> setter) {

    /**
     * Builder for defining multiple Excel read columns fluently.
     *
     * @param <T> The row data type
     */
    public static class ExcelReadColumnBuilder<T> {
        private final ExcelReader<T> reader;
        private final BiConsumer<T, ExcelCellData> setter;

        /**
         * Constructs a column builder.
         *
         * @param reader The parent ExcelReader
         * @param setter The setter function for this column
         */
        ExcelReadColumnBuilder(ExcelReader<T> reader, BiConsumer<T, ExcelCellData> setter) {
            this.reader = reader;
            this.setter = setter;
        }

        /**
         * Adds the current column and begins a new column definition.
         *
         * @param setter The setter function for the next column
         * @return A new column builder for chaining
         */
        public ExcelReadColumnBuilder<T> column(BiConsumer<T, ExcelCellData> setter) {
            this.reader.addColumn(new ExcelReadColumn<>(this.setter));
            return new ExcelReadColumnBuilder<>(reader, setter);
        }

        /**
         * Finalizes column definitions and builds an {@link ExcelReadHandler} to start reading.
         *
         * @param inputStream The input stream of the Excel file
         * @return Configured ExcelReadHandler
         */
        public ExcelReadHandler<T> build(InputStream inputStream) {
            this.reader.addColumn(new ExcelReadColumn<>(this.setter));
            return this.reader.build(inputStream);
        }

    }

}
