package reimbursements;
import common.exceptions.DataSourceException;
import common.connection.ConnectionFactory;
import common.exceptions.ResourceNotFoundException;
import users.User;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import users.UserDAO;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;


public class ReimbursementsDAO {

    private static Logger logger = LogManager.getLogger(ReimbursementsDAO.class);

    private final String baseSelect = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, " +
            "er.description, er.payment_id, er.author_id, er.resolved_id, " +
            "ers.status, ert.type_ " +
            "FROM project1.ers_reimbursements er " +
            "JOIN project1.ers_reimbursement_statuses ers ON er.status_id = ers.status_id " +
            "JOIN project1.ers_reimbursement_types ert ON er.type_id = ert.type_id ";


    public List<Reimbursements> getAllReimbs() {

        logger.info("Attempting to connect to the database at {}", LocalDateTime.now());

        List<Reimbursements> allReimbs = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(baseSelect);

            allReimbs = mapResultSet(rs);
            logger.info("Successful database connection at {}", LocalDateTime.now());

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
            logger.fatal("Unsuccessful database connection at {}, error message: {}", LocalDateTime.now(), e.getMessage());
        }

        return allReimbs;
    }

    public Optional<Reimbursements> getReimbByReimbId(String reimb_id) {

        logger.info("Attempting to connect to the database at {}", LocalDateTime.now());


        String sql = baseSelect + "WHERE er.reimb_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reimb_id);
            ResultSet rs = pstmt.executeQuery();

            logger.info("Attempting to connect to the database at {}", LocalDateTime.now());


            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            logger.warn("Unable to process reimbursement id search at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();

            throw new DataSourceException(e);
        }
    }


    public Optional<Reimbursements> getReimbByStatus(String status) {

        logger.info("Attempting to connect to the database at {}", LocalDateTime.now());


        String sql = baseSelect + "WHERE er.status_id = ? ";


        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            logger.info("Reimbursement found by status at {}", LocalDateTime.now());

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();


        } catch (SQLException e) {
            logger.warn("Unable to process reimbursement status search at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

    public Optional<Reimbursements> getReimbByType(String type) {

        logger.info("Attempting to connect to the database at {}", LocalDateTime.now());


        String sql = baseSelect + "WHERE ert.type_id = ? ";


        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type.toUpperCase());
            ResultSet rs = pstmt.executeQuery();


            return mapResultSet(rs).stream().findFirst();

        } catch (Exception e) {
            logger.warn("Unable to process reimbursement type search at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }

    public void updateReimb(Reimbursements reimbursements) {


        String sql = "UPDATE project1.ers_reimbursements " +
                "SET amount = ?, description = ?, type_id = ? " +
                "WHERE reimb_id = ? ";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setFloat(1, reimbursements.getAmount());
            pstmt.setString(2, reimbursements.getDescription());
            pstmt.setString(3, reimbursements.getType_id());
            pstmt.setString(4, reimbursements.getReimb_id());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated != 1) {
                System.out.println("Sorry we didnt actually update anything.");
            }




        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }

    public String updateUserAmount(String reimbId, float newAmount) {


        String sql = "UPDATE project1.ers_reimbursements SET amount = ? WHERE reimb_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, newAmount);
            pstmt.setString(2, reimbId);

            pstmt.executeUpdate();

            return "Amount ";
        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }

    public String updateUserDescription(String reimbId, String description) {


        String sql = "UPDATE project1.ers_reimbursements SET description = ? WHERE reimb_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, description);
            pstmt.setString(2, reimbId);
            System.out.println(pstmt);
            pstmt.executeUpdate();

            return "Description ";
        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }


    public String updateUserType(String reimb_id, String type_id) {


        String sql = "UPDATE project1.ers_reimbursements SET type_id = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type_id);
            pstmt.setString(2, reimb_id);
            System.out.println(pstmt);
            pstmt.executeUpdate();

            return "Type ";
        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }


    public boolean isPending(String reimb_id) {

        try {
            Optional<Reimbursements> reimb = getReimbByReimbId(reimb_id);

            if (reimb.get().getStatus_id().equals("PENDING")) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {

            throw new ResourceNotFoundException();
        }
    }
    public String register(Reimbursements newReimbursements) {

        String baseSelect = " INSERT INTO project1.ers_reimbursements (reimb_id, amount, description, status_id, author_id, type_id) " +
                " VALUES (?, ?, ?, 99913, ?, ?) ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(baseSelect);
            pstmt.setString(1, newReimbursements.getReimb_id());
            pstmt.setFloat(2, newReimbursements.getAmount());
              pstmt.setString(3, newReimbursements.getDescription());
              pstmt.setString(4, newReimbursements.getAuthor_id());
            pstmt.setString(5, newReimbursements.getType_id());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            newReimbursements.setReimb_id(rs.getString("reimb_id"));



        } catch (SQLException e) {

        }


        return newReimbursements.getReimb_id();
    }

    private List<Reimbursements> mapResultSet(ResultSet rs) throws SQLException {

        List<Reimbursements> reimbursements = new ArrayList<>();

        while (rs.next()) {
            Reimbursements reimbursement = new Reimbursements();
            reimbursement.setReimb_id(rs.getString("reimb_id"));
            reimbursement.setAmount(rs.getInt("amount"));
            reimbursement.setSubmitted(LocalDateTime.parse(rs.getString("submitted")));
            reimbursement.setResolved(LocalDateTime.parse(rs.getString("resolved")));
            reimbursement.setDescription(rs.getString("description"));
            reimbursement.setPayment_id(rs.getString("payment_id"));
            reimbursement.setAuthor_id(rs.getString("author_id"));
            reimbursement.setResolved_id(rs.getString("resolved_id"));
            reimbursement.setStatus_id(rs.getString("status_id"));
            reimbursement.setType_id(rs.getString("type_id"));

        }

        return reimbursements;
    }


}