package dev.dornol.codebox.exceldownload.app.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public enum DownloadFileType {
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    CSV("text/csv; charset=UTF-8", "csv"),
    ;
    private final String contentType;
    private final String extension;

    /**
     * Constructs a DownloadFileType enum constant with the specified MIME content type and file extension.
     *
     * @param contentType the MIME type associated with the file type
     * @param extension the file extension for the file type
     */
    DownloadFileType(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    /**
     * Returns the MIME content type string associated with this file type.
     *
     * @return the MIME content type (e.g., "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" or "text/csv; charset=UTF-8")
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Returns the file extension associated with this file type.
     *
     * @return the file extension string (e.g., "xlsx" or "csv")
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Generates a Content-Disposition header value for HTTP file downloads using the provided filename and the file type's extension.
     *
     * The header includes both an ASCII-only fallback filename and a UTF-8 percent-encoded filename according to RFC 5987 for broad client compatibility.
     *
     * @param filename The base filename (without extension) to use for the downloaded file.
     * @return A Content-Disposition header value suitable for HTTP responses to prompt file download.
     */
    public String getContentDisposition(String filename) {
        String csvFilename = filename + "." + extension;

        // ASCII fallback (따옴표 안에 ASCII 문자만)
        String fallback = csvFilename.replaceAll("[^\\x20-\\x7E]", "_");

        // RFC 5987 인코딩 (UTF-8 + percent encoding)
        String encoded;
        try {
            encoded = URLEncoder.encode(csvFilename, StandardCharsets.UTF_8)
                    .replace("+", "%20")
                    .replace("%2B", "+"); // 선택적으로 '+' 복원
        } catch (Exception e) {
            encoded = fallback;
        }

        return String.format(Locale.ROOT,
                "attachment; filename=\"%s\"; filename*=UTF-8''%s", fallback, encoded);
    }
}
