package shinobi;

import authorization.Authorize;
import authorization.AuthServlet;
import users.UserDAO;
import users.UserServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class ShinobiApp {

    public static void main(String[] args) throws LifecycleException {
        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();


        webServer.setBaseDir(docBase);
        webServer.setPort(5000); //
        webServer.getConnector();


        UserDAO userDAO = new UserDAO();
        Authorize authService = new Authorize(userDAO);
        UserServlet userServlet = new UserServlet(userDAO);
        AuthServlet authServlet = new AuthServlet(authService);


        final String rootContext = "/shinobi";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");


        webServer.start();
        webServer.getServer().await();

    }

}




