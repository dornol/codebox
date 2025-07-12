package dev.dornol.codebox.exceldownload.app.controller;

import dev.dornol.codebox.exceldownload.app.service.BookService;
import dev.dornol.codebox.exceldownload.app.util.CsvDownloadUtil;
import dev.dornol.codebox.exceldownload.app.util.ExcelDownloadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebController {
    private final BookService bookService;

    @GetMapping("/download-excel")
    public ResponseEntity<StreamingResponseBody> downloadExcel(
            @RequestParam(required = false, defaultValue = "1234") String password) {
        String filename = "book_list";
        var handler = bookService.getExcelHandler();
        return ExcelDownloadUtil.builder(filename)
                .body(outputStream -> handler.consumeOutputStreamWithPassword(outputStream, password));
    }

    @GetMapping("/download-csv")
    public ResponseEntity<StreamingResponseBody> downloadCsv() {
        String filename = "book_list";
        var handler = bookService.getCsvHandler();
        return CsvDownloadUtil.builder(filename).body(handler::consumeOutputStream);
    }

    @ResponseBody
    @GetMapping("/memory")
    public String memory() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return String.format("Memory used: %d MB", usedMemory / 1024 / 1024);
    }

}
