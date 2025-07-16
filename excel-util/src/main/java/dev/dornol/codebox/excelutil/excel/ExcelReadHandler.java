package dev.dornol.codebox.excelutil.excel;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public record ExcelReadHandler<T>(Stream<T> stream) {
    public void consume(Consumer<T> consumer) {
        stream.forEach(consumer);
    }

    public List<T> collect() {
        return stream.toList();
    }
}
