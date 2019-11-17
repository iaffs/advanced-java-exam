package no.ingridmarcin.taskmanager;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberControllerTest {

    private JdbcDataSource dataSource = DatabaseTest.testDataSource();

    @Test
    void shouldReturnAllElements() throws SQLException {
        MemberDao memberDao = new MemberDao(dataSource);
        Member member = new Member();
        member.setId(1);
        member.setMemberName("Joseph");
        member.setMail("joseph@gmail.com");
        memberDao.insert(member);

        MembersController controller = new MembersController(memberDao);
        assertThat(controller.getBody())
                .contains("<option id='" + member.getId() + "'> " + member.getMemberName() + " </option>");
    }

    @Test
    void shouldCreateNewMember() throws IOException, SQLException {
        MemberDao memberDao = new MemberDao(dataSource);
        MembersController controller = new MembersController(memberDao);
        String requestBody = "name=Joseph&mail=kutasiarz";
        controller.handle("POST", "api/members", null, requestBody, new ByteArrayOutputStream());

        assertThat(memberDao.listAll())
                .extracting(Member::getMemberName)
                .contains("Joseph");
    }
}
