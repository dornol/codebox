package dev.dornol.codebox.excelutil.excel;

@FunctionalInterface
public interface ExcelRowFunction<T, R> {
    R apply(T rowData, ExcelCursor cursor);
}
