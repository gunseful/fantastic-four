package app.model.user;

import java.util.Objects;

public class User {

    private int id;
    private String name;
    private String nickname;
    private String password;
    private boolean isAdministrator;
    private boolean isInBlackList;
    private String role;

    public User() {
    }

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public User(String name, String nickname, String password) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isInBlackList() {
        return isInBlackList;
    }

    public void setInBlackList(boolean inBlackList) {
        isInBlackList = inBlackList;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean isAdministrator) {
        this.isAdministrator = isAdministrator;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", isAdministrator=" + isAdministrator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                isAdministrator == user.isAdministrator &&
                Objects.equals(name, user.name) &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nickname, password, isAdministrator);
    }
}
