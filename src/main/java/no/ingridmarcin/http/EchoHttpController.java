package no.ingridmarcin.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class EchoHttpController implements HttpController {

    @Override
    public void handle(String requestAction, String path, Map<String, String> queryParameters, String body, OutputStream outputStream) throws IOException {
        if (requestAction.equals("POST")) {
            queryParameters = HttpServer.parseQueryString(body);
        }

        String status = queryParameters.getOrDefault("status", "200");
        String location = queryParameters.getOrDefault("Location", null);
        String responseBody = queryParameters.getOrDefault("body", "Hello World");
        int contentLength = responseBody.length();
        outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + contentLength + "\r\n" +
                "Connection: close\r\n" +
                (location != null ? "Location: " + location + "\r\n" : "") +
                "\r\n" +
                responseBody).getBytes());
    }

}
