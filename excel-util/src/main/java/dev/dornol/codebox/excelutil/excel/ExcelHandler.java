package dev.dornol.codebox.excelutil.excel;

import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

public class ExcelHandler {
    private final SXSSFWorkbook wb;
    private boolean consumed = false;

    ExcelHandler(SXSSFWorkbook wb) {
        this.wb = wb;
    }

    public void consumeOutputStream(OutputStream outputStream) throws IOException {
        if (consumed) {
            throw new IllegalStateException("Already consumed");
        }
        try {
            wb.write(outputStream);
        } finally {
            consumed = true;
            wb.close();
        }
    }

    public void consumeOutputStreamWithPassword(OutputStream outputStream, String password) throws IOException {
        if (consumed) {
            throw new IllegalStateException("Already consumed");
        }
        try (POIFSFileSystem fs = new POIFSFileSystem()) {
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor enc = info.getEncryptor();
            enc.confirmPassword(password);

            try (OutputStream encOut = enc.getDataStream(fs)) {
                wb.write(encOut);
            } catch (GeneralSecurityException e) {
                throw new IllegalStateException(e);
            }

            fs.writeFilesystem(outputStream);
        } finally {
            consumed = true;
            wb.close();
        }
    }

}
