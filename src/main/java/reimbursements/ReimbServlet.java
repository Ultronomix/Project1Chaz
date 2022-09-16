package reimbursements;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import common.ResourceCreationResponse;
import common.ErrorResponse;
import common.exceptions.AuthenticationException;
import common.exceptions.DataSourceException;
import common.exceptions.InvalidRequestException;
import common.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.ErrorResponse;
import users.UserResponse;
public class ReimbServlet extends HttpServlet {

    private final ReimbService reimbService;

    public ReimbServlet(ReimbService reimbService) {
        this.reimbService = reimbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {  //add logger
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("loggedInUser");

        String reimb_idToSearchFor = req.getParameter("reimb_id");
        String status_idToSearchFor = req.getParameter("status_id");
        String type_idToSearchFor = req.getParameter("type_id");


        if ((!requester.getRole().equals("HOKAGE(DIRECTOR)") && !requester.getRole().equals("ADVISORS(FINANCE MANAGERS)")) && !requester.getRole().equals("JONIN(EMPLOYEES)")) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }


        try {


            if (reimb_idToSearchFor != null) {

                ReimbursementsResponse foundRequest = reimbService.getReimbByReimb_id(reimb_idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundRequest));
                //! resp.getWriter().write("\nGet reimburse request by id");
            }
            if (status_idToSearchFor != null) {

                List<ReimbursementsResponse> foundStatus_id = reimbService.getReimbByStatus_id(status_idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundStatus_id));
                //! resp.getWriter().write("\nGet reimburse by status");
            }
            if (type_idToSearchFor != null) {
                // TODO add log
                List<ReimbursementsResponse> foundType_id = reimbService.getReimbByType_id(type_idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundType_id));
                //! resp.getWriter().write("\nGet reimburse by type");

        }
        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (ResourceNotFoundException e) {

            resp.setStatus(404);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
        } catch (DataSourceException e) {

            resp.setStatus(500);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }


        resp.getWriter().write("reimb working");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        resp.getWriter().write("reimb working ");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester not authenticated with server, log in")));
            return;
        }
        UserResponse requester = (UserResponse) reimbSession.getAttribute("loggedInUser");

        String userIdToSearchFor = req.getParameter("user_id");
        String reimb_idToSearchFor = req.getParameter("reimb_id");

        if ((!requester.getRole().equals("HOKAGE(DIRECTOR)") && !requester.getRole().equals("ADVISORS(FINANCE MANAGERS)"))
                && !requester.getRole().equals("JONIN(EMPLOYEES)")) {

            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
        ReimbursementsResponse foundReimb = reimbService.getReimbByReimb_id(userIdToSearchFor);
        resp.getWriter().write(jsonMapper.writeValueAsString(foundReimb));

        try {

            if (requester.getUserId().equals(userIdToSearchFor)) {


                ResourceCreationResponse responseBody =
                        reimbService.updateReimb(jsonMapper.readValue(req.getInputStream(), UpdateReimbursementRequest.class), reimb_idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

                return;
            }
            ResourceCreationResponse responseBody =
                    reimbService.updateReimb(jsonMapper.readValue(req.getInputStream(), UpdateReimbursementRequest.class), userIdToSearchFor);
         } catch (InvalidRequestException | JsonMappingException e) {

        resp.setStatus(400);
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
    } catch (AuthenticationException e) {

        resp.setStatus(409);
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
    } catch (DataSourceException e) {

        resp.setStatus(500);
        resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
    }


        resp.getWriter().write("still working on reimb update");
    }
}
