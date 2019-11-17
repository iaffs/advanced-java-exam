package no.ingridmarcin.taskmanager;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class StatusTest {

    private JdbcDataSource dataSource = DatabaseTest.testDataSource();

    @Test
    void shouldFindStoredStatus() throws SQLException {
        StatusDao statusDao = new StatusDao(dataSource);
        Status status = sampleProject();
        long id = statusDao.insert(status);
        status.setId(id);
        assertThat(statusDao.listAll()).contains(status);
    }

    @Test
    void shouldSaveAllStatusFields() throws SQLException {
        StatusDao statusDao = new StatusDao(dataSource);
        Status status = sampleProject();
        long id = statusDao.insert(status);
        status.setId(id);
        assertThat(status).hasNoNullFieldsOrProperties();
        assertThat(statusDao.retrieve(id))
                .isEqualToComparingFieldByField(status);
    }

    private Status sampleProject() {
        Status status = new Status();
        String statusName = "To do";
        status.setStatusName(statusName);
        return status;
    }
}
