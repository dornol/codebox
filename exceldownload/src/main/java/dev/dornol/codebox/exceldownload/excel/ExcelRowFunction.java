package dev.dornol.codebox.exceldownload.excel;

@FunctionalInterface
public interface ExcelRowFunction<T, R> {
    R apply(T rowData, ExcelCursor cursor);
}
