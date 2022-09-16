package reimbursements;

import common.Request;

public class NewReimbursementRequest implements Request<Reimbursements> {

private float amount;

private String description;

private String author_id;


private String type_id;



public float getAmount(){
    return amount;
}

public void setAmount(float amount){
    this.amount = amount;

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


    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", author_id='" + author_id + '\'' +
                ", type_id='" + type_id + '\'' +
                '}';
    }

    @Override
    public Reimbursements extractEntity() {
    Reimbursements extractEntity = new Reimbursements();

        extractEntity.setAmount(this.amount);

        extractEntity.setDescription(this.description);

        extractEntity.setAuthor_id(this.author_id);

        extractEntity.setType_id(this.type_id);
        return extractEntity;
    }
}
