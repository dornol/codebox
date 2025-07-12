package dev.dornol.codebox.exceldownload.excel;

public final class CsvCursor {
    private int rowOfSheet;
    private final int columnCount;
    private int currentTotal;

    public CsvCursor(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getColumnCount() {
        return columnCount;
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

    public int getCurrentTotal() {
        return currentTotal;
    }
}
