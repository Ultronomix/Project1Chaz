package reimbursements;

import common.Request;

public class NewReimbursementRequest implements Request<Reimbursements> {


    private String reimbId;
private int amount;
private String submitted;
private String description;
private String payment_id;
private String author_id;
private String type_id;


public String getReimbId() {
    return reimbId;

}

public void setReimbId(String reimbId) {
    this.reimbId = reimbId;
}

public int getAmount(){
    return amount;
}

public void setAmount(int amount){
    this.amount = amount;

}

public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "reimbId='" + reimbId + '\'' +
                ", amount=" + amount +
                ", submitted='" + submitted + '\'' +
                ", description='" + description + '\'' +
                ", payment_id='" + payment_id + '\'' +
                ", author_id='" + author_id + '\'' +
                ", type_id='" + type_id + '\'' +
                '}';
    }

    @Override
    public Reimbursements extractEntity() {
    Reimbursements extractEntity = new Reimbursements();
        extractEntity.setReimb_id(this.reimbId);
        extractEntity.setAmount(this.amount);
        extractEntity.setSubmitted(this.submitted);
        extractEntity.setDescription(this.description);
        extractEntity.setPayment_id(this.payment_id);
        extractEntity.setAuthor_id(this.author_id);
        extractEntity.setType_id(this.type_id);
        return null;
    }
}
