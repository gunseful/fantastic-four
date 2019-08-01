package app.entities;

import java.util.Objects;

public class User {
    public int getId() {
        return id;
    }

    private int id;
    private String name;
    private int dick;

    public User() {
    }

    public User(String name, int dick) {
        this.name = name;
        this.dick = dick;
    }

    public User(int id, String name, int dick) {
        this.id = id;
        this.name = name;
        this.dick = dick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDick() {
        return dick;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + dick + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return dick == user.dick &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dick);
    }
}
