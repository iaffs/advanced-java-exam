package no.ingridmarcin.taskmanager;

import no.ingridmarcin.http.HttpController;
import no.ingridmarcin.http.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class StatusController implements HttpController {
    private StatusDao statusDao;

    public StatusController(StatusDao statusDao) {
        this.statusDao = statusDao;
    }

    @Override
    public void handle(String requestAction, String path, Map<String, String> queryParameters, String requestBody, OutputStream outputStream) throws IOException {

        try {
            if(requestAction.equals("POST")) {
                queryParameters = HttpServer.parseQueryString(requestBody);
                Status status = new Status();
                status.setStatusName(queryParameters.get("statusName"));
                statusDao.insert(status);
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080/createStatus.html\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
            }
            else {
                String status = "200";
                String body = getBody();
                outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        body).getBytes());
                outputStream.flush();
            }
        } catch (SQLException e) {
            String message = e.toString();
            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes());
        }
    }

    public String getBody() throws SQLException {
        return statusDao.listAll().stream()
                .map(p -> String.format("<option id='%s'>%s</option>", p.getId(), p.getStatusName()))
                .collect(Collectors.joining(""));
    }
}
