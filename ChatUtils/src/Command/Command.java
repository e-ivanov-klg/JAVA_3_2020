package Command;

import Command.CommandType;
import com.sun.jmx.remote.internal.ClientCommunicatorAdmin;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Command implements Serializable {
    private CommandType type;
    private Object data;

    public static Command changeUserData(UserData currentUSer, UserData newUser) {
        Command command = new Command();
        command.type = CommandType.CHANGE_USER_DATA;
        command.data = new ChangeUserData(currentUSer, newUser);
        return command;
    }

    public CommandType getType() {
        return this.type;
    }

    public Object getData() {
        return this.data;
    }

    public static Command authCommand (UserData user) {
        Command command = new Command();
        command.type = CommandType.AUTH;
        command.data = new AuthCommand(user);
        return command;
    }

    public static Command authErrorCommand(String errorMessage){
        Command command = new Command();
        command.type = CommandType.AUTH_ERROR;
        command.data = new ErrorCommand(errorMessage);
        return command;
    }

    public static Command privateMessage(UserData recipient, UserData sender, String message){
        Command command = new Command();
        command.type = CommandType.PRIVATE_MESSAGE;
        command.data = new PrivateMessageCommand(recipient, sender, message);
        return command;
    }

    public static Command updateUserList (List<UserData> userList){
        Command command = new Command();
        command.type = CommandType.UPDATE_USER_LIST;
        command.data = new UpdateUserListCommand(userList);
        return command;
    }

    public  static Command endConnection (){
        Command command = new Command();
        command.type = CommandType.END;
        command.data = null;
        return command;
    }

    public static Command sensorErrorComand (){
        Command command = new Command();
        command.type = CommandType.CENSORE_ERROR;
        command.data = new ErrorCommand("Mesage content censored words !!!");
        return command;
    }
}
