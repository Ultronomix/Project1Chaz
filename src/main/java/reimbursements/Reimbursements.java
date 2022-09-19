package reimbursements;

import java.util.Objects;
import java.time.LocalDateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import common.connection.ConnectionFactory;
import common.exceptions.DataSourceException;
import common.exceptions.ResourceNotFoundException;

public class Reimbursements {
    private String reimb_id;
    private float amount;

    private LocalDateTime submitted;

    private LocalDateTime resolved;

    private String description;


    private String author_id;
    private String resolved_id;
    private String status_id;
    private String type_id;

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDateTime getSubmitted() {
        return submitted;
    }

    public void setSubmitted(LocalDateTime submitted) {
        this.submitted = submitted;
    }

    public LocalDateTime getResolved() {
        return resolved;
    }

    public void setResolved(LocalDateTime resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getResolved_id() {
        return resolved_id;
    }

    public void setResolved_id(String resolved_id) {
        this.resolved_id = resolved_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reimbursements that = (Reimbursements) o;
        return Float.compare(that.amount, amount) == 0 && Objects.equals(reimb_id, that.reimb_id) && Objects.equals(submitted, that.submitted) && Objects.equals(resolved, that.resolved) && Objects.equals(description, that.description) && Objects.equals(author_id, that.author_id) && Objects.equals(resolved_id, that.resolved_id) && Objects.equals(status_id, that.status_id) && Objects.equals(type_id, that.type_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimb_id, amount, submitted, resolved, description, author_id, resolved_id, status_id, type_id);
    }

    @Override
    public String toString() {
        return "Reimbursements{" +
                "reimb_id='" + reimb_id + '\'' +
                ", amount=" + amount +
                ", submitted=" + submitted +
                ", resolved=" + resolved +
                ", description='" + description + '\'' +
                ", author_id='" + author_id + '\'' +
                ", resolved_id='" + resolved_id + '\'' +
                ", status_id='" + status_id + '\'' +
                ", type_id='" + type_id + '\'' +
                '}';
    }


    final public static Status getStatusFromName(String status_name) {
        String sql = "SELECT status_id, status FROM project1.ers_reimbursement_statuses WHERE status = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, status_name);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {

                throw new ResourceNotFoundException();
            }
            return new Status(rs.getString("status_id"), rs.getString("status"));
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }
}
