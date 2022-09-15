package reimbursements;

import common.Request;

public class NewReimbursementRequest implements Request<Reimbursements> {


    private String reimb_id;
private double amount;
private String submitted;
private String description;
private String payment_id;
private String author_id;

private String status;
private String type_;


public String getReimb_id() {
    return reimb_id;

}

public void setReimb_id(String reimb_id) {
    this.reimb_id = reimb_id;
}

public double getAmount(){
    return amount;
}

public void setAmount(double amount){
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

    public String getStatus() {
    return status;
    }

    public void setStatus(String status){
    this.status = status;
    }

    public String getType_() {
        return type_;
    }

    public void setType_(String type_) {
        this.type_ = type_;
    }

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "reimb_id='" + reimb_id + '\'' +
                ", amount=" + amount +
                ", submitted='" + submitted + '\'' +
                ", description='" + description + '\'' +
                ", payment_id='" + payment_id + '\'' +
                ", author_id='" + author_id + '\'' +
                ", status='" + status + '\'' +
                ", type_='" + type_ + '\'' +
                '}';
    }



    @Override
    public Reimbursements extractEntity() {
    Reimbursements extractEntity = new Reimbursements();
        extractEntity.setReimb_id(this.reimb_id);
        extractEntity.setAmount(this.amount);
        extractEntity.setSubmitted(this.submitted);
        extractEntity.setDescription(this.description);
        extractEntity.setPayment_id(this.payment_id);
        extractEntity.setAuthor_id(this.author_id);
        extractEntity.setStatus(this.status);
        extractEntity.setType_(this.type_);
        return null;
    }
}
