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

import common.exceptions.ResourcePersistenceException;
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

    @Test
    public void testGetUserbyRole_InvalidRequestException_EmptyString() {

        String role_ = "";

        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByRole(role_);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(role_);
    }

    @Test
    public void testGetUserbyRole_InvalidRequestException_Null() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByRole(null);
        });

        verify(mockUserDAO, times(0)).getUserByRole(null);
    }

    @Test
    public void testGetUserbyRole() {

        User userStub = new User();
        when(mockUserDAO.getUserByRole(anyString())).thenReturn(Optional.of(userStub));

        UserResponse actual = sut.getUserByRole("Admin");
        UserResponse expected = new UserResponse(userStub);

        assertEquals(expected, actual);
    }

    @Test
    public void testRegister_InvalidRequestException_Null() {

        NewUserRequest newUser = new NewUserRequest();

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(null);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    public void testRegister_InvalidRequestException_GivenName_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("");

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(null);
    }

    @Test
    public void testRegister_InvalidRequestException_GivenName_Null() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName(null);
        newUser.setSurname("Test");
        newUser.setEmail("test@gmail.com");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    public void testRegister_InvalidRequestException_All() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("");
        newUser.setSurname("");
        newUser.setEmail("test@gmail.com");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    public void testRegister_InvalidRequestException_Surname_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("");
        newUser.setEmail("test@gmail.com");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }


    @Test
    public void testRegister_InvalidRequestException_Surname_Null() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname(null);
        newUser.setEmail("test@gmail.com");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    public void testRegister_InvalidRequestException_Email_Null() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setEmail(null);
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    public void testRegister_InvalidRequestException_Email_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    public void testRegister_InvalidRequestException_Username_Null() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername(null);

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    public void testRegister_InvalidRequestException_Username_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    public void testRegister_InvalidRequestException_Password_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setActive(true);
        newUser.setPassword("");
        newUser.setUserId("6");
        newUser.setUsername("Tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    @Test
    void testRegister_InvalidRequestException_Password_Null() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.isActive();
        newUser.setPassword(null);
        newUser.setUserId("6");
        newUser.setUsername("Tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).register(user);
    }

    //@Test

    @Test
    void testRegister_ResourcePersistenceException_IsEmailTaken_true() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("Tester");

        when(mockUserDAO.isEmailTaken(newUser.getEmail())).thenReturn(true);

        assertThrows(ResourcePersistenceException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(1)).isEmailTaken(newUser.getEmail());
    }

    @Test
    void testRegister_ResourcePersistenceException_IsUsernameTaken_true() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setActive(true);
        newUser.setPassword("password");
        newUser.setUserId("6");
        newUser.setUsername("Tester");

        when(mockUserDAO.isUsernameTaken(newUser.getUsername())).thenReturn(true);

        assertThrows(ResourcePersistenceException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(1)).isUsernameTaken(newUser.getUsername());
    }

    @Test
    public void testRegister_isIsActive_false() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setUserId("6");
        newUser.setUsername("Tester");
        newUser.setPassword("password");
        newUser.setEmail("test@test.com");
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");

        User user = newUser.extractEntity();

        when(mockUserDAO.register(user)).thenReturn(user.getUsername() + " has been added.");

        ResourceCreationResponse actual = sut.register(newUser);
        ResourceCreationResponse expected = new ResourceCreationResponse("Tester has been added.");
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRegister_isIsActive_true() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setUserId("6");
        newUser.setUsername("Tester");
        newUser.setPassword("password");
        newUser.setEmail("test@test.com");
        newUser.setGivenName("Tester");
        newUser.setSurname("Test");
        newUser.setActive(true);

        User user = newUser.extractEntity();

        when(mockUserDAO.register(user)).thenReturn(user.getUsername() + " has been added.");

        ResourceCreationResponse actual = sut.register(newUser);
        ResourceCreationResponse expected = new ResourceCreationResponse("Tester has been added.");
        assertNotNull(actual);
        assertEquals(expected, actual);
    }


    @Test
    public void testUpdateUser_Null() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.updateUser(null);
        });
    }











    @Test
    public void testUpdateUser_Role_Invalid() {

        UpdateUserRequest updateUser = new UpdateUserRequest();
        updateUser.setUsername("test");
        updateUser.setRole("rol");

        assertThrows(InvalidRequestException.class, () -> {
            sut.updateUser(updateUser);
        });

    }
}



//    @Test
//    public void test_updateUser_GivenValidUserToUpdate() {

