package authentication;

import authorization.AuthService;
import authorization.Credentials;
import common.exceptions.AuthenticationException;
import common.exceptions.InvalidRequestException;
import users.Role;
import users.User;
import users.UserDAO;
import users.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    AuthService sut;
    UserDAO mockUserDAO;

    @BeforeEach
    public void setup() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new AuthService(mockUserDAO);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO);
    }

    @Test
    public void test_authenticate_returnsSuccessfully_givenValidAndKnownCredentials() {

        // Arrange
        Credentials credentialsStub = new Credentials("valid", "credentials");
        User userStub = new User("some-uuid", "Val", "Id", "valid123@revature.net", "valid", "credentials", new Role("some-role-id", "QA"));
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(userStub));
        UserResponse expectedResult = new UserResponse(userStub);

        // Act
        UserResponse actualResult = sut.authenticate(credentialsStub);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(mockUserDAO, times(1)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenInvalidCredentials() {

        // Arrange
        Credentials credentialsStub = new Credentials("invalid", "creds");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());


    }

    @Test
    public void test_authenticate_throwsAuthenticationException_givenValidUnknownCredentials() {
        // TODO implement this test

        // Arrange
        Credentials credentialsStub = new Credentials("unknown", "credentials");
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        // Act
        assertThrows(AuthenticationException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        // Assert
        verify(mockUserDAO, times(1)).findUserByUsernameAndPassword(anyString(), anyString());

    }
}
