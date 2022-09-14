package reimbursements;

import java.util.Objects;

public class UpdateReimbursementRequest {

    private String updateReimb;
    private String reimb_id;

    public String getUpdateReimb(){
        return updateReimb;

    }
    public void setUpdateReimb(String updateReimb) {
        this.updateReimb = updateReimb;
    }

    public String getReimb_id(){
        return reimb_id;
    }
    public void setReimb_id(String reimb_id){
        this.reimb_id = reimb_id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateReimbursementRequest that = (UpdateReimbursementRequest) o;
        return Objects.equals(updateReimb, that.updateReimb) && Objects.equals(reimb_id, that.reimb_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updateReimb, reimb_id);
    }

    @Override
    public String toString() {
        return "UpdateReimbursementRequest{" +
                "updateReimb='" + updateReimb + '\'' +
                ", reimbId='" + reimb_id + '\'' +
                '}';
    }
}
