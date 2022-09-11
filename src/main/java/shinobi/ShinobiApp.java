package shinobi;

import authorization.Credentials;
import authorization.AuthService;
import authorization.AuthServlet;
import users.User;
import users.UserDAO;
import users.UserService;
import users.UserServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.util.Optional;

public class ShinobiApp {

    public static void main(String[] args) throws LifecycleException {
        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();


        webServer.setBaseDir(docBase);
        webServer.setPort(5000); //
        webServer.getConnector();


        UserDAO userDAO = new UserDAO();
        AuthService authService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        UserServlet userServlet = new UserServlet(userService);
        AuthServlet authServlet = new AuthServlet(authService);


        final String rootContext = "/shinobi";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");


        webServer.start();
        webServer.getServer().await();

    }
}


