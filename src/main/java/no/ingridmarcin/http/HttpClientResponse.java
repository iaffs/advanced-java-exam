package no.ingridmarcin.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

class HttpClientResponse extends HttpMessage {
    private String statusLine;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpClientResponse(InputStream inputStream) throws IOException {
        this.statusLine = readLine(inputStream);
        headers = readHeaders(inputStream);

        body = readBody(headers, inputStream);
    }

    public int getStatusCode() {
        return Integer.parseInt(statusLine.split(" ")[1]);
    }

    public Map<String, String> getHeader() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getBody() {
        return body;
    }
}
