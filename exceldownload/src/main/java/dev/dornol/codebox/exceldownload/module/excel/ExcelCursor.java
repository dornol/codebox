package dev.dornol.codebox.exceldownload.module.excel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    void plusRow() {
        this.rowOfSheet++;
    }

    void initRow() {
        this.rowOfSheet = 0;
    }

    void plusTotal() {
        this.currentTotal++;
    }

    public int getRowOfSheet() {
        return rowOfSheet;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getCurrentTotal() {
        return currentTotal;
    }

    public Map<String, String> getStringBox() {
        return stringBox;
    }

    public Map<String, Long> getLongBox() {
        return longBox;
    }

    public Map<String, Integer> getIntegerBox() {
        return integerBox;
    }

    public Map<String, Double> getDoubleBox() {
        return doubleBox;
    }

    public Map<String, Float> getFloatBox() {
        return floatBox;
    }

    public Map<String, LocalDateTime> getDatetimeBox() {
        return datetimeBox;
    }

    public Map<String, LocalDate> getDateBox() {
        return dateBox;
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
