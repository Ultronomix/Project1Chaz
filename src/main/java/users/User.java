package users;

import java.util.Objects;

public class User {

    private String userId;
    private String givenName;
    private String surname;
    private String email;
    private String username;
    private String password;

    private boolean isActive;
    private Role role;

    public User() {
        super();
    }

    public User(String userId, String givenName, String surname, String email, String username, String password, Role role) {
        this.userId = userId;
        this.givenName = givenName;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(givenName, user.givenName) && Objects.equals(surname, user.surname) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(isActive, user.isActive) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, givenName, surname, email, username, password, isActive, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", givenName='" + givenName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive='" + isActive + '\'' +
                ", role=" + role +
                '}';
    }

}


//
//    private String userID;
//    private String username;
//    private String email;
//    private String password;
//    private String givenName;
//    private String surname;
//    private String isActive;
//    private Role role;
//
//    public User(){
//        super();
//    }
//
//    public User(String userID, String givenName, String surname, String email, String username, String password, Role role) {
//        this.userID = userID;
//        this.givenName = givenName;
//        this.surname = surname;
//        this.email = email;
//        this.username = username;
//        this.password = password;
//        this.role = role;
//
//
//    }
//
//    public String getUserID() {
//        return userID;
//    }
//
//    public void setUserID(String userID) {
//        this.userID = userID;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getGivenName() {
//        return givenName;
//    }
//
//    public void setGivenName(String givenName) {
//        this.givenName = givenName;
//    }
//
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }
//
//    public String getIsActive() {
//        return isActive;
//    }
//
//    public void setIsActive(String isActive) {
//        this.isActive = isActive;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o;
//        return Objects.equals(userID, user.userID) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(givenName, user.givenName) && Objects.equals(surname, user.surname) && Objects.equals(isActive, user.isActive)  && Objects.equals(role, user.role);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(userID, username, email, password, givenName, surname, isActive, role);
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "userID='" + userID + '\'' +
//                ", username='" + username + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", givenName='" + givenName + '\'' +
//                ", surname='" + surname + '\'' +
//                ", isActive='" + isActive + '\'' +
//                ", role=" + role +
//                '}';
//    }
//}
