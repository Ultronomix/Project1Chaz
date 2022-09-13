package users;

import common.ResourceCreationResponse;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;
import common.exceptions.ResourcePersistenceException;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserResponse> getAllUsers() {
        return userDAO.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }


    public UserResponse getUserByUserId(String userid) {

        if (userid == null || userid.length() <= 0) {
            throw new InvalidRequestException("A non-empty id must be provided!");
        }

        return null;

    }


    public ResourceCreationResponse register(NewUserRequest newUser) {

        if (newUser == null) {
            throw new InvalidRequestException("Provided request payload was null.");
        }

        if (newUser.getGivenName() == null || newUser.getGivenName().length() <= 0 ||
                newUser.getSurname() == null || newUser.getSurname().length() <= 0) {
            throw new InvalidRequestException("A non-empty given name and surname must be provided");
        }

        if (newUser.getEmail() == null || newUser.getEmail().length() <= 0) {
            throw new InvalidRequestException("A non-empty email must be provided.");
        }

        if (newUser.getUsername() == null || newUser.getUsername().length() < 4) {
            throw new InvalidRequestException("A username with at least 4 characters must be provided.");
        }

        if (newUser.getPassword() == null || newUser.getPassword().length() < 8) {
            throw new InvalidRequestException("A password with at least 8 characters must be provided.");
        }

        if (userDAO.isEmailTaken(newUser.getEmail())) {
            throw new ResourcePersistenceException("Resource not persisted! The provided email is already taken.");
        }

        if (userDAO.isUsernameTaken(newUser.getUsername())) {
            throw new ResourcePersistenceException("Resource not persisted! The provided username is already taken.");
        }

        User userToPersist = newUser.extractEntity();
        String newUserId = userDAO.save(userToPersist);
        return new ResourceCreationResponse(newUserId);

    }

    public UserResponse getUserbyUsername(String usernameToSearchFor) {

        if (usernameToSearchFor == null || usernameToSearchFor.length() <= 0) {
            throw new InvalidRequestException("A non-empty username must be provided!");
        }

        return null;


    }

    public void updateUser(UpdateUserRequest updateUserRequest) {

        User userToUpdate = userDAO.getUserByUserId(updateUserRequest.getUserId())
                                   .orElseThrow(ResourceNotFoundException::new);

        System.out.println(userToUpdate);

        if (updateUserRequest.getGivenName() != null) {
            userToUpdate.setGivenName(updateUserRequest.getGivenName());
        }
        if(updateUserRequest.getUserId() != null) {
            userToUpdate.setUserId(updateUserRequest.getUserId());
        }
        if(updateUserRequest.getUsername() != null) {
            userToUpdate.setUsername(updateUserRequest.getUsername());
        }
        if (updateUserRequest.getRole_id() != null) {
            userToUpdate.setRole(updateUserRequest.getRole_id());
        }
        }

        // keep doing this for each field (remember to check for uniqueness for usernames and emails)

        // after you have copied all the updated values into the user for update
        // persist that user (create a DAO method that runs a SQL UPDATE statement



    public List<UserResponse> getUsersByRole(String roleToSearchFor) {
        return null;
    }
}
