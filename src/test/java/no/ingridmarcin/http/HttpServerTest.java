package no.ingridmarcin.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpServer(0);
        server.startServer();
    }

    @Test
    void shouldReturn200() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo");
        HttpClientResponse response = client.executeRequest("GET");
        server.setFileLocation("src/main/resources");
        System.out.println(server.getPort());
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    void shouldParseMultipleParameters() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?content-type=text/html&body=foobar");
        HttpClientResponse response = client.executeRequest("GET");
        assertThat(response.getHeader("Content-Type")).isEqualTo("text/html");
        assertThat(response.getBody()).isEqualTo("foobar");
    }

    @Test
    void shouldParsePostParameters() throws IOException {
        String formBody = "content-type=text/html&body=foobar";
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo");
        client.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setBody(formBody);
        HttpClientResponse response = client.executeRequest("POST");
        assertThat(response.getHeader("Content-Type")).isEqualTo("text/html");
        assertThat(response.getBody()).isEqualTo("foobar");
    }

    @Test
    void shouldReturnFileFromDisk() throws IOException {
        server.setFileLocation("target");
        String fileContent = "Hello Kristiania";
        Files.writeString(Paths.get("target","somefile.txt"), fileContent);
        HttpClient client = new HttpClient("localhost", server.getPort(), "/somefile.txt");
        HttpClientResponse response = client.executeRequest("GET");
        assertEquals("Hello Kristiania", response.getBody());
    }

}

