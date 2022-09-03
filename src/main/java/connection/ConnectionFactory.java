package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionFactory {


    public static void main(String[] args) throws SQLException {
        String jdbcURL = "jdbc:postgresql://java-angular-220815.cq9t60lp8p7n.us-west-1.rds.amazonaws.com:5432/postgres?currentSchema=project1";
        String username = "chazc";
        String password = "The6thHokage!";
        try {
            @SuppressWarnings("unused")
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to PostgreSQL server");


            // connection.close();
        } catch (SQLException e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();
            Connection connection = null;
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(e.getMessage());
            if (rows > 0) {
                System.out.println("A new account has been created.");
            }
        }

    }
}