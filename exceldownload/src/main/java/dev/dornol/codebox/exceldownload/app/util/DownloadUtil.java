package dev.dornol.codebox.exceldownload.app.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public final class DownloadUtil {

    /**
     * Prevents instantiation of the {@code DownloadUtil} utility class.
     */
    private DownloadUtil() {
        /* empty */
    }

    /**
     * Creates a ResponseEntity.BodyBuilder pre-configured with headers for downloading a file.
     *
     * The builder sets the HTTP status to 200 OK and includes headers for content disposition, content type, and cache control.
     *
     * @param filename the name of the file to be downloaded
     * @param type the file type, used to determine content disposition and content type headers
     * @return a ResponseEntity.BodyBuilder with appropriate headers for file download
     */
    public static ResponseEntity.BodyBuilder builder(String filename, DownloadFileType type) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, type.getContentDisposition(filename))
                .header(HttpHeaders.CONTENT_TYPE, type.getContentType())
                .header(HttpHeaders.CACHE_CONTROL, "max-age=10");
    }

}
