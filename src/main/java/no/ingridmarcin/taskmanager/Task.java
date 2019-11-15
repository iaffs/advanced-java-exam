package no.ingridmarcin.taskmanager;


import java.util.Objects;

public class Task {
    private long id;
    private String name;

    public void setId(long id) {
        this.id = id;
    }

    public void setTaskName(String name) {
        this.name = name;
    }

    public String getTaskName() {
        return this.name;
    }

    public long getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

}