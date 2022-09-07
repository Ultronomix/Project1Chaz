package authorization;

import common.exceptions.AuthenticationException;
import common.exceptions.InvalidRequestException;
import users.UserDAO;
import users.UserResponse;
public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    public UserResponse authenticate(Credentials credentials){

        if(credentials == null){
            throw new InvalidRequestException("The provided credentials object was found to be null");
        }

        if(credentials.getUsername().length() < 4) {
            throw new InvalidRequestException("The provided username was not the correct length (must be at least 4 characters.");
        }

        if (credentials.getPassword().length() < 6) {
            throw new InvalidRequestException("The provided password was not the correct length (must be at least 6 characters.");
        }

        return userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                .map(UserResponse::new)
                .orElseThrow(AuthenticationException::new);
    }

}
