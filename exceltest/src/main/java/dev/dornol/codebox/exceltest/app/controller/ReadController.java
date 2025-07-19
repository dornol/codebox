package dev.dornol.codebox.exceltest.app.controller;

import dev.dornol.codebox.exceltest.app.service.BookService;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.util.IOUtils;
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
            ZipSecureFile.setMaxFileCount(10000000); // 적절히 넉넉하게 조정
            IOUtils.setByteArrayMaxOverride(2_000_000_000);

            bookService.readExcel(inputStream);
        }
        return "redirect:/";
    }

}
