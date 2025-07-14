package dev.dornol.codebox.exceldownload.module.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvHandler {
    private static final Logger log = LoggerFactory.getLogger(CsvHandler.class);
    private final Path tempFile;
    private boolean consumed = false;

    CsvHandler(Path tempFile) {
        this.tempFile = tempFile;
    }

    public void consumeOutputStream(OutputStream outputStream) {
        if (consumed) {
            throw new IllegalStateException("Already consumed");
        }
        try {
            try (InputStream is = Files.newInputStream(tempFile)) {
                is.transferTo(outputStream);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            consumed = true;
            this.close();
        }
    }

    private void close() {
        if (tempFile != null) {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                log.warn("Failed to delete temp file: {}", tempFile, e);
            }
        }
    }
}
