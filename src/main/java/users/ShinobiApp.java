package users;

import connection.ConnectionFactory;
import users.User;
import users.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class ShinobiApp {

    public static void main (String[] args){

        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        System.out.println(users);


    }
}
