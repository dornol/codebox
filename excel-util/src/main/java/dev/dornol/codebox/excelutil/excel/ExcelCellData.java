package dev.dornol.codebox.excelutil.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public record ExcelCellData(int columnIndex, String formattedValue) {
    private static final Logger log = LoggerFactory.getLogger(ExcelCellData.class);

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

    public Integer asInt() {
        return asLong().intValue();
    }

    public String asString() {
        return formattedValue;
    }

    public Boolean asBoolean() {
        return "true".equalsIgnoreCase(formattedValue) || "1".equals(formattedValue);
    }
}
