package authorization;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.exceptions.AuthenticationException;
import common.exceptions.DataSourceException;
import common.exceptions.InvalidRequestException;
import users.UserResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthServlet extends HttpServlet {

    private final AuthService authService;

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");


        try {

            Credentials credentials = jsonMapper.readValue(req.getInputStream(), Credentials.class);
            UserResponse responseBody = authService.authenticate(credentials);
            resp.setStatus(200);
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {


            resp.setStatus(400);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));

        } catch (AuthenticationException e) {

            resp.setStatus(401);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 401);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));

        } catch (DataSourceException e) {

            resp.setStatus(500);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));

        }

    }
}