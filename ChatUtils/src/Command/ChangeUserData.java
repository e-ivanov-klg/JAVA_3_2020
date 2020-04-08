package Command;

import java.io.Serializable;

public class ChangeUserData implements Serializable {
    private UserData newUser;
    private UserData currentUser;

    ChangeUserData(UserData currentUser, UserData newUser) {
        this.newUser = newUser;
        this.currentUser = currentUser;
    }

    public UserData getNewUser() {
        return this.newUser;
    }

    public UserData getCurrentUser() {
        return this.currentUser;
    }
}
