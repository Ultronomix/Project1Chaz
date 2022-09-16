package reimbursements;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import common.ResourceCreationResponse;
import common.connection.ConnectionFactory;
import common.exceptions.DataSourceException;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;
import common.exceptions.ResourcePersistenceException;

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
        try {
            return reimbDAO.getReimbByReimbId(reimb_id)
                    .map(ReimbursementsResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("an invalid reimbursement id was provided");
        }


    }

    public ReimbursementsResponse getReimbByStatus_id(String status_id) {

        // TODO add log
        if (status_id == null || status_id.length() <= 0) {
            throw new InvalidRequestException("a non empty id must be provided");
        }

        try {
            return reimbDAO.getReimbByStatus(status_id)
                    .map(ReimbursementsResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);


        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("an invalid status id was provided");


        }


    }

    public ReimbursementsResponse getReimbByType_id(String type_id) {


        if (type_id == null || (!type_id.toUpperCase().trim().equals("LODGING")
                && !type_id.toUpperCase().trim().equals("TRAVEL")
                && !type_id.toUpperCase().trim().equals("FOOD"))) {


            throw new InvalidRequestException("Type must be 'Lodging', 'Travel', or 'Food' ");

        }

        try {
            return reimbDAO.getReimbByType(type_id)
                    .map(ReimbursementsResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("an invalid type id was provided");

        }


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


        Reimbursements reimbToPersist = newReimb.extractEntity();
        String newReimbId = reimbDAO.register(reimbToPersist);
        return new ResourceCreationResponse(newReimbId);
    }

}