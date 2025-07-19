package dev.dornol.codebox.exceltest.app.controller;

import dev.dornol.codebox.exceltest.app.dto.TypeTestDto;
import dev.dornol.codebox.exceltest.app.excel.TypeTestExcelMapper;
import dev.dornol.codebox.exceltest.app.service.BookService;
import dev.dornol.codebox.exceltest.app.util.DownloadFileType;
import dev.dornol.codebox.exceltest.app.util.DownloadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.stream.Stream;

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

    @GetMapping("/download-excel-types")
    public ResponseEntity<StreamingResponseBody> downloadExcelTypes() {
        String filename = "type test excel";
        var handler = TypeTestExcelMapper.getHandler(Stream.generate(TypeTestDto::rand).limit(100000));
        return DownloadUtil.builder(filename, DownloadFileType.EXCEL)
                .body(handler::consumeOutputStream);
    }

    @ResponseBody
    @GetMapping("/memory")
    public String memory() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return String.format("Memory used: %d MB", usedMemory / 1024 / 1024);
    }

}
