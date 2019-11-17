package no.ingridmarcin.taskmanager;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.*;


public class MemberToProjectTest {

    private JdbcDataSource dataSource = DatabaseTest.testDataSource();

    @Test
    void shouldFilterByName() throws SQLException {
    MemberToProject memberToProject = new MemberToProject();
    memberToProject.setId(1);
    memberToProject.setMemberName("Joseph");
    memberToProject.setProjectName("Vacanza");
    memberToProject.setTaskName("cleaning");
    memberToProject.setStatusName("ongoing");
    MemberToProjectDao memberToProjectDao = new MemberToProjectDao(dataSource);
    memberToProjectDao.insert(memberToProject);

    assertThat(memberToProjectDao.filter("Joseph")).contains(memberToProject);

    }




}
