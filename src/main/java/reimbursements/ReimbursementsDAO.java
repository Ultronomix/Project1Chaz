package reimbursements;
import common.exceptions.DataSourceException;
import common.connection.ConnectionFactory;
import users.User;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;


import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
        }

        return allReimbs;
    }


    public Optional<Reimbursements> getReimbById (String reimb_id) {

        // TODO add log
        String sqlId = baseSelect + "WHERE er.author_id = ? ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlId);
            pstmt.setString(1, reimb_id);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();
            // TODO add log
        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
}
    public List<Reimbursements> getReimbByStatus (String status) {

        // TODO add log
        String sqlStatus = baseSelect + "WHERE ers.status = ? ";
        List<Reimbursements> reimbsStatus = new  ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlStatus);
            pstmt.setString(1, status.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            reimbsStatus = mapResultSet(rs);

            return reimbsStatus;
            // TODO add log
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }
    }
    public List<Reimbursements> getReimbByType (String type) {

        // TODO add log
        String sqlType = baseSelect + "WHERE ert.type_ = ? ";
        List<Reimbursements> reimbsType = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlType);
            pstmt.setString(1, type.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            reimbsType = mapResultSet(rs);

            return reimbsType;
            // TODO add log
        } catch (Exception e) {
            // TODO add log
            throw new DataSourceException(e);
        }
    }

    public String updateRequestStatus (String status, String reimb_id, String resolver_id) {

        //TODO add log
        String updateSql = "UPDATE project1.ers_reimbursements SET status_id = ?, resolved = ?, resolved_id = ? WHERE reimb_id = ? ";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, status);
            pstmt.setString(2, LocalDateTime.now().format(format));
            pstmt.setString(3, resolver_id);
            pstmt.setString(4, reimb_id);

            // ResultSet rs =
            pstmt.executeUpdate();

            return "Updated status";
            //TODO add log
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }

    }
    public String newReimbRequest(Reimbursements reimbursements) {

        String baseSelect = " INSERT INTO project1.ers_reimbursements (reimb_id, amount, submitted, resolved, description, payment_id, author_id, resolved_id, status_id, type_id) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(baseSelect, new String[]{"reimb_id"});
            pstmt.setString(1, reimbursements.getReimb_id());
            pstmt.setString(2, String.valueOf(reimbursements.getAmount()));
            pstmt.setString(3, reimbursements.getSubmitted());
            pstmt.setString(4, reimbursements.getResolved());
            pstmt.setString(5, reimbursements.getDescription());
            pstmt.setString(6, reimbursements.getPayment_id());
            pstmt.setString(7, reimbursements.getAuthor_id());
            pstmt.setString(8, reimbursements.getResolved_id());
            pstmt.setString(9, reimbursements.getStatus_id());
            pstmt.setString(10, reimbursements.getType_id());


            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            reimbursements.setReimb_id(rs.getString("reimb_id"));

        } catch (SQLException e) {
            log("ERROR", e.getMessage());
        }

        log("INFO", "Successfully persisted new user with id: " + reimbursements.getReimb_id());

        return reimbursements.getReimb_id();
    }

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
            reimbursement.setAuthor_id("author_id");
            reimbursement.setResolved_id("resolver_id");
            reimbursement.setStatus_id("status_id");
            reimbursement.setType_id("type_id");

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


