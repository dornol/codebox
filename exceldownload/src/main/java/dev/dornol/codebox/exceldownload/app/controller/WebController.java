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

    /**
     * Constructs a new WebController with the specified BookService.
     *
     * @param bookService the service used to handle book-related operations
     */
    public WebController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Handles HTTP GET requests to download the book list as a password-protected Excel file.
     *
     * @param password the password to protect the Excel file; defaults to "1234" if not provided
     * @return a response entity containing a streaming response body for the Excel file download
     */
    @GetMapping("/download-excel-with-password")
    public ResponseEntity<StreamingResponseBody> downloadExcelWithPassword(
            @RequestParam(required = false, defaultValue = "1234") String password) {
        String filename = "book list excel with password";
        var handler = bookService.getExcelHandler();
        return DownloadUtil.builder(filename, DownloadFileType.EXCEL)
                .body(outputStream -> handler.consumeOutputStreamWithPassword(outputStream, password));
    }

    /**
     * Handles HTTP GET requests to download the book list as an Excel file without password protection.
     *
     * @return a ResponseEntity containing a streaming response body for the Excel file download
     */
    @GetMapping("/download-excel")
    public ResponseEntity<StreamingResponseBody> downloadExcel() {
        String filename = "book list excel";
        var handler = bookService.getExcelHandler();
        return DownloadUtil.builder(filename, DownloadFileType.EXCEL)
                .body(handler::consumeOutputStream);
    }

    /**
     * Handles HTTP GET requests to stream a CSV file containing book data for download.
     *
     * @return a ResponseEntity that streams the CSV file as the response body
     */
    @GetMapping("/download-csv")
    public ResponseEntity<StreamingResponseBody> downloadCsv() {
        String filename = "book list csv";
        var handler = bookService.getCsvHandler();
        return DownloadUtil.builder(filename, DownloadFileType.CSV).body(handler::consumeOutputStream);
    }

    /**
     * Returns the current JVM memory usage in megabytes as a plain text string.
     *
     * @return a string reporting the amount of memory used by the JVM in MB
     */
    @ResponseBody
    @GetMapping("/memory")
    public String memory() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return String.format("Memory used: %d MB", usedMemory / 1024 / 1024);
    }

}
