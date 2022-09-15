package reimbursements;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ReimbursementsResponse implements Serializable {

    private String reimb_id;
    private double amount;
    private String submitted;
    private String resolved;
    private String description;
    private String payment_id;
    private LocalDateTime author_id; // ---> user_id
    private LocalDateTime resolved_id; // ---> user_id
    private String status_id; // ---> reimb statuses
    private String type_id; // ---> reimb types

    public ReimbursementsResponse (Reimbursements subject) {
        this.reimb_id = subject.getReimb_id();
        this.amount = subject.getAmount();
        this.submitted = subject.getSubmitted();
        this.resolved = subject.getResolved();
        this.description = subject.getDescription();
        this.payment_id = subject.getPayment_id();
        this.author_id = LocalDateTime.parse(subject.getAuthor_id());
        this.resolved_id = LocalDateTime.parse(subject.getResolved_id());
        this.status_id = subject.getStatus();
        this.type_id = subject.getType_();



    }

    public ReimbursementsResponse(List<Reimbursements> reimbursements) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReimbursementsResponse that = (ReimbursementsResponse) o;
        return amount == that.amount && Objects.equals(reimb_id, that.reimb_id) && Objects.equals(submitted, that.submitted) && Objects.equals(resolved, that.resolved) && Objects.equals(description, that.description) && Objects.equals(payment_id, that.payment_id) && Objects.equals(author_id, that.author_id) && Objects.equals(resolved_id, that.resolved_id) && Objects.equals(status_id, that.status_id) && Objects.equals(type_id, that.type_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimb_id, amount, submitted, resolved, description, payment_id, author_id, resolved_id, status_id, type_id);
    }

    @Override
    public String toString() {
        return "ReimbursementsResponse{" +
                "reimbId='" + reimb_id + '\'' +
                ", amount=" + amount +
                ", submitted='" + submitted + '\'' +
                ", resolved='" + resolved + '\'' +
                ", description='" + description + '\'' +
                ", payment_id='" + payment_id + '\'' +
                ", author_id='" + author_id + '\'' +
                ", resolver_id='" + resolved_id + '\'' +
                ", status_id='" + status_id + '\'' +
                ", type_id='" + type_id + '\'' +
                '}';
    }
}
