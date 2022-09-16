package users;

import common.Request;


public class NewUserRequest implements Request<User> {

   private String userId;
    private String username;
    private String givenName;
    private String surname;
    private String email;

    private String password;

    private boolean isActive;

    private Role role;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "NewUserRequest{" +
                "user_id='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", givenName='" + givenName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                ", role=" + role +
                '}';
    }

    @Override
    public User extractEntity() {
        User extractedEntity = new User();
        extractedEntity.setUserId(this.userId);
        extractedEntity.setUsername(this.username);
        extractedEntity.setEmail(this.email);
        extractedEntity.setPassword(this.password);
        extractedEntity.setGivenName(this.givenName);
        extractedEntity.setSurname(this.surname);
        extractedEntity.setIsActive((this.isActive));
        extractedEntity.setRole((this.role));
        return extractedEntity;
    }


    }
