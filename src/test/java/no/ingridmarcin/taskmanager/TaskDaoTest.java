package no.ingridmarcin.taskmanager;

import no.ingridmarcin.taskmanager.daos.TasksDao;
import no.ingridmarcin.taskmanager.objects.Task;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskDaoTest {

    private JdbcDataSource dataSource = DatabaseTest.testDataSource();

    @Test
    void shouldFindStoredProjects() throws SQLException {
        TasksDao taskDao = new TasksDao(dataSource);
        Task task = sampleProject();
        long id = taskDao.insert(task);
        task.setId(id);
        assertThat(taskDao.listAll()).contains(task);
    }

    @Test
    void shouldSaveAllProjectFields() throws SQLException {
        TasksDao taskDao = new TasksDao(dataSource);
        Task task = sampleProject();
        long id = taskDao.insert(task);
        task.setId(id);
        assertThat(task).hasNoNullFieldsOrProperties();
        assertThat(taskDao.retrieve(id))
                .isEqualToComparingFieldByField(task);
    }

    private Task sampleProject() {
        Task task = new Task();
        String taskName = "clean table";
        task.setTaskName(taskName);
        return task;
    }
}
