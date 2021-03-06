package no.ingridmarcin.taskmanager.daos;
import no.ingridmarcin.taskmanager.objects.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class MemberDao extends AbstractDao<Member> {


    public MemberDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertObject(Member member, PreparedStatement statement) throws SQLException {
        statement.setString(1, member.getMemberName());
        statement.setString(2, member.getMail());
    }

    @Override
    protected Member readObject(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setMemberName(rs.getString("name"));
        member.setMail(rs.getString("email"));
        return member;
    }

    public long insert(Member member) throws SQLException {
         return insert(member,
                 "insert into members (name, email) values (?, ?)"
         );
    }

    public List<Member> listAll() throws SQLException {
        return listAll(
                "select * from members"
        );
    }

}
