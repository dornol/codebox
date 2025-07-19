package dev.dornol.codebox.excelutil.csv;

import dev.dornol.codebox.excelutil.TempFileContainer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvHandler extends TempFileContainer {
    private boolean consumed = false;

    CsvHandler(Path tempDir, Path tempFile) {
        setTempFile(tempFile);
        setTempDir(tempDir);
    }

    public void consumeOutputStream(OutputStream outputStream) {
        if (consumed) {
            throw new IllegalStateException("Already consumed");
        }
        try {
            try (InputStream is = Files.newInputStream(getTempFile())) {
                is.transferTo(outputStream);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            consumed = true;
            super.close();
        }
    }
}
