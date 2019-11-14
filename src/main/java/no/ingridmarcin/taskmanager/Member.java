package no.ingridmarcin.taskmanager;

import java.util.Objects;

public class Member {
    private Long id;
    private String memberName;
    private String mail;


    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return this.mail;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Member{" +
                "mail='" + mail + '\'' +
                "memberName='" + memberName + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return  Objects.equals(id, member.id) &&
                Objects.equals(memberName, member.memberName) &&
                Objects.equals(mail, member.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberName, mail);
    }
}
