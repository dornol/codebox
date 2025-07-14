package dev.dornol.codebox.exceldownload.module.csv;

public final class CsvCursor {
    private int rowOfSheet;
    private int currentTotal;

    public CsvCursor() {
        /* empty */
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
