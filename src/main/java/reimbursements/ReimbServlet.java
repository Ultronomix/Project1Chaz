package reimbursements;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.ResourceCreationResponse;
import common.ErrorResponse;

import common.exceptions.*;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.ErrorResponse;
import users.UserResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import common.SecurityTools;
public class ReimbServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger(ReimbServlet.class);

    private final ReimbService reimbService;
    private final ObjectMapper jsonMapper;


    public ReimbServlet(ReimbService reimbService, ObjectMapper jsonMapper) {
        this.reimbService = reimbService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            logger.warn("User not logged in, attempted to access information at {}", LocalDateTime.now());

            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("loggedInUser");

        String reimb_idToSearchFor = req.getParameter("reimb_id");
        String status_idToSearchFor = req.getParameter("status_id");
        String type_idToSearchFor = req.getParameter("type_id");


        if ((!requester.getRole().equals("ADVISORS(FINANCE MANAGERS)"))) {
            logger.warn("Requester with invalid permissions attempted to view information at {}, {}", LocalDateTime.now(), requester.getUsername());

            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }


        try {
            logger.info("Iterating through list of reimbursements by id at {}", LocalDateTime.now());


            if (reimb_idToSearchFor != null) {

                ReimbursementsResponse foundRequest = reimbService.getReimbByReimb_id(reimb_idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundRequest));
                //! resp.getWriter().write("\nGet reimburse request by id");
            }
            if (status_idToSearchFor != null) {

                ReimbursementsResponse foundStatus_id = reimbService.getReimbByStatus_id(status_idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundStatus_id));
                //! resp.getWriter().write("\nGet reimburse by status");
            }
            if (type_idToSearchFor != null) {
                // TODO add log
                ReimbursementsResponse foundType_id = reimbService.getReimbByType_id(type_idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundType_id));
                //! resp.getWriter().write("\nGet reimburse by type");

        }
        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
            logger.warn("Unable to locate reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (ResourceNotFoundException e) {

            resp.setStatus(404);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
            logger.warn("Unable to locate reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (DataSourceException e) {

            resp.setStatus(500);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
            logger.warn("Unable to locate reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            logger.warn("User who is not logged in, attempted to access information at {}", LocalDateTime.now());

            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }


    UserResponse requester = (UserResponse) reimbSession.getAttribute("loggedInUser");

        if (!requester.getRole().equals("JONIN(EMPLOYEES)")) {
        logger.warn("Requester with invalid permissions attempted to register at {}", LocalDateTime.now());

        resp.setStatus(403);
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester is not permitted to communicate with this endpoint.")));
        return;
    }

        logger.info("Attempting to register a new reimbursement at {}", LocalDateTime.now());

        try {

        NewReimbursementRequest requestBody = jsonMapper.readValue(req.getInputStream(), NewReimbursementRequest.class);
        requestBody.setAuthor_id(requester.getUserId());
        ResourceCreationResponse responseBody = reimbService.create(requestBody);
        resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        logger.info("New reimbursement successfully persisted at {}", LocalDateTime.now());

    } catch (InvalidRequestException | JsonMappingException e) {

        resp.setStatus(400); // BAD REQUEST;
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        logger.warn("Unable to persist new reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

    } catch (ResourcePersistenceException e) {

        resp.setStatus(409); // CONFLICT; indicates that the provided resource could not be saved without conflicting with other data
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        logger.warn("Unable to persist new reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

    } catch (DataSourceException e) {

        resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        logger.warn("Unable to persist new reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

    }
}



    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/json");
        logger.info("Attempting to alter a reimbursement at {}", LocalDateTime.now());

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            logger.warn("User who is not logged in, attempted to access information at {}", LocalDateTime.now());

            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester not authenticated with server, log in")));
            return;


        }
        UserResponse requester = (UserResponse) reimbSession.getAttribute("loggedInUser");



        if ((!requester.getRole().equals("ADVISORS(FINANCE MANAGERS)"))) {
            logger.warn("Requester with invalid permissions attempted to update reimbursements at {}", LocalDateTime.now());

            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }


        try {

            UpdateReimbursementRequest requestPayload = jsonMapper.readValue(req.getInputStream(), UpdateReimbursementRequest.class);


                reimbService.updateReimb(requestPayload);
                logger.info("Reimbursement successfully updated at {}", LocalDateTime.now());
                resp.setStatus(204);

         } catch (InvalidRequestException | JsonMappingException e) {

        resp.setStatus(400);
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
            logger.warn("Unable to persist updated reimbursement status at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (AuthenticationException e) {

        resp.setStatus(409);
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
            logger.warn("Unable to persist updated reimbursement status at {}, error message: {}", LocalDateTime.now(), e.getMessage());

    } catch (DataSourceException e) {

        resp.setStatus(500);
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
            logger.warn("Unable to persist updated reimbursement status at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        }



    }
}
