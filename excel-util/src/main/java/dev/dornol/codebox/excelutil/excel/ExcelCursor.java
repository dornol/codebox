package dev.dornol.codebox.excelutil.excel;

public class ExcelCursor {
    private int rowOfSheet;
    private int currentTotal;

    ExcelCursor() {
        this.rowOfSheet = 0;
        this.currentTotal = 0;
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
