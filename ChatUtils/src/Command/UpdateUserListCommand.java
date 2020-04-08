package Command;

import java.io.Serializable;
import java.util.List;

public class UpdateUserListCommand implements Serializable {
    private List<UserData> usersList;

    public UpdateUserListCommand(List<UserData> userList){
        this.usersList = userList;
    }

    public void add (UserData user)
    {
        this.usersList.add(user);
    }

    public List<UserData> getUsersList ()
    {
        return this.usersList;
    }
}
