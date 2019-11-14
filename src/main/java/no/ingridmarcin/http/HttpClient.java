package no.ingridmarcin.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClient {
    private String host;
    private String requestTarget;
    private int port;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public HttpClient(String host, int port, String requestTarget) {
        this.host = host;
        this.requestTarget = requestTarget;
        this.port = port;
        setRequestHeader("Host", host);
        setRequestHeader("Connection", "close");
    }

    // this is the code where client is writing request to the server. Only client can start a communication in
    // client-server architecture
    public HttpClientResponse executeRequest(final String httpMethod) throws IOException {
        try(Socket socket = new Socket(host, port)) {
            if(body != null) {
                setRequestHeader("Content-Length",String.valueOf(body.length()));
            }

            String headerString = headers.entrySet().stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining("\r\n"));

            socket.getOutputStream().write((httpMethod + " " + requestTarget + " HTTP/1.1 \r\n" +
                    headerString +
                    "\r\n\r\n" + body).getBytes());

            return new HttpClientResponse(socket.getInputStream());
        }
    }

    public void setRequestHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public void setBody(String body) {
        this.body = body;
    }

}