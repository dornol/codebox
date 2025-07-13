package dev.dornol.codebox.exceldownload.module.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

import static dev.dornol.codebox.exceldownload.module.excel.ExcelDataFormat.*;

final class ExcelColumn<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelColumn.class);
    private static final int MAX_COLUMN_WIDTH = 255 * 256;
    private final String name;
    private final ExcelRowFunction<T, Object> function;
    private final CellStyle style;
    private final ExcelColumnSetter columnSetter;
    private int columnWidth = 1;

    /**
     * Constructs an ExcelColumn with the specified name, data extraction function, cell style, and column setter.
     *
     * Initializes the column width based on the logical length of the column name.
     */
    ExcelColumn(String name, ExcelRowFunction<T, Object> function, CellStyle style, ExcelColumnSetter columnSetter) {
        this.name = name;
        this.function = function;
        this.style = style;
        this.columnSetter = columnSetter;
        this.columnWidth = getLogicalLength(name);
    }

    /**
     * Applies the column's data extraction function to the given row data and cursor.
     *
     * If an exception occurs during function execution, logs the error and returns {@code null}.
     *
     * @param rowData the data object representing the current row
     * @param cursor  the Excel cursor providing context for the operation
     * @return the extracted cell value, or {@code null} if an error occurs
     */
    Object applyFunction(T rowData, ExcelCursor cursor) {
        try {
            return function.apply(rowData, cursor);
        } catch (Exception e) {
            log.error("applyFunction exception caught : {}, {} \n", rowData, cursor, e);
            return null;
        }
    }

    /**
     * Sets the width of the column to the specified value.
     *
     * @param columnWidth the desired width of the column
     */
    void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    /**
     * Adjusts the column width to fit the logical length of the given value if it exceeds the current width.
     *
     * @param value the value whose string representation is used to determine the required column width
     */
    void fitColumnWidthByValue(Object value) {
        int width = getLogicalLength(String.valueOf(value));
        this.setColumnWidth(Math.max(this.columnWidth, width));
    }

    /**
     * Sets the value of the given Excel cell using the configured column setter, falling back to the string representation if an error occurs.
     *
     * @param cell        the Excel cell to set the value for
     * @param columnData  the data to be set in the cell; if {@code null}, an empty string is set
     */
    void setColumnData(SXSSFCell cell, Object columnData) {
        if (columnData == null) {
            cell.setCellValue("");
            return;
        }
        try {
            this.columnSetter.set(cell, columnData);
        } catch (Exception e) {
            log.warn("cast error: {}", e.getMessage());
            cell.setCellValue(String.valueOf(columnData));
        }
    }

    /**
     * Calculates the logical width for a column based on the input string, where ASCII characters count as 1 and non-ASCII characters as 2, then converts this length to a column width value capped by the maximum allowed width.
     *
     * @param input the string to measure for column width calculation
     * @return the calculated column width, scaled and capped by MAX_COLUMN_WIDTH
     */
    private int getLogicalLength(String input) {
        int logicalLength = 0;
        for (char ch : input.toCharArray()) {
            logicalLength += (ch <= 0x7F) ? 1 : 2; // ASCII: 1, 한글 등: 2
        }
        return Math.min(MAX_COLUMN_WIDTH, logicalLength * 250 + 1024);
    }

    /**
     * Returns the header name of the column.
     *
     * @return the column's header name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the cell style applied to this Excel column.
     *
     * @return the {@link CellStyle} used for formatting cells in this column
     */
    public CellStyle getStyle() {
        return style;
    }

    /**
     * Returns the current width of the column.
     *
     * @return the column width in units determined by logical string length, capped by the maximum allowed width
     */
    public int getColumnWidth() {
        return columnWidth;
    }

    public static class ExcelColumnBuilder<T> {
        private final ExcelHandler<T> handler;
        private final String name;
        private final ExcelRowFunction<T, Object> function;
        private ExcelDataType dataType;
        private String dataFormat;
        private HorizontalAlignment alignment = HorizontalAlignment.CENTER;
        private CellStyle style;
        private ExcelColumnSetter columnSetter;

        /**
         * Initializes a new builder for configuring and creating an Excel column with the specified name and data extraction function.
         *
         * @param name the header name of the column
         * @param function the function used to extract cell data from a row object
         */
        public ExcelColumnBuilder(ExcelHandler<T> handler, String name, ExcelRowFunction<T, Object> function) {
            this.handler = handler;
            this.name = name;
            this.function = function;
        }

        /**
         * Sets the data type for the column and configures the cell value setter and default data format accordingly.
         *
         * If no custom data format is set, assigns a default format based on the specified data type.
         * The column setter is updated to ensure cell values are written in the correct format for the chosen data type.
         *
         * @param dataType the data type to use for this column
         * @return this builder instance for method chaining
         */
        public ExcelColumnBuilder<T> type(ExcelDataType dataType) {
            this.dataType = dataType;
            if (dataFormat == null) {
                if (dataType.equals(ExcelDataType.LONG) || dataType.equals(ExcelDataType.INTEGER)) {
                    this.format(NUMBER.getFormat());
                } else if (dataType.equals(ExcelDataType.DOUBLE_PERCENT) || dataType.equals(ExcelDataType.FLOAT_PERCENT)) {
                    this.format(PERCENT.getFormat());
                } else if (dataType.equals(ExcelDataType.DATE)) {
                    this.format(DATE.getFormat());
                } else if (dataType.equals(ExcelDataType.DATETIME)) {
                    this.format(DATETIME.getFormat());
                } else if (dataType.equals(ExcelDataType.TIME)) {
                    this.format(TIME.getFormat());
                }
            }

            columnSetter = switch (this.dataType) {
                case LONG -> (cell, value) -> cell.setCellValue((Long) value);
                case INTEGER -> (cell, value) -> cell.setCellValue((Integer) value);
                case DOUBLE, DOUBLE_PERCENT -> (cell, value) -> cell.setCellValue((Double) value);
                case FLOAT, FLOAT_PERCENT -> (cell, value) -> cell.setCellValue((Float) value);
                case DATETIME, TIME -> (cell, value) -> cell.setCellValue((LocalDateTime) value);
                case DATE -> (cell, value) -> cell.setCellValue((LocalDate) value);
                case BIG_DECIMAL_TO_DOUBLE -> (cell, value) -> cell.setCellValue(((BigDecimal) value).doubleValue());
                case BIG_DECIMAL_TO_LONG -> (cell, value) -> cell.setCellValue(((BigDecimal) value).longValue());
                default -> (cell, value) -> cell.setCellValue(String.valueOf(value));
            };
            return this;
        }

        /**
         * Sets a custom data format pattern for the column.
         *
         * @param dataFormat the data format string to apply to the column's cells
         * @return this builder instance for method chaining
         */
        public ExcelColumnBuilder<T> format(String dataFormat) {
            this.dataFormat = dataFormat;
            return this;
        }

        /**
         * Sets the horizontal alignment for the column and returns the builder for chaining.
         *
         * @param alignment the horizontal alignment to apply to the column cells
         * @return this builder instance for method chaining
         */
        public ExcelColumnBuilder<T> alignment(HorizontalAlignment alignment) {
            this.alignment = alignment;
            return this;
        }

        /**
         * Sets the cell style for the column and returns the builder for chaining.
         *
         * @param style the cell style to apply to the column
         * @return this builder instance for method chaining
         */
        public ExcelColumnBuilder<T> style(CellStyle style) {
            this.style = style;
            return this;
        }

        /**
         * Constructs an {@link ExcelColumn} instance using the current builder configuration.
         * <p>
         * If no data type is specified, defaults to {@code STRING}. If no cell style is set, creates a style
         * using the handler's workbook, the specified alignment, and data format.
         *
         * @return a new {@link ExcelColumn} configured with the builder's settings
         */
        private ExcelColumn<T> build() {
            if (this.dataType == null) {
                this.type(ExcelDataType.STRING);
            }
            if (this.style == null) {
                this.style(ExcelStyleSupporter.cellStyle(handler.getWb(), this.alignment, this.dataFormat));
            }
            return new ExcelColumn<>(this.name, this.function, this.style, this.columnSetter);
        }

        /**
         * Adds the current column to the handler and returns a new builder for the next column with the specified name and data extraction function.
         *
         * @param name the name of the new column
         * @param function the function to extract data for the new column
         * @return a new builder instance for chaining additional column definitions
         */
        public ExcelColumnBuilder<T> column(String name, ExcelRowFunction<T, Object> function) {
            this.handler.addColumn(this.build());
            return new ExcelColumnBuilder<>(handler, name, function);
        }

        /**
         * Adds the current column to the handler and returns a new builder for the next column using a simple data extraction function.
         *
         * @param name the name of the new column
         * @param function a function to extract the column value from a row object
         * @return a new ExcelColumnBuilder for chaining additional column definitions
         */
        public ExcelColumnBuilder<T> column(String name, Function<T, Object> function) {
            this.handler.addColumn(this.build());
            return new ExcelColumnBuilder<>(handler, name, (r, c) -> function.apply(r));
        }

        /**
         * Adds the current column to the handler and starts building a new column with a constant value for all rows.
         *
         * @param name  the name of the new constant column
         * @param value the constant value to be used for every row in the new column
         * @return a new builder for the constant column
         */
        public ExcelColumnBuilder<T> constColumn(String name, Object value) {
            this.handler.addColumn(this.build());
            return new ExcelColumnBuilder<>(handler, name, (r, c) -> value);
        }

        /**
         * Sets the maximum number of rows allowed per sheet in the Excel export.
         *
         * @param maxRowsOfSheet the maximum number of rows for each sheet
         * @return this builder instance for method chaining
         */
        public ExcelColumnBuilder<T> maxRowsOfSheet(int maxRowsOfSheet) {
            this.handler.setMaxRowsOfSheet(maxRowsOfSheet);
            return this;
        }

        /**
         * Adds the current column to the handler and writes the provided data stream to Excel using the specified consumer.
         *
         * @param stream   the stream of data rows to write
         * @param consumer a consumer for processing each row during export
         * @return the ExcelHandler managing the export operation
         */
        public ExcelHandler<T> write(Stream<T> stream, ExcelConsumer<T> consumer) {
            this.handler.addColumn(this.build());
            return this.handler.write(stream, consumer);
        }

        /**
         * Adds the current column to the handler and writes the provided data stream to the Excel output.
         *
         * @param stream the stream of row data to be written to the Excel file
         * @return the ExcelHandler instance managing the export process
         */
        public ExcelHandler<T> write(Stream<T> stream) {
            this.handler.addColumn(this.build());
            return this.handler.write(stream);
        }

    }

}
