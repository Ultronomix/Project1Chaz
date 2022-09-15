package reimbursements;
import java.util.ArrayList;
import java.util.List;
import common.ResourceCreationResponse;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;
import common.exceptions.ResourcePersistenceException;

public class ReimbService {
    private final ReimbursementsDAO reimbDAO;

    public ReimbService(ReimbursementsDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    public List<ReimbursementsResponse> getAllReimbursements() {



        List<ReimbursementsResponse> result = new ArrayList<>();
        List<Reimbursements> reimbursements = reimbDAO.getAllReimbs();

        for (Reimbursements reimbursement : reimbursements) {
            result.add(new ReimbursementsResponse(reimbursements));
        }

        return result;

    }

    public ReimbursementsResponse getReimbById (String user_id) {


        if (user_id == null || user_id.trim().length() <= 0) {

            throw new InvalidRequestException("A user's id must be provided");
        }

        return reimbDAO.getReimbById(user_id).map(ReimbursementsResponse::new).orElseThrow(ResourceNotFoundException::new);


    }

    public List<ReimbursementsResponse> getReimbByStatus (String status) {

        // TODO add log
        if (status == null || (!status.toUpperCase().trim().equals("APPROVED")
                && !status.toUpperCase().trim().equals("PENDING")
                && !status.toUpperCase().trim().equals("DENIED"))) {
            // TODO add log
            throw new InvalidRequestException("Status cannot be empty. Enter 'Approved', 'Pending', " +
                    " or 'Denied'");
        }
        // TODO add log

        List<ReimbursementsResponse> result = new ArrayList<>();
        List<Reimbursements> reimbs = reimbDAO.getReimbByStatus(status);

        for (Reimbursements reimbursements : reimbs) {
            result.add(new ReimbursementsResponse(reimbs));
        }

        return result;

    }

    public List<ReimbursementsResponse> getReimbByType_ (String type_) {


        if (type_ == null || (!type_.toUpperCase().trim().equals("LODGING")
                && !type_.toUpperCase().trim().equals("TRAVEL")
                && !type_.toUpperCase().trim().equals("FOOD"))) {


            throw new InvalidRequestException("Type must be 'Lodging', 'Travel', or 'Food' ");

        }

        List<ReimbursementsResponse> result = new ArrayList<>();
        List<Reimbursements> reimbs = reimbDAO.getReimbByType(type_);

        for (Reimbursements reimbursements : reimbs) {
            result.add(new ReimbursementsResponse(reimbs));
        }

        return result;

    }
    public ResourceCreationResponse updateReimb (UpdateReimbursementRequest updateReimb, String reimb_id) {


        if (updateReimb == null) {

            throw new InvalidRequestException("Provide request payload");
        }

        if (!reimbDAO.isPending(reimb_id)) {
            // TODO add log
            throw new ResourcePersistenceException("Request is not pending.");
        }

        double newAmount = updateReimb.extractEntity().getAmount();
        String newDescription = updateReimb.extractEntity().getDescription();
        String newType = updateReimb.extractEntity().getType_();

        System.out.println(newAmount);

        if (newAmount > 0) {
            if (newAmount > 9999.99) {
                // TODO add log
                throw new InvalidRequestException("Amount must be below 10,000.");
            }

            reimbDAO.updateUserAmount(reimb_id, newAmount);

        }
        if (newDescription != null) {

            reimbDAO.updateUserDescription(reimb_id, newDescription);

        }
        if (newType != null) {
            if (!newType.toUpperCase().equals("LODGING") && !newType.toUpperCase().equals("TRAVEL")
                    && !newType.toUpperCase().equals("FOOD")) {
                // TODO add log
                throw new InvalidRequestException("Type must be 'Lodging', 'Travel', 'Food' " +
                        "or 'Other'");
            }
            if (newType.toUpperCase().equals("LODGING")) {
                newType = "200001";
            }
            if (newType.toUpperCase().equals("TRAVEL")) {
                newType = "200002";
            }
            if (newType.toUpperCase().equals("FOOD")) {
                newType = "200003";

            }

            reimbDAO.updateUserType(reimb_id, newType);
        }

        return new ResourceCreationResponse("Updated requests") ;
    }
}