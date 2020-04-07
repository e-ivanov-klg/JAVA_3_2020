package Command;

import java.io.Serializable;

public class AuthCommand implements Serializable {
    private UserData user;

    public AuthCommand(UserData user) {
        this.user = user;
    }

    public String getLogin() {
        return this.user.getLogin();
    }

    public String getPasswod()
    {
        return this.user.getPassword();
    }

    public String getUserName() {
        return this.user.getFullName();
    }

    public void setUserName(String userName) {
        this.user.setFullName(userName);
    }

    public UserData getUser(){
        return this.user;
    }
}
