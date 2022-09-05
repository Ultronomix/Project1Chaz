package connection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppContext {
    private static boolean appRunning;
    private BufferedReader consoleReader;

    public AppContext() {

        appRunning = true;
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }
        public void startApp() {
            while(appRunning){
                try {
                    System.out.println("The app is running but will stop immediately.");
                    appRunning = false;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        public static void shutdown(){appRunning = false;}
    }

