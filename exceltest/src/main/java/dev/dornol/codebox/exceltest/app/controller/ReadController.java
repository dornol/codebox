package dev.dornol.codebox.exceltest.app.controller;

import dev.dornol.codebox.exceltest.app.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class ReadController {
    private final BookService bookService;

    public ReadController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/read")
    public String readExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            bookService.readExcel(inputStream);
        }
        return "redirect:/";
    }

    @PostMapping("/read-and-save")
    public String readAndSaveExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            bookService.readAndSaveExcel(inputStream);
        }
        return "redirect:/";
    }

}
