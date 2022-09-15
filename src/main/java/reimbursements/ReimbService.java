package reimbursements;
import java.util.ArrayList;
import java.util.List;
import common.ResourceCreationResponse;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;
public class ReimbService {
    private final ReimbursementsDAO reimbDAO;

    public ReimbService(ReimbursementsDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    public List<ReimbursementsResponse> getAllReimbursements() {


        // TODO add log
        List<ReimbursementsResponse> result = new ArrayList<>();
        List<Reimbursements> reimbursements = reimbDAO.getAllReimbs();

        for (Reimbursements reimbursement : reimbursements) {
            result.add(new ReimbursementsResponse(reimbursements));
        }

        return result;
        // TODO add log
    }

    public ReimbursementsResponse getReimbById (String reimb_id) {

        // TODO add log
        if (reimb_id == null || reimb_id.trim().length() <= 0) {
            // TODO add log
            throw new InvalidRequestException("A user's id must be provided");
        }

        return reimbDAO.getReimbById(reimb_id).map(ReimbursementsResponse::new).orElseThrow(ResourceNotFoundException::new);
        // TODO add logs

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
        // TODO add log
    }

    public List<ReimbursementsResponse> getReimbByType (String type) {

        // TODO add log
        if (type == null || (!type.toUpperCase().trim().equals("LODGING")
                && !type.toUpperCase().trim().equals("TRAVEL")
                && !type.toUpperCase().trim().equals("FOOD")
                && !type.toUpperCase().trim().equals("OTHER"))) {
            // TODO add log
            throw new InvalidRequestException("Type must be 'Lodging', 'Travel', " +
                    "'Food', or 'Other'");

        }

        List<ReimbursementsResponse> result = new ArrayList<>();
        List<Reimbursements> reimbs = reimbDAO.getReimbByType(type);

        for (Reimbursements reimbursements : reimbs) {
            result.add(new ReimbursementsResponse(reimbs));
        }

        return result;
        // TODO add log
    }
    public ResourceCreationResponse updateReimb (UpdateReimbursementRequest updateReimb, String userIdToSearchFor) {

        // TODO add log
        if (updateReimb == null) {
            // TODO add log
            throw new InvalidRequestException("Provide request payload");
        }

        String reimbToUpdate = updateReimb.extractEntity().getStatus_id();
        // TODO create update method
        return null;
    }

}
