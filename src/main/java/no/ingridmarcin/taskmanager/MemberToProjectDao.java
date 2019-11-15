package no.ingridmarcin.taskmanager;


import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MemberToProjectDao extends AbstractDao<MemberToProject> {

    public MemberToProjectDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertObject(MemberToProject memberToProject, PreparedStatement statement) throws SQLException {
        statement.setString(1, memberToProject.getProjectName());
        statement.setString(2, memberToProject.getMemberName());
        statement.setString(3, memberToProject.getTaskName());
    }

    @Override
    protected MemberToProject readObject(ResultSet rs) throws SQLException {
        MemberToProject memberToProject = new MemberToProject();
        memberToProject.setId(rs.getLong("id"));
        memberToProject.setProjectName(rs.getString("project_name"));
        memberToProject.setMemberName(rs.getString("member_name"));
        memberToProject.setTaskName(rs.getString("task_name"));
        return memberToProject;
    }

    public long insert(MemberToProject memberToProject) throws SQLException {
        return insert(memberToProject,
                "insert into member_to_project (project_name, member_name, task_name) values (?, ?, ?)"
        );
    }

    //we had to use seperate method different than insert from AbstractDao,  cause we didnt have automatic primary key in member_to_project table
    /*public void insertMemberToProject(MemberToProject memberToProject) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(
                    "insert into member_to_project (project_name, member_name) values (?, ?)"
                )) {
                insertObject(memberToProject, statement);
                statement.executeUpdate();
            }
        }
    } */

    public List<MemberToProject> listAll() throws SQLException {
        return listAll(
                "select * from member_to_project"
        );
    }

    public List<MemberToProject> selectUnique(String project_name, long member_name) throws SQLException {
        return listAll(
                "select * from member_to_project where project_name = " + project_name
                        + " and member_name = " + member_name
        );
    }

    public String listToString(List <MemberToProject> memberToProject) {
        return Arrays.toString((memberToProject).toArray())
                .replace("[", " ")
                .replace("]", "")
                .replace(",", "");
    }
}
