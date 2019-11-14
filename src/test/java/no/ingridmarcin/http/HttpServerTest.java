package no.ingridmarcin.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    @Test
    void shouldReturnRequestedErrorCode() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost",port, "/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(401, response.getStatusCode());
    }

    @Test
    void shouldReturnContentLength() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost",port, "/echo?body=123456");
        HttpClientResponse response = client.executeRequest();
        assertEquals(6, response.getContentLength());
    }

    @Test
    void shouldReturnRequestBody() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost",port, "/echo?body=HelloWorld!");
        HttpClientResponse response = client.executeRequest();
        assertEquals("HelloWorld!", response.getBody());
    }

    private int startServer() throws IOException {
        HttpServer httpServer = new HttpServer(0);

        new Thread(() -> {
            try {
                httpServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return httpServer.getActualPort();
    }

}

