package reimbursements;

import common.Request;

import java.util.Objects;

public class UpdateReimbursementRequest implements Request<Reimbursements> {

    private String status;
    private double amount;
    private String description;
    private String type_;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_() {
        return type_;
    }

    public void setType_(String type_) {
        this.type_ = type_;
    }

    @Override
    public String toString() {
        return "UpdateReimbursementRequest{" +
                "status='" + status + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type_='" + type_ + '\'' +
                '}';
    }

    @Override
    public Reimbursements extractEntity() {
        Reimbursements extractedEntity = new Reimbursements();
        extractedEntity.setStatus(this.status);
        extractedEntity.setAmount(this.amount);
        extractedEntity.setDescription(this.description);
        extractedEntity.setType_(this.type_);

        return extractedEntity;
    }


}