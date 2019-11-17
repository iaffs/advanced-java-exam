package no.ingridmarcin.taskmanager.daos;


import no.ingridmarcin.taskmanager.objects.Task;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TasksDao extends AbstractDao<Task> {

    public TasksDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertObject(Task task, PreparedStatement statement) throws SQLException {
        statement.setString(1, task.getTaskName());
    }

    @Override
    protected Task readObject(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTaskName(rs.getString("name"));
        return task;
    }

    public long insert(Task task) throws SQLException {
        return insert(task,
                "insert into tasks (name) values (?)"
        );
    }

    public List<Task> listAll() throws SQLException {
        return listAll(
                "select * from tasks"
        );
    }

    public Task retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from tasks where id = ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return readObject(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
