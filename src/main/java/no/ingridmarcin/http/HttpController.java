package no.ingridmarcin.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpController {
    void handle(
            String requestPath, String path, Map<String, String> queryParameters, String body, OutputStream outputStream
    ) throws IOException;

}
