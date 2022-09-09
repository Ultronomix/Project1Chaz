package users;
import common.connection.ConnectionFactory;
import common.exceptions.DataSourceException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {


    private final String baseSelect = "Select eu.user_id, eu.username, eu.email, eu.password, eu.given_name, eu.surname, eu.is_active, eu.role_id, eur.role_ " +
            "FROM project1.ers_users eu " +
            "JOIN project1.ers_user_roles eur " +
            "ON eu.role_id = eur.role_id";

    public List<User> getAllUsers() {

        List<User> allUsers = new ArrayList<>();


        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {


            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(baseSelect);

            allUsers = mapResultSet(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when communicating with the database");
            e.printStackTrace();
        }
        return allUsers;

    }

    public Optional<User> findUserByUserId(String id) {

        String sql = baseSelect + " WHERE eu.username = ? AND eu.password = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, id);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);

        }

    }

    public Optional<User> findUserByUsername(String username) {
        String sql = baseSelect + " WHERE eu.username = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, username);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);


        }

    }

    public Optional<User> findUserByRole(String role) {

        String sql = baseSelect + " WHERE eur.role_ = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            //TODO log exception
            throw new DataSourceException(e);
        }
    }

    public boolean isUsernameTaken(String username) {
        return findUserByUsername(username).isPresent();
    }

    public Optional<User> findUserByEmail(String email) {

        String sql = baseSelect + " WHERE eu.email = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {


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
        return findUserByEmail(email).isPresent();
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {

        String sql = baseSelect + " WHERE eu.username = ? AND eu.password = ?";

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


    public String save(User user) {

        String sql = "INSERT INTO project1.ers_users (user_id, userName, email, password, given_name, surname, is_active, role_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, '0001')";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"user_id"});
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getGivenName());
            pstmt.setString(5, user.getSurname());
            pstmt.setString(6, user.getIsActive());
            pstmt.setString(7, user.getRoleID());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            user.setUserID(rs.getString("user_id"));

        } catch (SQLException e) {
            log("ERROR", e.getMessage());
        }

        log("INFO", "Successfully persisted new used with id: " + user.getUserID());

        return user.getUserID();

    }

    public String updateUserEmail(String email, String user_id) {

        String sql = "UPDATE project1.ers_users SET email = ? WHERE user_id = '2'";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println(email);
            pstmt.setString(1, email);
            System.out.println(user_id);
            // pstmt.setString(2, "2");
            // ResultSet rs =
            System.out.println("pstmt: " + pstmt);
            pstmt.executeUpdate();
            // pstmt.executeQuery();
            System.out.println("4");
            return "email added";
            // mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            //TODO log exception
            throw new DataSourceException(e);
        }
        // return "Update Email check";
    }

    private List<User> mapResultSet(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
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
            users.add(user);

        }
        return users;

    }

    public void log(String level, String message) {
        try {
            File logFile = new File("src/main/resources/ShinobiApp.log");
            logFile.createNewFile();
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile,true));
            logWriter.write(String.format("\n[%s] at %s logged: [%s] %s", Thread.currentThread().getName(), LocalDate.now(), level.toUpperCase(), message));
            logWriter.flush();
            logWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        public String testLog () {

            try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
                System.out.println("Connected");
                log("WORK", "Connected");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                log("ERROR", e.getMessage());
                System.out.println("Logged");
            }
            return "Done";
        }
    }
