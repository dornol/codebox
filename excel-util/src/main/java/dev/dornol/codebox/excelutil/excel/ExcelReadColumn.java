package dev.dornol.codebox.excelutil.excel;

import org.apache.poi.ss.usermodel.Cell;

import java.io.InputStream;
import java.util.function.BiConsumer;

record ExcelReadColumn<T>(BiConsumer<T, Cell> setter) {

    public static class ExcelReadColumnBuilder<T> {
        private final ExcelReader<T> reader;
        private final BiConsumer<T, Cell> setter;
        private ExcelDataType dataType;
        private ExcelColumnSetter columnSetter;

        ExcelReadColumnBuilder(ExcelReader<T> reader, BiConsumer<T, Cell> setter) {
            this.reader = reader;
            this.setter = setter;
        }

        public ExcelReadColumnBuilder<T> type(ExcelDataType dataType) {
            this.dataType = dataType;
            return this;
        }

        private ExcelReadColumn<T> build() {
            if (this.dataType == null) {
                this.type(ExcelDataType.STRING);
            }
            if (this.columnSetter == null) {
                this.columnSetter = this.dataType.getSetter();
            }
            return new ExcelReadColumn<>(this.setter);
        }

        public ExcelReadColumnBuilder<T> column(BiConsumer<T, Cell> setter) {
            this.reader.addColumn(this.build());
            return new ExcelReadColumnBuilder<>(reader, setter);
        }

        public ExcelReadHandler<T> read(InputStream inputStream) {
            this.reader.addColumn(this.build());
            return this.reader.read(inputStream);
        }

    }

}
