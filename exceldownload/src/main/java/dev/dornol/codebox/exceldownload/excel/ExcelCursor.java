package dev.dornol.codebox.exceldownload.excel;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class ExcelCursor {

    private int rowOfSheet;
    private final int columnCount;
    private int currentTotal;
    private final Map<String, String> stringBox = new HashMap<>();
    private final Map<String, Long> longBox = new HashMap<>();
    private final Map<String, Integer> integerBox = new HashMap<>();
    private final Map<String, Double> doubleBox = new HashMap<>();
    private final Map<String, Float> floatBox = new HashMap<>();
    private final Map<String, LocalDateTime> datetimeBox = new HashMap<>();
    private final Map<String, LocalDate> dateBox = new HashMap<>();

    ExcelCursor(int columnCount) {
        this.rowOfSheet = 0;
        this.currentTotal = 0;
        this.columnCount = columnCount;
    }

    void setRowOfSheet(int rowOfSheet) {
        this.rowOfSheet = rowOfSheet;
    }

    void plusRow() {
        this.rowOfSheet++;
    }

    void initRow() {
        this.rowOfSheet = 0;
    }

    void plusTotal() {
        this.currentTotal++;
    }


    @Override
    public String toString() {
        return "ExcelCursor{" +
                "rowOfSheet=" + rowOfSheet +
                ", columnCount=" + columnCount +
                ", currentTotal=" + currentTotal +
                '}';
    }
}
