import users.User;

import java.io.Serializable;
import java.util.Objects;

public class UserResponse implements Serializable {

    private String userId;
    private String username;
    private String email;
    private String password;
    private String givenName;
    private String surname;
    private String roleID;
    private String role;

    public UserResponse (User subject) {
        this.userId = subject.getUserID();
        this.username = subject.getUsername();
        this.email = subject.getEmail();
        this.password = subject.getPassword();
        this.givenName = subject.getGivenName();
        this.surname = subject.getSurname();
        this.roleID = subject.getRoleID();
        this.role = subject.getRole().getName();


    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(givenName, that.givenName) && Objects.equals(surname, that.surname) && Objects.equals(roleID, that.roleID) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, password, givenName, surname, roleID, role);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", givenName='" + givenName + '\'' +
                ", surname='" + surname + '\'' +
                ", roleID='" + roleID + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
