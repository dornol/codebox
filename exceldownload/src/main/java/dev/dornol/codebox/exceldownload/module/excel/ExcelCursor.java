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

    /**
     * Initializes a new ExcelCursor with the specified number of columns, setting the row index and total count to zero.
     *
     * @param columnCount the total number of columns in the Excel sheet context
     */
    ExcelCursor(int columnCount) {
        this.rowOfSheet = 0;
        this.currentTotal = 0;
        this.columnCount = columnCount;
    }

    /**
     * Increments the current row index by one.
     */
    void plusRow() {
        this.rowOfSheet++;
    }

    /**
     * Resets the current row index to zero.
     */
    void initRow() {
        this.rowOfSheet = 0;
    }

    /**
     * Increments the running total counter by one.
     */
    void plusTotal() {
        this.currentTotal++;
    }

    /**
     * Returns the current row index within the Excel sheet.
     *
     * @return the current row index
     */
    public int getRowOfSheet() {
        return rowOfSheet;
    }

    /**
     * Returns the total number of columns in the Excel sheet.
     *
     * @return the column count
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Returns the current running total value tracked by this cursor.
     *
     * @return the current total
     */
    public int getCurrentTotal() {
        return currentTotal;
    }

    /**
     * Returns the map storing string values associated with their keys for the current Excel context.
     *
     * @return a map of string values keyed by name
     */
    public Map<String, String> getStringBox() {
        return stringBox;
    }

    /**
     * Returns the map storing long values associated with string keys for the current Excel context.
     *
     * @return a map of string keys to long values
     */
    public Map<String, Long> getLongBox() {
        return longBox;
    }

    /**
     * Returns the map storing integer values associated with string keys for the current Excel context.
     *
     * @return the map of string keys to integer values
     */
    public Map<String, Integer> getIntegerBox() {
        return integerBox;
    }

    /**
     * Returns the map storing double values associated with string keys for the current Excel context.
     *
     * @return a map of string keys to double values
     */
    public Map<String, Double> getDoubleBox() {
        return doubleBox;
    }

    /**
     * Returns the map storing float values associated with string keys for the current Excel context.
     *
     * @return a map of string keys to float values
     */
    public Map<String, Float> getFloatBox() {
        return floatBox;
    }

    /**
     * Returns the map storing LocalDateTime values associated with string keys.
     *
     * @return a map of string keys to LocalDateTime values
     */
    public Map<String, LocalDateTime> getDatetimeBox() {
        return datetimeBox;
    }

    /**
     * Returns the map storing LocalDate values keyed by string identifiers.
     *
     * @return a map of string keys to LocalDate values
     */
    public Map<String, LocalDate> getDateBox() {
        return dateBox;
    }

    /**
     * Returns a string representation of the ExcelCursor, including the current row, column count, and total.
     *
     * @return a string summarizing the cursor's state
     */
    @Override
    public String toString() {
        return "ExcelCursor{" +
                "rowOfSheet=" + rowOfSheet +
                ", columnCount=" + columnCount +
                ", currentTotal=" + currentTotal +
                '}';
    }
}
