package reimbursements;

import common.Request;

public class UpdateReimbursementRequest implements Request<Reimbursements> {

    private String reimb_id;
    private String status_id;
    private float amount;
    private String description;
    private String type_id;

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    @Override
    public String toString() {
        return "UpdateReimbursementRequest{" +
                "reimb_id='" + reimb_id + '\'' +
                ", status_id='" + status_id + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type_id='" + type_id + '\'' +
                '}';
    }

    @Override
    public Reimbursements extractEntity() {
        Reimbursements extractedEntity = new Reimbursements();
        extractedEntity.setReimb_id(this.reimb_id);
        extractedEntity.setStatus_id(this.status_id);
        extractedEntity.setAmount(this.amount);
        extractedEntity.setDescription(this.description);
        extractedEntity.setType_id(this.type_id);

        return extractedEntity;
    }


}