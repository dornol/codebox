package dev.dornol.codebox.exceldownload.app.excel;

import dev.dornol.codebox.exceldownload.app.dto.TypeTestDto;
import dev.dornol.codebox.exceldownload.module.excel.ExcelDataFormat;
import dev.dornol.codebox.exceldownload.module.excel.ExcelDataType;
import dev.dornol.codebox.exceldownload.module.excel.ExcelHandler;
import dev.dornol.codebox.exceldownload.module.excel.ExcelWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class TypeTestExcelMapper {
    private TypeTestExcelMapper() {
        /* empty */
    }

    public static ExcelHandler getHandler(Stream<TypeTestDto> stream) {
        var date = LocalDate.now();
        return new ExcelWriter<TypeTestDto>(1000)
                .column("string", TypeTestDto::aString)
                .column("long", TypeTestDto::aLong).type(ExcelDataType.LONG)
                .column("integer", TypeTestDto::anInteger).type(ExcelDataType.INTEGER)
                .column("localdatetime", TypeTestDto::aLocalDateTime).type(ExcelDataType.DATETIME)
                .column("localdate", TypeTestDto::aLocalDate).type(ExcelDataType.DATE)
                .column("localtime", r -> LocalDateTime.of(date, r.aLocalTime())).type(ExcelDataType.TIME)
                .column("double", TypeTestDto::aDouble).type(ExcelDataType.DOUBLE)
                .column("double_percent", TypeTestDto::aDouble).type(ExcelDataType.DOUBLE_PERCENT)
                .column("float", TypeTestDto::aFloat).type(ExcelDataType.FLOAT)
                .column("float_percent", TypeTestDto::aFloat).type(ExcelDataType.FLOAT_PERCENT)
                .column("boolean", TypeTestDto::aBoolean).type(ExcelDataType.BOOLEAN_TO_YN)
                .column("bigdecimal_long", TypeTestDto::aLongBigDecimal).type(ExcelDataType.BIG_DECIMAL_TO_LONG)
                .column("bigdecimal_double", TypeTestDto::aDoubleBigDecimal).type(ExcelDataType.BIG_DECIMAL_TO_DOUBLE)
                .column("currency", TypeTestDto::aDoubleBigDecimal).type(ExcelDataType.BIG_DECIMAL_TO_DOUBLE).format(ExcelDataFormat.CURRENCY_KRW.getFormat())
                .write(stream);
    }
}
