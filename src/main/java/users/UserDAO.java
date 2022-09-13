package users;
import common.connection.ConnectionFactory;
import common.exceptions.DataSourceException;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
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

    public boolean isEmailTaken(String email) {
        return getUserByEmail(email).isPresent();
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
            user.setRole(new Role(rs.getString("role_id"), rs.getString("role_")));
            users.add(user);
        }
        return users;
    }


    public String updateUserGivenName(String givenName, String user_id) {
        String sql = baseSelect + " WHERE given_name = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, givenName);
            pstmt.setInt(2, Integer.parseInt(user_id));

            int rs = pstmt.executeUpdate();

            return "User first name updated to " + givenName + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserSurname(String surname, String user_id) {
        String sql = baseSelect + " WHERE surname = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, surname);
            pstmt.setInt(2, Integer.parseInt(user_id));

            int rs = pstmt.executeUpdate();

            return "User last name updated to " + surname + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserEmail(String email, String user_id) {
        String sql = baseSelect + " WHERE email = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setInt(2, Integer.parseInt(user_id));

            int rs = pstmt.executeUpdate();

            return "User email updated to " + email + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserPassword(String password, String user_id) {
        String sql = baseSelect + " WHERE \"password\" = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, password);
            pstmt.setInt(2, Integer.parseInt(user_id));

            int rs = pstmt.executeUpdate();

            return "User password updated to " + password + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserIsActive(String isActive, String user_id) {
        String sql = baseSelect + " WHERE is_active = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setBoolean(1, Boolean.parseBoolean(isActive));
            pstmt.setInt(2, Integer.parseInt(user_id));

            int rs = pstmt.executeUpdate();

            return "User active status updated to " + isActive + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserRoleId(String roleId, String user_id) {
        String sql = baseSelect + " WHERE eu.role_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(roleId));
            pstmt.setInt(2, Integer.parseInt(user_id));

            int rs = pstmt.executeUpdate();

            return "User role ID updated to " + roleId + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }
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
        public String save(User user) {

            String sql = "INSERT INTO project1.ers_users (user_id, given_name, surname, email, username, password, role_id) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?) ";

            try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

                PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"user_id"});
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getGivenName());
                pstmt.setString(3, user.getSurname());
                pstmt.setString(4, user.getEmail());
                pstmt.setString(5, user.getUsername());
                pstmt.setString(6, user.getPassword());
                pstmt.setString(7, user.getRole().getId());

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



    public void updateUser(User user) {

        System.out.println(user);

        String sql = "UPDATE project1.ers_users " +
                " SET given_name = ?, surname = ?, username = ?, \"password\" = ?, email = ? " +
                " WHERE user_id = ?";

        try(Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getGivenName());
            pstmt.setString(2, user.getSurname());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getUserId());
            pstmt.executeUpdate();

        } catch(SQLException e) {
            logger.warn("unable to persist data at {}, error messages {}" , LocalDateTime.now(), e.getMessage());
        }


        }
    }


