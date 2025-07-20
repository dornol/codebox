package dev.dornol.codebox.excelutil.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a single cell's value read from an Excel file,
 * along with its column index and formatted string content.
 * <p>
 * Provides utility methods to convert the cell's value into various Java types,
 * including number, string, boolean, date/time, etc.
 *
 * @param columnIndex    the column index of the cell (0-based)
 * @param formattedValue the formatted string value extracted from Excel
 *
 * @author dhkim
 * @since 2025-07-19
 */
public record ExcelCellData(int columnIndex, String formattedValue) {
    private static final Logger log = LoggerFactory.getLogger(ExcelCellData.class);

    public ExcelCellData {
        if (formattedValue == null) {
            formattedValue = "";
        }
        if (columnIndex < 0) {
            throw new IllegalArgumentException("columnIndex must be non-negative");
        }
    }

    /**
     * Parses the value as a {@link Number} using the given locale.
     * This method removes formatting characters such as commas, currency symbols, and percent signs.
     *
     * @param locale the locale to use for number formatting (e.g. {@code Locale.KOREA})
     * @return parsed number
     * @throws IllegalArgumentException if parsing fails
     */
    public Number asNumber(Locale locale) {
        try {
            // 공백, NBSP 제거 + 특수 문자 제거
            String cleaned = formattedValue
                    .replace("\u00A0", " ")
                    .replaceAll("[$,₩€%원]", "") // $, ₩, €, %, 원 제거
                    .replace(" ", "")         // 일반 공백도 제거
                    .trim();

            return NumberFormat.getNumberInstance(locale).parse(cleaned);
        } catch (ParseException e) {
            log.warn("Failed to parse number (col {}): '{}'", columnIndex, formattedValue);
            throw new IllegalArgumentException("Failed to parse number: " + formattedValue);
        }
    }

    /**
     * Parses the value as a {@link Number} using {@link Locale#KOREA} as default.
     */
    public Number asNumber() {
        return asNumber(Locale.KOREA);
    }

    /**
     * Converts the value to {@link Long}.
     */
    public Long asLong() {
        return asNumber().longValue();
    }

    /**
     * Converts the value to {@link Integer}.
     * Throws if the long value is out of int range.
     */
    public Integer asInt() {
        long longValue = asLong();
        if (longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Value out of range: " + longValue);
        }
        return (int) longValue;
    }

    /**
     * Returns the raw string as-is.
     */
    public String asString() {
        return formattedValue;
    }

    /**
     * Converts the value to {@link Boolean}.
     * Accepts "true", "1", "y", "yes" (case-insensitive) as {@code true}.
     */
    public Boolean asBoolean() {
        String val = formattedValue.trim().toLowerCase();
        return val.equals("true") || val.equals("1") || val.equals("y") || val.equals("yes");
    }

    /**
     * Converts the value to {@link LocalDateTime} using default format "yyyy-MM-dd[ HH:mm[:ss]]".
     * This is useful for common Excel datetime formats.
     */
    public LocalDateTime asLocalDateTime() {
        return asLocalDateTime("yyyy-MM-dd[ HH:mm[:ss]]");
    }

    /**
     * Converts the value to {@link LocalDateTime} using the specified format.
     *
     * @param format the date-time pattern (e.g., "yyyy-MM-dd HH:mm:ss")
     */
    public LocalDateTime asLocalDateTime(String format) {
        return LocalDateTime.parse(formattedValue, DateTimeFormatter.ofPattern(format));
    }

    /**
     * Converts the value to {@link LocalDate} using ISO format (yyyy-MM-dd).
     */
    public LocalDate asLocalDate() {
        return LocalDate.parse(formattedValue);
    }

    /**
     * Converts the value to {@link LocalDate} using the specified format.
     *
     * @param format the date pattern (e.g., "yyyy/MM/dd")
     */
    public LocalDate asLocalDate(String format) {
        return LocalDate.parse(formattedValue, DateTimeFormatter.ofPattern(format));
    }

    /**
     * Converts the value to {@link LocalTime} using ISO format (HH:mm:ss).
     */
    public LocalTime asLocalTime() {
        return LocalTime.parse(formattedValue);
    }

    /**
     * Converts the value to {@link LocalTime} using the specified format.
     *
     * @param format the time pattern (e.g., "HH:mm")
     */
    public LocalTime asLocalTime(String format) {
        return LocalTime.parse(formattedValue, DateTimeFormatter.ofPattern(format));
    }

    /**
     * Converts the value to {@link Double}.
     */
    public Double asDouble() {
        return asNumber().doubleValue();
    }

    /**
     * Converts the value to {@link Float}.
     */
    public Float asFloat() {
        return asNumber().floatValue();
    }

    /**
     * Converts the value to {@link BigDecimal}.
     * Uses the string representation of the parsed number to avoid precision loss.
     */
    public BigDecimal asBigDecimal() {
        return new BigDecimal(asNumber().toString());
    }

}
