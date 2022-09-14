package shinobi;

import authorization.Credentials;
import authorization.AuthService;
import authorization.AuthServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import reimbursements.ReimbService;
import reimbursements.ReimbServlet;
import reimbursements.Reimbursements;
import reimbursements.ReimbursementsDAO;
import users.User;
import users.UserDAO;
import users.UserService;
import users.UserServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ShinobiApp {

    private static Logger logger = LogManager.getLogger(Reimbursements.class);

    public static void main(String[] args) throws LifecycleException {

        logger.info("Starting the Shinobi app");

        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();


        webServer.setBaseDir(docBase);
        webServer.setPort(5000); //
        webServer.getConnector();


        UserDAO userDAO = new UserDAO();
        ReimbursementsDAO reimbursementsDAO = new ReimbursementsDAO();
        AuthService authService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        ReimbService reimbService = new ReimbService(reimbursementsDAO);
        ObjectMapper jsonMapper = new ObjectMapper();
        UserServlet userServlet = new UserServlet(userService, jsonMapper);
        AuthServlet authServlet = new AuthServlet(authService, jsonMapper);
        ReimbServlet reimbServlet = new ReimbServlet(reimbService);


        final String rootContext = "/shinobi";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");
        webServer.addServlet(rootContext, "ReimbServlet", reimbServlet).addMapping("/reimb");


        webServer.start();

        logger.info("Welcome to the Hidden Leaf Reimbursements portal!");

        webServer.getServer().await();

    }
}


