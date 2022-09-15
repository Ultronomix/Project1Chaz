package reimbursements;

import common.Request;

import java.util.Objects;

public class UpdateReimbursementRequest implements Request<Reimbursements> {

    private String status_id;

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }



    @Override
    public String toString() {
        return "UpdateReimbursementRequest [" +
                "status_id = '" + status_id + "'']";
    }

    @Override
    public Reimbursements extractEntity() {
        Reimbursements extractedEntity = new Reimbursements();
        extractedEntity.setStatus_id(this.status_id);
        return extractedEntity;
    }


}