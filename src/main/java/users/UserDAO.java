package users;
import common.connection.ConnectionFactory;
import common.exceptions.DataSourceException;

import java.time.LocalDateTime;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDAO {

    private static Logger logger = LogManager.getLogger(UserDAO.class);


    private final String baseSelect = " Select eu.user_id, eu.username, eu.email, eu.\"password\", eu.given_name, eu.surname, eu.is_active, eu.role_id, eur.role_ " +
            "FROM project1.ers_users eu " +
            "JOIN project1.ers_user_roles eur " +
            "ON eu.role_id = eur.role_id ";


    public List<User> getAllUsers() {

        List<User> allUsers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(baseSelect);

            allUsers = mapResultSet(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when communicating with the database");
            e.printStackTrace();
        }

        return allUsers;

    }

    public Optional<User> getUserByUserId(String userId) {

        String sql = baseSelect + " WHERE eu.user_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, userId);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

    }

    public Optional<User> getUserByUsername(String username) {

        String sql = baseSelect + " WHERE eu.username = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            // TODO log this exception
            throw new DataSourceException(e);
        }

    }


    public Optional<User> getUserByEmail(String email) {

        String sql = baseSelect + " WHERE eu.email = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

        public Optional<User> getUserByRole(String role) {

            String sql = baseSelect + " WHERE eu.role_id = ? ";

            try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

                // JDBC Statement objects are vulnerable to SQL injection
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, role);
                ResultSet rs = pstmt.executeQuery();
                return mapResultSet(rs).stream().findFirst();

            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataSourceException(e);
            }

    }


    public Optional<User> findUserByUsernameAndPassword(String username, String password) {

        String sql = baseSelect + " WHERE eu.username = ? AND eu.password = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

    }



    private List<User> mapResultSet(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getString("user_id"));
            user.setGivenName(rs.getString("given_name"));
            user.setSurname(rs.getString("surname"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password")); // done for security purposes
            user.setRole(rs.getString("role_"));
            users.add(user);
        }
        return users;
    }


    public String updateUserGivenName(String givenName, String userId) {
        String sql = "UPDATE project1.ers_users " +
                " SET given_name = ? " +
                " WHERE user_id = ? " ;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, givenName);
            pstmt.setString(2, userId);
            ResultSet rs = pstmt.executeQuery();

            return "User first name updated to " + givenName + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserSurname(String surname, String user_id) {
        String sql = "UPDATE project1.ers_users " +
                " SET surname = ? " +
                " WHERE user_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, surname);
            pstmt.setString(2, user_id);
            ResultSet rs = pstmt.executeQuery();

            return "User last name updated to " + surname + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserEmail(String email, String user_id) {
        String sql = " UPDATE project1.ers_users " +
                " SET email = ? " +
                " WHERE user_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, user_id);

            ResultSet rs = pstmt.executeQuery();

            return "User email updated to " + email + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }  // todo must finish update methods for all columns


    public void log(String level, String message) {
        try {
            File logFile = new File("logs/app.log");
            logFile.createNewFile();
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile));
            logWriter.write(String.format("[%s] at %s logged: [%s] %s\n", Thread.currentThread().getName(), LocalDate.now(), level.toUpperCase(), message));
            logWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String register(User user) {

        String sql = " INSERT INTO project1.ers_users (user_id, username, given_name, surname, email, \"password\", is_active, role_id) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"user_id"});
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getGivenName());
            pstmt.setString(4, user.getSurname());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPassword());
            pstmt.setBoolean(7, user.getIsActive());
            pstmt.setString(8, user.getRole());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            user.setUserId(rs.getString("user_id"));

        } catch (SQLException e) {
           log("ERROR", e.getMessage());
        }

        log("INFO", "Successfully persisted new user with id: " + user.getUserId());

        return user.getUserId();

    }

    public boolean isUsernameTaken (String username){
        return getUserByUsername(username).isPresent();
    }

    public boolean isEmailTaken (String email) {
        return getUserByEmail(email).isPresent();
    }


    public void updateUser(User user) {


        String sql = "UPDATE project1.ers_users " +
                " SET user_id = ?, username = ?, email = ?, given_name = ?, surname = ?, \"password\" = ?, role_id = ? " +
                " WHERE project1.ers_users.role_id = ? ";

        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getGivenName());
            pstmt.setString(5, user.getSurname());
            pstmt.setString(6, user.getPassword());
            pstmt.setString(7, user.getRole());
            pstmt.setString(8, user.getRole());

            ResultSet rs = pstmt.getGeneratedKeys();


            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("# of rows updated: " + rowsUpdated);

        } catch(SQLException e) {
            e.printStackTrace();
            logger.warn("unable to persist data at {}, error messages {}" , LocalDateTime.now(), e.getMessage());
        }


        }
    }


