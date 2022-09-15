package users;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import common.ErrorResponse;
import common.ResourceCreationResponse;
import common.exceptions.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static common.SecurityTools.isDirector;
import static common.SecurityTools.requesterOwned;


public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper jsonMapper;

    // TODO inject a shared reference to a configured ObjectMapper
    public UserServlet(UserService userService, ObjectMapper jsonMapper) {
        this.userService = userService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        // Access the HTTP session on the request (if it exists; otherwise it will be null)
        HttpSession userSession = req.getSession(false);

        // If userSession is null, this means that the requester is not authenticated with the server
        if (userSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with the system, please log in.")));
            return;
        }
        String userId = req.getParameter("user_id");
        String role = req.getParameter("role_");
        String username = req.getParameter("username");
        String email = req.getParameter("email");


        UserResponse requester = (UserResponse) userSession.getAttribute("loggedInUser");

        if (!isDirector(requester) && !requesterOwned(requester, userId)) {
            resp.setStatus(403); // FORBIDDEN; the system recognizes the user, but they do not have permission to be here
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester is not permitted to communicate with this endpoint.")));
            return;
        }


        try {


            if (userId != null) {
                UserResponse foundUser = userService.getUserByUserId(userId);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
                return;
            }

            if (username != null) {
                UserResponse foundUser = userService.getUserByUsername(username);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
                return;
            }


            if (email != null) {
                UserResponse foundUser = userService.getUserByEmail(email);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
                return;
            }


            List<UserResponse> allUsers = userService.getAllUsers();
            resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));

        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400); // BAD REQUEST
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));

        } catch (ResourceNotFoundException e) {

            resp.setStatus(404); // NOT FOUND; the sought resource could not be located
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));

        } catch (DataSourceException e) {

            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/json");

        try {

            NewUserRequest requestBody = jsonMapper.readValue(req.getInputStream(), NewUserRequest.class);
            ResourceCreationResponse responseBody = userService.register(requestBody);
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400); // BAD REQUEST
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));

        } catch (ResourcePersistenceException e) {

            resp.setStatus(409); // CONFLICT; indicates that the provided resource could not be saved without conflicting with other data
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));

        } catch (DataSourceException e) {
            e.printStackTrace();
            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));

        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        // Access the HTTP session on the request
        HttpSession userSession = req.getSession(false);

        // if null, this meanS that the requester is not authenticated with server
        if (userSession == null) {
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }

        UserResponse requester = (UserResponse) userSession.getAttribute("loggedInUser");

        // Only CEO and ADMIN access
        if (!requester.getRole().equals("HOKAGE(DIRECTOR)")){
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            userService.updateUser(jsonMapper.readValue(req.getInputStream(), UpdateUserRequest.class));
            resp.setStatus(204); // NO CONTENT

        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            resp.setStatus(409); // * conflict; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            e.printStackTrace();
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
        resp.getWriter().write("\nEmail is: " + requester.getEmail());


    }
}