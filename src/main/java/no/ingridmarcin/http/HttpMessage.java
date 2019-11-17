package no.ingridmarcin.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpMessage {

    static String readLine(InputStream inputStream) throws IOException {
        StringBuilder line = new StringBuilder();
        int c;
        while ((c = inputStream.read()) != -1) {
            if (c == '\r') {
                inputStream.read();
                break;
            }
            line.append((char)c);
        }
        return line.toString();
    }


    static Map<String, String> readHeaders(InputStream inputStream) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while (!(headerLine = readLine(inputStream)).isBlank()) {
            int colonPos = headerLine.indexOf(':');
            headers.put(headerLine.substring(0, colonPos).trim(),
                    headerLine.substring(colonPos + 1).trim());
        }
        return  headers;
    }


    static String readBody(Map<String, String> headers, InputStream inputStream) throws IOException {
        if (headers.containsKey("Content-Length")) {
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < Integer.parseInt(headers.get("Content-Length")); i++) {
                body.append((char) inputStream.read());
            }
            return body.toString();
        } else {
            return null;
        }
    }
}
