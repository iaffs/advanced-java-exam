package no.ingridmarcin.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    @Test
    void shouldReturnRequestedErrorCode() throws IOException {
        HttpClient client = new HttpClient("localhost",8080, "/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200, response.getStatusCode());
    }
}