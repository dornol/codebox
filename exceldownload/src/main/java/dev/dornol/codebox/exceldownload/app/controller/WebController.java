package dev.dornol.codebox.exceldownload.app.controller;

import dev.dornol.codebox.exceldownload.app.service.BookService;
import dev.dornol.codebox.exceldownload.app.util.DownloadFileType;
import dev.dornol.codebox.exceldownload.app.util.DownloadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
public class WebController {
    private final BookService bookService;

    public WebController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/download-excel-with-password")
    public ResponseEntity<StreamingResponseBody> downloadExcelWithPassword(
            @RequestParam(required = false, defaultValue = "1234") String password) {
        String filename = "book list excel with password";
        var handler = bookService.getExcelHandler();
        return DownloadUtil.builder(filename, DownloadFileType.EXCEL)
                .body(outputStream -> handler.consumeOutputStreamWithPassword(outputStream, password));
    }

    @GetMapping("/download-excel")
    public ResponseEntity<StreamingResponseBody> downloadExcel() {
        String filename = "book list excel";
        var handler = bookService.getExcelHandler();
        return DownloadUtil.builder(filename, DownloadFileType.EXCEL)
                .body(handler::consumeOutputStream);
    }

    @GetMapping("/download-csv")
    public ResponseEntity<StreamingResponseBody> downloadCsv() {
        String filename = "book list csv";
        var handler = bookService.getCsvHandler();
        return DownloadUtil.builder(filename, DownloadFileType.CSV).body(handler::consumeOutputStream);
    }

    @ResponseBody
    @GetMapping("/memory")
    public String memory() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return String.format("Memory used: %d MB", usedMemory / 1024 / 1024);
    }

}
