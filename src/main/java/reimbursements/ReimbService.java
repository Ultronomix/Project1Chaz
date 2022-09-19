package reimbursements;

import java.util.List;

import common.ResourceCreationResponse;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;


import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ReimbService {

    private static Logger logger = LogManager.getLogger(ReimbService.class);

    private final ReimbursementsDAO reimbDAO;

    public ReimbService(ReimbursementsDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    public List<ReimbursementsResponse> getAllReimbs() {

        return reimbDAO.getAllReimbs().stream()
                .map(ReimbursementsResponse::new)
                .collect(Collectors.toList());
    }



    public ReimbursementsResponse getReimbByReimb_id(String reimb_id) {


        if (reimb_id == null || reimb_id.trim().length() <= 0) {

            throw new InvalidRequestException("A user's id must be provided");
        }

            return reimbDAO.getReimbByReimbId(reimb_id)
                    .map(ReimbursementsResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

    }


    public void updateReimb(UpdateReimbursementRequest updateReimb) {


        Reimbursements reimbToUpdate = reimbDAO.getReimbByReimbId(updateReimb.getReimb_id())
                .orElseThrow(ResourceNotFoundException::new);

        if (updateReimb.getAmount() != 0) {
            reimbToUpdate.setAmount(updateReimb.getAmount());

        }
        if (updateReimb.getDescription() != null) {
            reimbToUpdate.setDescription(updateReimb.getDescription());

        }

        if (updateReimb.getType_id() != null) {
            reimbToUpdate.setType_id(updateReimb.getType_id());

        }
        reimbDAO.updateReimb(reimbToUpdate);
    }

    public ResourceCreationResponse create(NewReimbursementRequest newReimb) {

        if (newReimb == null) {
            throw new InvalidRequestException("Provided request payload was null.");

        }

        if (newReimb.getAmount() == 0) {
            throw new InvalidRequestException("Provided request payload was null.");

        }

        if (newReimb.getDescription() == null) {
            throw new InvalidRequestException("Provided request payload was null.");
        }


        if (newReimb.getType_id() == null) {
            throw new InvalidRequestException("Provided request payload was null.");

        }
        if (newReimb.getAuthor_id() == null) {
            throw new InvalidRequestException("Provided request payload was null.");
        }

        Reimbursements reimbToPersist = newReimb.extractEntity();
        String newReimbId = reimbDAO.register(reimbToPersist);
        return new ResourceCreationResponse(newReimbId);
    }
    public ResourceCreationResponse approveDeny(String reimb_id, String resolver_id, String status_id) {
        if (reimb_id == null || status_id == null || resolver_id == null) {
            throw new RuntimeException("Provided request payload was null.");
        }
        Status reimb_status = Reimbursements.getStatusFromName(status_id);
        // TODO: validate status
        String newApproveDeny = reimbDAO.approveDeny(reimb_id, resolver_id, status_id);
        return new ResourceCreationResponse(newApproveDeny);
    }

    public ReimbursementsResponse getReimbByStatus(String status_id) {

        if (status_id == null || status_id.length() <= 0) {
            throw new InvalidRequestException("A non-empty id must be provided!");
        }

        try {

            return reimbDAO.getReimbByStatus(status_id)
                    .map(ReimbursementsResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid UUID string was provided.");
        }
    }
}