package no.ingridmarcin.taskmanager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractDao<T> {
    protected DataSource dataSource;


    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public abstract void insertObject(T project, PreparedStatement statement) throws SQLException;


    public long insert(T projectName, String sql1) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertObject(projectName, statement);
                statement.executeUpdate();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return  generatedKeys.getLong("id");
            }
        }
    }
    // used only to change status
    public void update(String sql, String status, long id) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1,status);
                statement.setLong(2,id);
                statement.executeUpdate();
            }
        }
    }

    // used only to filter members
    public List<T> filter(String sql, String name) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1,name);
                statement.executeQuery();
                try (ResultSet rs = statement.executeQuery()) {
                    List<T> result = new ArrayList<>();

                    while (rs.next()) {
                        result.add(readObject(rs));
                    }
                    return result;
                }
            }
        }
    }



    public List<T> listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<T> result = new ArrayList<>();

                    while (rs.next()) {
                        result.add(readObject(rs));
                    }
                    return result;
                }
            }
        }
    }

    public String listToString(List <T> object) {
        return Arrays.toString((object).toArray())
                .replace("[", " ")
                .replace("]", "")
                .replace(",", "");
    }


    protected abstract T readObject(ResultSet rs) throws SQLException;
}
