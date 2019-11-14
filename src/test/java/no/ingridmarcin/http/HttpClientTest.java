package no.ingridmarcin.http;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {

    @Test
    void shouldReturnStatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com","/echo");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200, response.getStatusCode());

    }


    @Test
    void shouldReadFailureStatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", "/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(401, response.getStatusCode());
    }
}
