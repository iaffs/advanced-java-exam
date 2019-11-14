package no.ingridmarcin.http;


import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class FileHttpController implements HttpController {
    private HttpServer httpServer;

    public FileHttpController(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(FileHttpController.class);

    @Override
    public void handle(String requestPath, String path, Map<String, String> queryParameters, String body, OutputStream outputStream) throws IOException {
        File file = new File(httpServer.getFileLocation() + path);
        Logger.debug("Requesting file {}", file);
        if (file.isDirectory()) {
            file = new File(file, "index.html");
        }
        if (file.exists()) {
            long length = file.length();
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Length: " + length + "\r\n" +
                    "Connenction: close\r\n" +
                    "\r\n").getBytes());
            // loading body to the http package under the header
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                fileInputStream.transferTo(outputStream);
            }
        } else {
            outputStream.write(("HTTP/1.1 404 Not found\r\n" +
                    "Content-Length: 9\r\n" +
                    "Connenction: close\r\n" +
                    "\r\n" +
                    "Not found").getBytes());
        }
    }

}
