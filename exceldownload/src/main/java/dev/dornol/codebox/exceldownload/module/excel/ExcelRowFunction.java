package dev.dornol.codebox.exceldownload.module.excel;

@FunctionalInterface
public interface ExcelRowFunction<T, R> {
    R apply(T rowData, ExcelCursor cursor);
}
