package dev.dornol.codebox.excelutil.excel;

import org.apache.poi.ss.usermodel.Cell;

import java.io.InputStream;
import java.util.function.BiConsumer;

record ExcelReadColumn<T>(BiConsumer<T, Cell> setter) {

    public static class ExcelReadColumnBuilder<T> {
        private final ExcelReader<T> reader;
        private final BiConsumer<T, Cell> setter;

        ExcelReadColumnBuilder(ExcelReader<T> reader, BiConsumer<T, Cell> setter) {
            this.reader = reader;
            this.setter = setter;
        }

        public ExcelReadColumnBuilder<T> column(BiConsumer<T, Cell> setter) {
            this.reader.addColumn(new ExcelReadColumn<>(this.setter));
            return new ExcelReadColumnBuilder<>(reader, setter);
        }

        public ExcelReadHandler<T> read(InputStream inputStream) {
            this.reader.addColumn(new ExcelReadColumn<>(this.setter));
            return this.reader.read(inputStream);
        }

    }

}
