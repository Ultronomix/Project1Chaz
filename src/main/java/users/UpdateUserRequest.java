package users;

import common.Request;


public class UpdateUserRequest implements Request<User> {

    public UpdateUserRequest getGivenName;
    private String username;
    private String email;

    private String password;
    private String givenName;
    private String surname;
    private boolean isActive;

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


    public String getGiven_name() {
        return givenName;
    }


    public void setGiven_name(String given_name) {
        this.givenName = given_name;
    }


    public String getSurname() {
        return surname;
    }


    public void setSurname(String surname) {
        this.surname = surname;
    }


    public boolean isIs_active() {
        return isActive;
    }


    public void setIs_active(boolean is_active) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Updated {" +
                "given_name = " + isActive + "' " +
                "surname = " + surname + "' " +
                "password = " + password + "' " +
                "is_active" + isActive + "' " +
                "}";
    }

    @Override
    public User extractEntity() {
        // TODO
        User extractedEntity = new User();
        extractedEntity.setEmail(this.email);
        extractedEntity.setGivenName(this.givenName);
        extractedEntity.setSurname(this.surname);
        extractedEntity.setIsActive(String.valueOf(this.isActive));
        return extractedEntity;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
}


