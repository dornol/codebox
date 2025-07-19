package dev.dornol.codebox.excelutil.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Represents a single cell's value read from an Excel file,
 * along with its column index and string representation.
 * <p>
 * Provides utility methods to convert the cell's content into various Java types.
 *
 * @param columnIndex     The index of the column this cell belongs to (0-based)
 * @param formattedValue  The cell's formatted string value
 *
 * @author dhkim
 * @since 2025-07-19
 */
public record ExcelCellData(int columnIndex, String formattedValue) {
    private static final Logger log = LoggerFactory.getLogger(ExcelCellData.class);

    /**
     * Converts the cell's value to a {@link Long}.
     * <p>
     * Uses {@link NumberFormat} with {@code Locale.KOREA} to support comma-separated numbers.
     *
     * @return The value as a Long
     * @throws IllegalArgumentException if the value cannot be parsed as a number
     */
    public Long asLong() {
        try {
            Number n = NumberFormat.getNumberInstance(Locale.KOREA)
                    .parse(formattedValue.replace("\u00A0", " ")); // nbsp 제거
            return n.longValue();
        } catch (ParseException e) {
            log.warn("숫자 변환 실패: {}", formattedValue);
            throw new IllegalArgumentException("숫자 변환 실패: " + formattedValue, e);
        }
    }

    /**
     * Converts the cell's value to an {@link Integer}.
     *
     * @return The value as an Integer
     */
    public Integer asInt() {
        return asLong().intValue();
    }

    /**
     * Returns the cell's value as a plain string.
     *
     * @return The raw formatted string value
     */
    public String asString() {
        return formattedValue;
    }

    /**
     * Converts the cell's value to a {@link Boolean}.
     * <p>
     * Accepts "true", "1" (case-insensitive) as true.
     *
     * @return true if the value is "true" or "1", false otherwise
     */
    public Boolean asBoolean() {
        return "true".equalsIgnoreCase(formattedValue) || "1".equals(formattedValue);
    }
}
