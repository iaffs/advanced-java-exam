package no.ingridmarcin.taskmanager;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.*;


public class MemberToProjectTest {

    private JdbcDataSource dataSource = DatabaseTest.testDataSource();

    @Test
    void shouldFilterByName() throws SQLException {
        MemberToProject memberToProject = simpleAssignment();
        MemberToProjectDao memberToProjectDao = new MemberToProjectDao(dataSource);
        memberToProjectDao.insert(memberToProject);

        assertThat(memberToProjectDao.filter("Joseph")).contains(memberToProject);
    }

    @Test
    void shouldUpdateStatusById() throws SQLException {
        MemberToProject memberToProject = simpleAssignment();
        MemberToProjectDao memberToProjectDao = new MemberToProjectDao(dataSource);
        memberToProjectDao.insert(memberToProject);
        memberToProjectDao.update("done",1);

        assertThat(memberToProjectDao.listAll())
                .extracting(MemberToProject::getStatusName)
                .contains("done");
    }

    private MemberToProject simpleAssignment() {
        MemberToProject memberToProject = new MemberToProject();
        memberToProject.setId(1);
        memberToProject.setMemberName("Joseph");
        memberToProject.setProjectName("Vacanza");
        memberToProject.setTaskName("cleaning");
        memberToProject.setStatusName("ongoing");
        return memberToProject;
    }
}
