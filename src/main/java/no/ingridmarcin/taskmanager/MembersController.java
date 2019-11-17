package no.ingridmarcin.taskmanager;

import no.ingridmarcin.http.HttpController;
import no.ingridmarcin.http.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class MembersController  implements HttpController {
    private MemberDao memberDao;

    public MembersController(MemberDao memberDao){
        this.memberDao = memberDao;
    }

    @Override
    public void handle(String requestAction, String path, Map<String, String> queryParameters, String requestBody, OutputStream outputStream) throws IOException {
        try {
            if (requestAction.equals("POST")) {
                queryParameters = HttpServer.parseQueryString(requestBody);
                Member member = new Member();

                String memberName = java.net.URLDecoder.decode(queryParameters.get("memberName"), StandardCharsets.UTF_8);
                String mail = java.net.URLDecoder.decode(queryParameters.get("mail"), StandardCharsets.UTF_8);

                member.setMemberName(memberName);
                member.setMail(mail);
                memberDao.insert(member);
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080/createMember.html\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
                return;

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
        String body = memberDao.listAll().stream()
                .map(p -> String.format("<option id='%s'> %s </option>", p.getId(), p.getMemberName()))
                .collect(Collectors.joining(""));
        return body;
    }
}
