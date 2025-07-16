package dev.dornol.codebox.excelutil.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExcelReader<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelReader.class);
    private final List<ExcelReadColumn<T>> columns = new ArrayList<>();
    private final Supplier<T> instanceSupplier;

    public ExcelReader(Supplier<T> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    void addColumn(ExcelReadColumn<T> column) {
        columns.add(column);
    }

    public ExcelReadColumn.ExcelReadColumnBuilder<T> column(BiConsumer<T, Cell> setter) {
        return new ExcelReadColumn.ExcelReadColumnBuilder<>(this, setter);
    }

    public ExcelReadHandler<T> read(InputStream inputStream) {
        Workbook wb;
        try {
            wb = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        Sheet sheet = wb.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        Stream<T> stream = StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(lastRowNum, Spliterator.ORDERED) {
            int current = 1;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (current > lastRowNum) {
                    return false;
                }

                Row row = sheet.getRow(current++);
                if (row == null) {
                    return true;
                }

                T instance = instanceSupplier.get();
                for (int j = 0; j < columns.size(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        columns.get(j).setter().accept(instance, cell);
                    }
                }
                action.accept(instance);
                return true;
            }
        }, false).onClose(() -> {
            try {
                wb.close();
            } catch (IOException e) {
                log.error("Failed to close workbook", e);
            }
        });

        return new ExcelReadHandler<>(stream);
    }

}
