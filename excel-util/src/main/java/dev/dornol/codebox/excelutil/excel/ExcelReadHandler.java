package dev.dornol.codebox.excelutil.excel;

import dev.dornol.codebox.excelutil.TempFileContainer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Reads Excel (.xlsx) files using Apache POI's event-based streaming API.
 * <p>
 * This handler parses sheet data row by row, maps values to Java objects, and performs optional validation.
 * It is optimized for large files and avoids loading the entire workbook into memory.
 *
 * @param <T> The target row data type to map each row into
 *
 * @author dhkim
 * @since 2025-07-19
 */
public class ExcelReadHandler<T> extends TempFileContainer {
    private static final Logger log = LoggerFactory.getLogger(ExcelReadHandler.class);
    private final List<ExcelReadColumn<T>> columns;
    private final Supplier<T> instanceSupplier;
    private final Validator validator;

    /**
     * Constructs a handler for reading Excel files.
     *
     * @param inputStream      The input stream of the uploaded Excel file
     * @param columns          The list of column setters to apply per row
     * @param instanceSupplier A supplier to instantiate new row objects
     * @param validator        Optional bean validator for validating mapped instances
     */
    ExcelReadHandler(InputStream inputStream, List<ExcelReadColumn<T>> columns, Supplier<T> instanceSupplier, Validator validator) {
        this.columns = columns;
        this.instanceSupplier = instanceSupplier;
        this.validator = validator;
        try {
            setTempDir(Files.createTempDirectory(UUID.randomUUID().toString()));
            setTempFile(Files.createTempFile(getTempDir(), UUID.randomUUID().toString(), ".xlsx"));
            Files.copy(inputStream, getTempFile(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Starts parsing the Excel file and invokes the given consumer for each row result.
     *
     * @param consumer Callback to receive parsed and validated row results
     */
    public void read(Consumer<ExcelReadResult<T>> consumer) {
        try {
            OPCPackage pkg = OPCPackage.open(getTempFile().toFile());
            XSSFReader reader = new XSSFReader(pkg);

            SharedStrings ss = reader.getSharedStringsTable();
            StylesTable styles = reader.getStylesTable();

            XMLReader parser = XMLHelper.newXMLReader();
            SheetHandler sheetHandler = new SheetHandler(consumer);
            XSSFSheetXMLHandler sheetParser = new XSSFSheetXMLHandler(styles, ss, sheetHandler, false);
            parser.setContentHandler(sheetParser);

            try (InputStream sheet = reader.getSheetsData().next()) {
                parser.parse(new InputSource(sheet));
            }

        } catch (Exception e) {
            throw new IllegalStateException("Failed to read excel", e);
        } finally {
            close();
        }
    }


    /**
     * Internal handler for row-by-row Excel parsing.
     */
    private class SheetHandler extends DefaultHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
        private T currentInstance;
        private final List<ExcelCellData> currentRow = new ArrayList<>();
        private final List<String> headerNames = new ArrayList<>();
        private final Consumer<ExcelReadResult<T>> consumer;

        public SheetHandler(Consumer<ExcelReadResult<T>> consumer) {
            this.consumer = consumer;
        }

        /**
         * Called at the start of each row.
         */
        @Override
        public void startRow(int rowNum) {
            currentInstance = instanceSupplier.get();
            currentRow.clear();
        }

        /**
         * Called at the end of each row.
         * Performs column mapping and validation, and passes result to consumer.
         */
        @Override
        public void endRow(int rowNum) {
            if (rowNum == 0) {
                headerNames.addAll(currentRow.stream().map(ExcelCellData::formattedValue).toList());
                return;
            }

            boolean success = true;
            List<String> messages = null;

            for (int i = 0; i < columns.size(); i++) {
                if (i >= currentRow.size()) continue;
                try {
                    columns.get(i).setter().accept(currentInstance, currentRow.get(i));
                } catch (Exception e) {
                    if (messages == null) {
                        messages = new ArrayList<>();
                    }
                    success = false;
                    String header = (i < headerNames.size()) ? headerNames.get(i) : "column#" + i;
                    messages.add("Failed to set column: " + header);
                    log.warn("Column mapping failed", e);
                }
            }

            if (success) {
                if (validator != null) {
                    Set<ConstraintViolation<T>> violations = validator.validate(currentInstance);
                    if (!violations.isEmpty()) {
                        messages = new ArrayList<>();
                        success = false;
                        violations.stream().map(ConstraintViolation::getMessage).forEach(messages::add);
                    }
                } else {
                    log.warn("Validator is not set. Skipping validation.");
                }
            }
            consumer.accept(new ExcelReadResult<>(currentInstance, success, messages));
        }

        /**
         * Called for each cell in the current row.
         */
        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            currentRow.add(new ExcelCellData(currentRow.size(), formattedValue));
        }

    }
}
