package dev.dornol.codebox.exceltest.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TypeTestReadDto {
    private Long no;
    private String aString;
    private Long aLong;
    private Integer anInteger;
    private LocalDateTime aLocalDateTime;
    private LocalDate aLocalDate;
    private LocalTime aLocalTime;
    private Double aDouble;
    private Float aFloat;
    private Boolean aBoolean;
    private BigDecimal aLongBigDecimal;
    private BigDecimal aDoubleBigDecimal;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getaString() {
        return aString;
    }

    public Long getaLong() {
        return aLong;
    }

    public Integer getAnInteger() {
        return anInteger;
    }

    public LocalDateTime getaLocalDateTime() {
        return aLocalDateTime;
    }

    public LocalDate getaLocalDate() {
        return aLocalDate;
    }

    public LocalTime getaLocalTime() {
        return aLocalTime;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public BigDecimal getaLongBigDecimal() {
        return aLongBigDecimal;
    }

    public BigDecimal getaDoubleBigDecimal() {
        return aDoubleBigDecimal;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    public void setAnInteger(Integer anInteger) {
        this.anInteger = anInteger;
    }

    public void setaLocalDateTime(LocalDateTime aLocalDateTime) {
        this.aLocalDateTime = aLocalDateTime;
    }

    public void setaLocalDate(LocalDate aLocalDate) {
        this.aLocalDate = aLocalDate;
    }

    public void setaLocalTime(LocalTime aLocalTime) {
        this.aLocalTime = aLocalTime;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public void setaLongBigDecimal(BigDecimal aLongBigDecimal) {
        this.aLongBigDecimal = aLongBigDecimal;
    }

    public void setaDoubleBigDecimal(BigDecimal aDoubleBigDecimal) {
        this.aDoubleBigDecimal = aDoubleBigDecimal;
    }

    @Override
    public String toString() {
        return "TypeTestReadDto{" +
                "no=" + no +
                ", aString='" + aString + '\'' +
                ", aLong=" + aLong +
                ", anInteger=" + anInteger +
                ", aLocalDateTime=" + aLocalDateTime +
                ", aLocalDate=" + aLocalDate +
                ", aLocalTime=" + aLocalTime +
                ", aDouble=" + aDouble +
                ", aFloat=" + aFloat +
                ", aBoolean=" + aBoolean +
                ", aLongBigDecimal=" + aLongBigDecimal +
                ", aDoubleBigDecimal=" + aDoubleBigDecimal +
                '}';
    }
}
