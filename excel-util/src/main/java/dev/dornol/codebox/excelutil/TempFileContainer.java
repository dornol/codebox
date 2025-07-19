package dev.dornol.codebox.excelutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempFileContainer implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(TempFileContainer.class);
    private Path tempDir;
    private Path tempFile;

    protected Path getTempDir() {
        return tempDir;
    }

    protected void setTempDir(Path tempDir) {
        this.tempDir = tempDir;
    }

    protected Path getTempFile() {
        return tempFile;
    }

    protected void setTempFile(Path tempFile) {
        this.tempFile = tempFile;
    }

    @Override
    public void close() {
        if (tempFile != null) {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                log.warn("Failed to delete temp file: {}", tempFile, e);
            }
        }
        if (tempDir != null) {
            try {
                Files.deleteIfExists(tempDir);
            } catch (IOException e) {
                log.warn("Failed to delete temp dir: {}", tempDir, e);
            }
        }
    }
}
