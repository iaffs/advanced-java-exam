package no.ingridmarcin.taskmanager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StatusDao extends AbstractDao<Status> {

    public StatusDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertObject(Status status, PreparedStatement statement) throws SQLException {
        statement.setString(1, status.getStatusName());
    }

    @Override
    protected Status readObject(ResultSet rs) throws SQLException {
        Status status = new Status();
        status.setId(rs.getLong("id"));
        status.setStatusName(rs.getString("name"));
        return status;
    }

    public long insert(Status status) throws SQLException {
        return insert(status,
                "insert into status (name) values (?)"
        );
    }

    public List<Status> listAll() throws SQLException {
        return listAll(
                "select * from status"
        );
    }

    public Status retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from status where id= ?")) {
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
