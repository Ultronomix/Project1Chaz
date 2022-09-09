package common;

import users.UserResponse;

public class SecurityTools {


    public static boolean isDirector(UserResponse subject) {
        return subject.getRole().equals("HOKAGE(DIRECTOR)");
    }

    // Only to be used with GET user requests
    public static boolean requesterOwned(UserResponse subject, String resourceId) {
        return subject.getUserId().equals(resourceId);
    }
}

