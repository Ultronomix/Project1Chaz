package users;
import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {

    public List<User> getAllUsers() {

        String sql = "Select eu.user_id, eu.username, eu.email, eu.password, eu.given_name, eu.surname, eu.is_active, eu.role_id, eur.role_" +
                "FROM ers_users eu " +
                "JOIN ers_user_roles eur" +
                "ON eu.role_id = eur.role_id";

List<User> allUsers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {


            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);

        while (rs.next()) {
            User user = new User();
            user.setUserID(rs.getString("user_id"));
            user.setUsername(rs.getString("given_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("******************"));
            user.setGivenName(rs.getString("given_name"));
            user.setSurname(rs.getString("surname"));
            user.setIsActive(rs.getString("is_active"));
            user.setRole(new Role(rs.getString("role_id"), rs.getString("role")));
            allUsers.add(user);


        }

        } catch (SQLException e) {
            System.err.println("something went wrong when communicating with the database");
            e.printStackTrace();

        }
        return allUsers;
    }
}
