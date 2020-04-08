package Command;

import java.io.Serializable;

public class UserData implements Serializable {
    private String password;
    private String login;
    private String fullName;

    public UserData(String login, String fullName) {
        this.login = login;
        this.fullName = fullName;
    }

    public UserData(String login, String fullName, String password) {
        this.login = login;
        this.fullName = fullName;
        this.password = password;
    }

    public UserData() {
        this.login = null;
        this.fullName = null;
        this.password = null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    @Override
    public String toString() {
        return this.fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this. getClass() != o.getClass()) {return false;}
        UserData obj = (UserData) o;
        if (this.login.equals(obj.login)) {
            return true;
        } else return false;
    }

    public void getCopy(UserData copyUser)  {
        this.login = copyUser.login;
        this.fullName = copyUser.fullName;
        this.password = copyUser.password;
    }
}