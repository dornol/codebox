package dev.dornol.codebox.excelutil.excel;

import java.util.List;

public record ExcelReadResult<T>(
        T data,
        boolean success,
        List<String> messages
) {
}
