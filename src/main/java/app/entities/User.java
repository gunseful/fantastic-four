package app.entities;

import java.util.Objects;

public class User {
    private String name;
    private String dick;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.dick = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return dick;
    }

    public void setPassword(String password) {
        this.dick = password;
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

        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return dick != null ? dick.equals(user.dick) : user.dick == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (dick != null ? dick.hashCode() : 0);
        return result;
    }
}
