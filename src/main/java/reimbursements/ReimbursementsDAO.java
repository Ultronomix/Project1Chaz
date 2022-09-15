package reimbursements;
import common.exceptions.DataSourceException;
import common.connection.ConnectionFactory;
import common.exceptions.ResourceNotFoundException;
import users.User;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;


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

    private final String baseSelect = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, " +
            "er.description, er.payment_id, er.author_id, er.resolved_id, " +
            "ers.status, ert.type_ " +
            "FROM project1.ers_reimbursements er " +
            "JOIN project1.ers_reimbursement_statuses ers ON er.status_id = ers.status_id " +
            "JOIN project1.ers_reimbursement_types ert ON er.type_id = ert.type_id ";

    public List<Reimbursements> getAllReimbs() {

        List<Reimbursements> allReimbs = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = ((java.sql.Statement) stmt).executeQuery(baseSelect);

            allReimbs = mapResultSet(rs);

            return allReimbs;

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
            throw new DataSourceException(e);
        }

    }

    public Optional<Reimbursements> getReimbById (String reimb_id) {


        String sql = baseSelect + "WHERE er.author_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reimb_id);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }

    public Optional<Reimbursements> getReimbByReimbId (String reimbid) {


        String sql = baseSelect + "WHERE er.reimb_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reimbid);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
}
    public List<Reimbursements> getReimbByStatus (String status) {


        String sql = baseSelect + "WHERE ers.status = ? ";
        List<Reimbursements> reimbsStatus = new  ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            reimbsStatus = mapResultSet(rs);

            return reimbsStatus;

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }
    public List<Reimbursements> getReimbByType (String type) {


        String sql = baseSelect + "WHERE ert.type_ = ? ";
        List<Reimbursements> reimbsType = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            reimbsType = mapResultSet(rs);

            return reimbsType;

        } catch (Exception e) {

            throw new DataSourceException(e);
        }
    }

    public String updateRequestStatus (String status, String reimb_id, String resolver_id) {


        String sql = "UPDATE project1.ers_reimbursements SET status_id = ?, resolved = ?, resolved_id = ? WHERE reimb_id = ? ";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, LocalDateTime.now().format(format));
            pstmt.setString(3, resolver_id);
            pstmt.setString(4, reimb_id);

            // ResultSet rs =
            pstmt.executeUpdate();

            return "Updated status";

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }

    public String updateUserAmount (String reimbId, double newAmount) {


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

    public String updateUserDescription (String reimbId, String description) {


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


    public String updateUserType (String reimbId, String type_id) {


        String sql = "UPDATE project1.ers_reimbursements SET type_id = ? WHERE reimb_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type_id);
            pstmt.setString(2, reimbId);
            System.out.println(pstmt);
            pstmt.executeUpdate();

            return "Type ";
        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }



    public boolean isPending (String reimbId) {

        try {
            Optional<Reimbursements> reimb = getReimbByReimbId(reimbId);

            if (reimb.get().getStatus().equals("PENDING")) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {

            throw new ResourceNotFoundException();
        }
    }
//    public String newReimbRequest(Reimbursements reimbursements) {
//
//        String baseSelect = " INSERT INTO project1.ers_reimbursements (reimb_id, amount, submitted, resolved, description, payment_id, author_id, resolved_id, status_id, type_id) " +
//                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
//
//        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
//
//            PreparedStatement pstmt = conn.prepareStatement(baseSelect, new String[]{"reimb_id"});
//            pstmt.setString(1, reimbursements.getReimb_id());
//            pstmt.setString(2, String.valueOf(reimbursements.getAmount()));
//            pstmt.setString(3, reimbursements.getSubmitted());
//            pstmt.setString(4, reimbursements.getResolved());
//            pstmt.setString(5, reimbursements.getDescription());
//            pstmt.setString(6, reimbursements.getPayment_id());
//            pstmt.setString(7, reimbursements.getAuthor_id());
//            pstmt.setString(8, reimbursements.getResolved_id());
//            pstmt.setString(9, reimbursements.getStatus_id());
//            pstmt.setString(10, reimbursements.getType_id());
//
//
//            pstmt.executeUpdate();
//
//            ResultSet rs = pstmt.getGeneratedKeys();
//            rs.next();
//            reimbursements.setReimb_id(rs.getString("reimb_id"));
//
//        } catch (SQLException e) {
//            log("ERROR", e.getMessage());
//        }
//
//        log("INFO", "Successfully persisted new user with id: " + reimbursements.getReimb_id());
//
//        return reimbursements.getReimb_id();
//    }

    private List<Reimbursements> mapResultSet(ResultSet rs) throws SQLException {

        List<Reimbursements> reimbursements = new ArrayList<>();

        while (rs.next()) {
            Reimbursements reimbursement = new Reimbursements();
            reimbursement.setReimb_id(rs.getString("reimb_id"));
            reimbursement.setAmount(rs.getInt("amount"));
            reimbursement.setSubmitted(rs.getString("submitted"));
            reimbursement.setResolved(rs.getString("resolved"));
            reimbursement.setDescription(rs.getString("description"));
            reimbursement.setPayment_id(rs.getString("payment_id"));
            reimbursement.setAuthor_id(rs.getString("author_id"));
            reimbursement.setResolved_id(rs.getString("resolver_id"));
            reimbursement.setStatus(rs.getString("status_id"));
            reimbursement.setType_(rs.getString("type_id"));

        }

        return reimbursements;
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
}


