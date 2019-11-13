package no.ingridmarcin.http;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class HttpClientTest {
    @Test
    void shouldReturnStatusCode() {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80,  "/echo");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200,response.getStatusCode());
    }
}
