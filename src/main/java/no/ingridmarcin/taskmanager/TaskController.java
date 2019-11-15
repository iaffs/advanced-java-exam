package no.ingridmarcin.taskmanager;

import no.ingridmarcin.http.HttpController;
import no.ingridmarcin.http.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskController implements HttpController {

    private TasksDao tasksDao;

    public TaskController(TasksDao tasksDao) {
        this.tasksDao = tasksDao;
    }

    @Override
    public void handle(
            String requestAction, String path, Map<String, String> queryParameters, String requestBody, OutputStream outputStream)
            throws IOException {
        try {
            if (requestAction.equals("POST")) {
                queryParameters = HttpServer.parseQueryString(requestBody);
                Task task = new Task();
                task.setTaskName(queryParameters.get("name"));
                tasksDao.insert(task);
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080/createTask.html\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
            } else {
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
            return tasksDao.listAll().stream()
                    .map(p -> String.format("<option id='%s'>%s. %s</option>", p.getId(), p.getId(), p.getTaskName()))
                    .collect(Collectors.joining(""));

    }
}
