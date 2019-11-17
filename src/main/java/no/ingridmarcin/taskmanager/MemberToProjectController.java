package no.ingridmarcin.taskmanager;

import no.ingridmarcin.http.HttpController;
import no.ingridmarcin.http.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberToProjectController implements HttpController {

    private MemberToProjectDao memberToProjectDao;

    public MemberToProjectController(MemberToProjectDao memberToProjectDao) {
        this.memberToProjectDao = memberToProjectDao;
    }

    @Override
    public void handle(String requestAction, String path, Map<String, String> queryParameters, String requestBody, OutputStream outputStream) throws IOException {

        try {
            if (requestAction.equals("POST")) {
                queryParameters = HttpServer.parseQueryString(requestBody);
                // kinda risky but lets assume no one uses 'fetch' as a name or email
                if (requestBody.contains("fetch")) {
                    updateStatus(queryParameters);
                } else {
                    executeAssignment(queryParameters);
                }
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080/assignMemberToProjects.html\r\n" +
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

    //insert data into memberToProject table
    private void executeAssignment(Map<String, String> queryParameters) throws SQLException {
        MemberToProject memberToProject = new MemberToProject();

        String projects = java.net.URLDecoder.decode(queryParameters.get("projects"), StandardCharsets.UTF_8);
        String members = java.net.URLDecoder.decode(queryParameters.get("members"), StandardCharsets.UTF_8);
        String tasks = java.net.URLDecoder.decode(queryParameters.get("tasks"), StandardCharsets.UTF_8);
        String status = java.net.URLDecoder.decode(queryParameters.get("status"), StandardCharsets.UTF_8);

        memberToProject.setProjectName(projects);
        memberToProject.setMemberName(members);
        memberToProject.setTaskName(tasks);
        memberToProject.setStatusName(status);
        memberToProjectDao.insert(memberToProject);
        System.out.println();
    }

    //updates status in memberToProject table
    private void updateStatus(Map<String, String> queryParameters) throws SQLException {
        String status = java.net.URLDecoder.decode(queryParameters.get("status"),StandardCharsets.UTF_8);
        String id = queryParameters.get("fetchId");
        long idToLong = Long.parseLong(id);
        memberToProjectDao.update(status, idToLong);
    }

    public String getBody() throws SQLException {
        return memberToProjectDao.listAll().stream()
                .map(p -> String.format("<option id='%s'>%s. %s %s %s %s</option>", p.getId(), p.getId(),
                 p.getProjectName(), p.getMemberName(), p.getTaskName(), p.getStatusName()))
                .collect(Collectors.joining(""));
    }
}
