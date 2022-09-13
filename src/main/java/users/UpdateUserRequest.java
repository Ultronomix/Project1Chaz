package users;

import common.Request;


public class UpdateUserRequest implements Request<User> {

    private String userId;
    private String givenName;
    private String surname;
    private String username;
    private String email;
    private String password;

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

    @Override
    public User extractEntity() {
        User extractedEntity = new User();
        extractedEntity.setUserId(this.userId);
        extractedEntity.setUsername(this.username);
        extractedEntity.setPassword(this.password);
        extractedEntity.setEmail(this.email);
        extractedEntity.setGivenName(this.givenName);
        extractedEntity.setSurname(this.surname);
        return extractedEntity;
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", givenName='" + givenName + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}


