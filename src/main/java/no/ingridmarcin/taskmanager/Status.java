package no.ingridmarcin.taskmanager;

import java.util.Objects;

public class Status {
    private long id;
    private String name;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setStatusName(String name) {
        this.name = name;
    }

    public String getStatusName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Status status = (Status) o;
        return id == status.id && Objects.equals(name, status.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Status{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
