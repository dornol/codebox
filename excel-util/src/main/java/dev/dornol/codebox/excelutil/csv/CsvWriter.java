package dev.dornol.codebox.excelutil.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvWriter<T> {
    private final List<CsvColumn<T>> columns = new ArrayList<>();


    public CsvWriter<T> column(String name, CsvRowFunction<T, Object> function) {
        var column = new CsvColumn<>(name, function);
        this.columns.add(column);
        return this;
    }

    public CsvWriter<T> column(String name, Function<T, Object> function) {
        return column(name, (r, c) -> function.apply(r));
    }

    public CsvWriter<T> constColumn(String name, Object value) {
        return column(name, (r, c) -> value);
    }

    public CsvHandler write(Stream<T> stream) {
        Path tempFile;
        try {
            tempFile = Files.createTempFile(UUID.randomUUID().toString(), ".csv");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        try (OutputStream os = Files.newOutputStream(tempFile)) {
            writeTempFile(stream, os);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return new CsvHandler(tempFile);
    }

    private void writeTempFile(Stream<T> stream, OutputStream outputStream) {
        Stream<T> sequential = stream.sequential();
        try (
                sequential;
                var writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
        ) {
            CsvCursor cursor = new CsvCursor();
            cursor.initRow();

            // 헤더 출력
            writer.println(columns.stream()
                    .map(CsvColumn::getName)
                    .reduce((a, b) -> a + "," + b).orElse(""));
            cursor.plusRow();

            // 데이터 출력
            sequential.forEach(row -> {
                cursor.plusTotal();
                cursor.plusRow();
                String line = columns.stream()
                        .map(col -> col.applyFunction(row, cursor))
                        .map(CsvWriter::escapeCsv)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");
                writer.println(line);
            });
        }
    }

    private static String escapeCsv(Object input) {
        if (input == null) {
            return "";
        }
        String value = input.toString();
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

}
