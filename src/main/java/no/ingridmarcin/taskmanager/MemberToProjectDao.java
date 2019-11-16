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
        statement.setString(4, memberToProject.getStatusName());
    }

    @Override
    protected MemberToProject readObject(ResultSet rs) throws SQLException {
        MemberToProject memberToProject = new MemberToProject();
        memberToProject.setId(rs.getLong("id"));
        memberToProject.setProjectName(rs.getString("project_name"));
        memberToProject.setMemberName(rs.getString("member_name"));
        memberToProject.setTaskName(rs.getString("task_name"));
        memberToProject.setStatusName(rs.getString("status_name"));
        return memberToProject;
    }

    public long insert(MemberToProject memberToProject) throws SQLException {
        return insert(memberToProject,
                "insert into member_to_project (project_name, member_name, task_name, status_name) values (?, ?, ?, ?)"
        );
    }

    public void update(String status, long id) throws SQLException {
         update(
                "update member_to_project set status_name = ? where id=?"
                , status, id);
    }

    public List<MemberToProject> filter(String name) throws SQLException {
        return filter(
                "select * from member_to_project where member_name =? order by id asc",
                name);
    }

    public List<MemberToProject> listAll() throws SQLException {
        return listAll(
                "select * from member_to_project order by id asc"
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
