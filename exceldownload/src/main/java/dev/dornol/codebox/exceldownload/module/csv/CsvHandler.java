package dev.dornol.codebox.exceldownload.module.csv;

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

public class CsvHandler<T> implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(CsvHandler.class);
    private final List<CsvColumn<T>> columns = new ArrayList<>();
    private Path tempFile;

    /**
     * Adds a new CSV column with the specified name and a function to extract its value from each row.
     *
     * @param name the name of the CSV column
     * @param function a function that computes the column value from a row and cursor context
     * @return this CsvHandler instance for method chaining
     */
    public CsvHandler<T> column(String name, CsvRowFunction<T, Object> function) {
        var column = new CsvColumn<T>(name, function);
        this.columns.add(column);
        return this;
    }

    /**
     * Adds a CSV column with the specified name, using a function to extract the column value from each data row.
     *
     * @param name the name of the CSV column
     * @param function a function that maps a data row to the column value
     * @return this CsvHandler instance for method chaining
     */
    public CsvHandler<T> column(String name, Function<T, Object> function) {
        return column(name, (r, c) -> function.apply(r));
    }

    /**
     * Adds a CSV column with the specified name and a constant value for all rows.
     *
     * @param name the name of the column
     * @param value the constant value to use for every row in this column
     * @return this CsvHandler instance for method chaining
     */
    public CsvHandler<T> constColumn(String name, Object value) {
        return column(name, (r, c) -> value);
    }

    /**
     * Writes the provided stream of data rows to a newly created temporary CSV file using the defined columns.
     *
     * @param stream the stream of data rows to write to the CSV file
     * @return this CsvHandler instance for method chaining
     * @throws IllegalStateException if an I/O error occurs during file creation or writing
     */
    public CsvHandler<T> write(Stream<T> stream) {
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

        return this;
    }

    /**
     * Writes the provided stream of data rows to the given output stream in CSV format, including a header row and properly escaped values.
     *
     * @param stream        the stream of data rows to write as CSV
     * @param outputStream  the output stream to which the CSV content is written
     */
    void writeTempFile(Stream<T> stream, OutputStream outputStream) {
        Stream<T> sequential = stream.sequential();
        try (
                sequential;
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
            sequential.forEach(row -> {
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

    /**
     * Streams the contents of the temporary CSV file to the provided output stream and deletes the temporary file afterward.
     *
     * @param outputStream the output stream to which the CSV content will be written
     * @throws IllegalStateException if an I/O error occurs during reading or writing
     */
    public void consumeOutputStream(OutputStream outputStream) {
        try {
            try (InputStream is = Files.newInputStream(tempFile)) {
                is.transferTo(outputStream);
            } finally {
                this.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Converts the given object to a CSV-safe string, escaping special characters as needed.
     *
     * If the input is null, returns an empty string. If the string representation contains commas, quotes, or newlines,
     * the value is enclosed in double quotes and internal quotes are escaped by doubling them.
     *
     * @param input the object to convert and escape for CSV output
     * @return the CSV-escaped string representation of the input
     */
    private static String escapeCsv(Object input) {
        if (input == null) return "";
        String value = input.toString();
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Deletes the temporary CSV file if it exists, suppressing any IO exceptions.
     *
     * This method is called to clean up resources when the handler is closed.
     */
    @Override
    public void close() {
        if (tempFile != null) {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                log.warn("Failed to delete temp file: {}", tempFile, e);
            }
        }
    }
}