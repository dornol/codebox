package dev.dornol.codebox.exceldownload.module.csv;

public final class CsvCursor {
    private int rowOfSheet;
    private final int columnCount;
    private int currentTotal;

    /**
     * Constructs a CsvCursor with the specified number of columns.
     *
     * @param columnCount the fixed number of columns for the CSV context
     */
    public CsvCursor(int columnCount) {
        this.columnCount = columnCount;
    }

    /**
     * Returns the fixed number of columns managed by this cursor.
     *
     * @return the column count
     */
    public int getColumnCount() {
        return columnCount;
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
     * Increments the cumulative total count by one.
     */
    void plusTotal() {
        this.currentTotal++;
    }

    /**
     * Returns the current row index within the CSV sheet.
     *
     * @return the current row index
     */
    public int getRowOfSheet() {
        return rowOfSheet;
    }

    /**
     * Returns the current cumulative total tracked by the cursor.
     *
     * @return the current total count
     */
    public int getCurrentTotal() {
        return currentTotal;
    }
}
