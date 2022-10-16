package reimbursements;

import common.exceptions.DataSourceException;
import common.connection.ConnectionFactory;


import java.time.LocalDateTime;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;


public class ReimbursementsDAO {

    private static Logger logger = LogManager.getLogger(ReimbursementsDAO.class);

    private final String baseSelect = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, " +
            "er.description, er.author_id, er.resolved_id, " +
            "ers.status_id, ert.type_id " +
            "FROM project1.ers_reimbursements er " +
            "JOIN project1.ers_reimbursement_statuses ers ON er.status_id = ers.status " +
            "JOIN project1.ers_reimbursement_types ert ON er.type_id = ert.type_ ";


    public List<Reimbursements> getAllReimbs() {

        logger.info("Attempting to connect to the database at {}", LocalDateTime.now());

        List<Reimbursements> allReimbs = new ArrayList<>();

        String sqlGet = baseSelect + " WHERE er.resolved IS NOT NULL" +
                " AND er.resolved_id IS NOT NULL";

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


        String sql = baseSelect + "WHERE er.reimb_id = ? " +
                " AND er.resolved IS NOT NULL " +
                " AND er.resolved_id IS NOT NULL ";

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


    public Optional<Reimbursements> getReimbByStatus(String status_id) {

        logger.info("Attempting to connect to the database at {}", LocalDateTime.now());


        String sql = baseSelect + " WHERE er.status_id = ? " +
                "AND er.resolved IS NOT NULL " +
                "AND er.resolved_id IS NOT NULL ";


        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            logger.info("Reimbursement found by status at {}", LocalDateTime.now());

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status_id.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();


        } catch (SQLException e) {
            logger.warn("Unable to process reimbursement status search at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();
            throw new DataSourceException(e);
        }
    }





    public String register(Reimbursements newReimbursements) {
        logger.info("Attempting to persist new reimbursement at {}", LocalDateTime.now());

        String baseSelect = " INSERT INTO project1.ers_reimbursements (reimb_id, amount, author_id, description, status_id, type_id) " +
                " VALUES (?, ?, ?, ?, 99913,?) ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(baseSelect);
            pstmt.setString(1, newReimbursements.getReimb_id());
            pstmt.setFloat(2, newReimbursements.getAmount());
            pstmt.setString(3, newReimbursements.getAuthor_id());
            pstmt.setString(4, newReimbursements.getDescription());
            pstmt.setString(5, newReimbursements.getType_id());


            pstmt.executeUpdate();


        } catch (SQLException e) {

            logger.warn("Unable to persist data at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            e.printStackTrace();

        }


        return newReimbursements.getReimb_id();
    }

    private List<Reimbursements> mapResultSet(ResultSet rs) throws SQLException {

        List<Reimbursements> reimbursements = new ArrayList<>();

        while (rs.next()) {
            logger.info("Attempting to map the result set of reimbursement info at {}", LocalDateTime.now());

            Reimbursements reimbursement = new Reimbursements();
            reimbursement.setReimb_id(rs.getString("reimb_id"));
            reimbursement.setAmount(rs.getFloat("amount"));
            reimbursement.setDescription(rs.getString("description"));

            reimbursement.setAuthor_id(rs.getString("author_id"));
            reimbursement.setResolved_id(rs.getString("resolved_id"));
            reimbursement.setStatus_id(rs.getString("status_id"));
            reimbursement.setType_id(rs.getString("type_id"));


            reimbursements.add(reimbursement);
        }

        return reimbursements;
    }

    public void updateReimb(Reimbursements reimbursements) {
        logger.info("Attempting to update reimbursement info at {}", LocalDateTime.now());


        String sql = "UPDATE project1.ers_reimbursements " +
                "SET amount = ?, description = ?, type_id = ? " +
                "WHERE reimb_id = ? ";

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
            logger.warn("Unable to persist updated reimbursement at {}", LocalDateTime.now());

            throw new DataSourceException(e);
        }
    }
    public String approveDeny(String reimb_id, String resolved_id, String status_id) {
        String sql = "UPDATE project1.ers_reimbursements SET (resolved_id, status_id, resolved) = (?,?,?) WHERE reimb_id = ? ";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resolved_id);
            pstmt.setString(2, status_id);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(4, reimb_id);
            pstmt.executeUpdate();
            return "reimb changed"; //TODO change
        } catch (SQLException e) {
            //TODO log exception
            throw new DataSourceException(e);
        }
    }

}