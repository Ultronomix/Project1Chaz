package users;

import common.ResourceCreationResponse;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;
import common.exceptions.ResourcePersistenceException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    public List<UserResponse> getAllUsers(){
        // Imperative (more explicit about what is being done)
//        List<UserResponse> result = new ArrayList<>();
//        List<User> users = userDAO.getAllUsers();
//        for (User user : users) {
//            result.add(new UserResponse(user));
//        }
//        return result;

        // Functional approach (more declarative)
        return userDAO.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse getUserByUserId(String userId) {

        if (userId == null || userId.length() <= 0 ){
            throw new InvalidRequestException("A non-empty user id must be provided!");
        }

        try{
            UUID uuid = UUID.fromString(userId);
            return userDAO.findUserByUserId(uuid)
                    .map(UserResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        }catch (IllegalArgumentException e){
            throw new InvalidRequestException("An invalid UUID String was provided.");
        }
    }

    public ResourceCreationResponse register(NewUserRequest newUser ){

        if (newUser == null){
            throw new InvalidRequestException("Provided request payload was null.");
        }
        if (newUser.getGivenName() == null || newUser.getGivenName().length() <= 0 ||
        newUser.getSurname() == null || newUser.getSurname().length() <= 0)
        {
            throw new InvalidRequestException("A non-empty given name and surname must be provided!");
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

}
