package no.ingridmarcin.taskmanager;

import java.util.Objects;

public class MemberToProject {
    private long id;
    private String memberName;
    private String projectName;
    private String taskName;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setMemberName(String memberName){
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return "MemberToProject{" +
                "projectName='" + projectName + '\'' +
                "memberName='" + memberName + '\'' +
                "tasks='" + taskName + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MemberToProject memberToProject = (MemberToProject) o;
        return  id == memberToProject.id &&
                Objects.equals(projectName, memberToProject.projectName) &&
                Objects.equals(memberName, memberToProject.memberName) &&
                Objects.equals(taskName, memberToProject.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectName, memberName, taskName);
    }

}
