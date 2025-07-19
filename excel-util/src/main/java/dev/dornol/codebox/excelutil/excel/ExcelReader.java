package dev.dornol.codebox.excelutil.excel;

import jakarta.validation.Validator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ExcelReader<T> {
    private final List<ExcelReadColumn<T>> columns = new ArrayList<>();
    private final Supplier<T> instanceSupplier;
    private final Validator validator;

    public ExcelReader(Supplier<T> instanceSupplier, Validator validator) {
        this.instanceSupplier = instanceSupplier;
        this.validator = validator;
    }

    void addColumn(ExcelReadColumn<T> column) {
        columns.add(column);
    }

    public ExcelReadColumn.ExcelReadColumnBuilder<T> column(BiConsumer<T, ExcelCellData> setter) {
        return new ExcelReadColumn.ExcelReadColumnBuilder<>(this, setter);
    }

    public ExcelReadHandler<T> build(InputStream inputStream) {
        return new ExcelReadHandler<>(inputStream, columns, instanceSupplier, validator);
    }
}
