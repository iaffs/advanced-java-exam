package no.ingridmarcin.taskmanager;

import no.ingridmarcin.http.HttpController;
import no.ingridmarcin.http.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
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
            if(requestAction.equals("POST")) {
                queryParameters = HttpServer.parseQueryString(requestBody);
                System.out.println(requestBody);
                // kinda risky but lets assume no one uses 'fetchID' as a name or email
                if(requestBody.contains("fetch")) {
                    updateStatus(queryParameters);
                } else {
                    executeAssignment(queryParameters);
                }
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080/assignMemberToProjects.html\r\n" +
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

    /* OK so i need to return id which will be an id i need to assign to the row! */

    public void executeAssignment(Map<String, String> queryParameters) throws SQLException {
        MemberToProject memberToProject = new MemberToProject();
        memberToProject.setProjectName(queryParameters.get("projects"));
        memberToProject.setMemberName(queryParameters.get("members"));
        memberToProject.setTaskName(queryParameters.get("tasks"));
        memberToProject.setStatusName(queryParameters.get("status"));
        memberToProjectDao.insert(memberToProject);
    }

    public void updateStatus(Map<String, String> queryParameters) throws SQLException {
        String status = queryParameters.get("status");
        String id = queryParameters.get("fetchId");
        long idToLong = Long.parseLong(id);
        memberToProjectDao.update(status, idToLong);
    }


    public String getBody() throws SQLException {
        return memberToProjectDao.listAll().stream()
                .map(p -> String.format("<option id='%s'>%s. %s %s %s %s</option>", p.getId(), p.getId(), p.getProjectName(), p.getMemberName(), p.getTaskName(), p.getStatusName()))
                .collect(Collectors.joining(""));
    }
}
