package dev.dornol.codebox.excelutil.excel;

import java.io.InputStream;
import java.util.function.BiConsumer;

record ExcelReadColumn<T>(BiConsumer<T, ExcelCellData> setter) {

    public static class ExcelReadColumnBuilder<T> {
        private final ExcelReader<T> reader;
        private final BiConsumer<T, ExcelCellData> setter;

        ExcelReadColumnBuilder(ExcelReader<T> reader, BiConsumer<T, ExcelCellData> setter) {
            this.reader = reader;
            this.setter = setter;
        }

        public ExcelReadColumnBuilder<T> column(BiConsumer<T, ExcelCellData> setter) {
            this.reader.addColumn(new ExcelReadColumn<>(this.setter));
            return new ExcelReadColumnBuilder<>(reader, setter);
        }

        public ExcelReadHandler<T> reader(InputStream inputStream) {
            this.reader.addColumn(new ExcelReadColumn<>(this.setter));
            return this.reader.build(inputStream);
        }

    }

}
