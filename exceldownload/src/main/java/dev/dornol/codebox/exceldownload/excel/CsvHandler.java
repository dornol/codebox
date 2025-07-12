package dev.dornol.codebox.exceldownload.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvHandler<T> {
    private static final Logger log = LoggerFactory.getLogger(CsvHandler.class);
    private final List<CsvColumn<T>> columns = new ArrayList<>();
    private Path tempFile;

    public CsvHandler<T> column(String name, CsvRowFunction<T, Object> function) {
        var column = new CsvColumn<T>(name, function);
        this.columns.add(column);
        return this;
    }

    public CsvHandler<T> column(String name, Function<T, Object> function) {
        return column(name, (r, c) -> function.apply(r));
    }

    public CsvHandler<T> constColumn(String name, Object value) {
        return column(name, (r, c) -> value);
    }

    public CsvHandler<T> write(Stream<T> stream) {
        try {
            tempFile = Files.createTempFile(UUID.randomUUID().toString(), ".csv");
        } catch (IOException e) {
            log.error("create temp file failed: {}", e.getMessage());
            throw new IllegalStateException(e);
        }

        try (OutputStream os = Files.newOutputStream(tempFile)) {
            writeTempFile(stream, os);
        } catch (IOException e) {
            log.error("write csv failed: {}", e.getMessage());
            throw new IllegalStateException(e);
        }

        return this;
    }

    void writeTempFile(Stream<T> stream, OutputStream outputStream) {
        try (
                stream;
                var writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
        ) {
            CsvCursor cursor = new CsvCursor(columns.size());
            cursor.initRow();

            // 헤더 출력
            writer.println(columns.stream()
                    .map(CsvColumn::getName)
                    .reduce((a, b) -> a + "," + b).orElse(""));
            cursor.plusRow();

            // 데이터 출력
            stream.forEach(row -> {
                cursor.plusTotal();
                cursor.plusRow();
                String line = columns.stream()
                        .map(col -> col.applyFunction(row, cursor))
                        .map(CsvHandler::escapeCsv)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");
                writer.println(line);
            });
        }
    }

    public void consumeOutputStream(OutputStream outputStream) {
        try {
            try (InputStream is = Files.newInputStream(tempFile)) {
                is.transferTo(outputStream);
            } finally {
                Files.deleteIfExists(tempFile);
            }
        } catch (IOException e) {
            log.error("consumeOutputStream failed: {}", e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    private static String escapeCsv(Object input) {
        if (input == null) return "";
        String value = input.toString();
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}