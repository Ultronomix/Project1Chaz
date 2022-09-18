package users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import authorization.Credentials;
import common.ResourceCreationResponse;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;

import users.Role;
import users.User;
import users.UserDAO;
import users.UserService;
public class UserServiceTest {
    UserService sut;
    UserDAO mockUserDAO;

    @BeforeEach
    public void setup() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new UserService(mockUserDAO);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO);

    }

    @Test
    public void test_getAllUsers_givenAListOfUsers() {

        //Arrange
        List<User> mockList = new ArrayList<>();
        User userStub = new User();
        mockList.add(userStub);
        when(mockUserDAO.getAllUsers()).thenReturn(mockList);
        List<UserResponse> expectedResult = mockUserDAO.getAllUsers().stream().map(UserResponse::new).collect(Collectors.toList());

        //Act
        List<UserResponse> actualResult = sut.getAllUsers();

        //Assertion
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);  // The objects compared need a .equals method in DAO
        verify(mockUserDAO, times(2)).getAllUsers();

    }

    @Test
    public void test_getUserByIdSuccessfully_givenValidId() {

        //Arrange
        User userStub = new User();
        when(mockUserDAO.getUserByUserId(anyString())).thenReturn(Optional.of(userStub));
        UserResponse expectedResult = new UserResponse(userStub);

        //Act
        UserResponse actualResult = sut.getUserByUserId("some-uuid");

        //Assertion
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(mockUserDAO, times(1)).getUserByUserId(anyString());
    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_givenNullId() {

        //Arrange
        String idStub = null;

        //Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUserId(idStub);
        });

        verify(mockUserDAO, times(0)).getUserByUserId(anyString());

    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_givenUknownUserId() {

        //Arrange
        String unknownUserId = "unknown-uuid";
        when(mockUserDAO.getUserByUserId(anyString())).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getUserByUserId(unknownUserId);
        });

        verify(mockUserDAO, times(1)).getUserByUserId(anyString());

    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_givenInvalidUUIDString() {
        //Arrange
        User userStub = new User();
        String badUserId = "";
        //Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUserId(badUserId);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());
    }
}
//    @Test
//    public void test_updateUser_GivenValidUserToUpdate() {

