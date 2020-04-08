package server;

import Command.Command;
import Command.UserData;
import Command.AuthCommand;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Autentification {
    private Map<String, String> users = new HashMap<String, String>();
    private ObjectInputStream in;
    private ObjectOutputStream out;

/*  Autentification () {
        users.put("ivanov", "123");
        users.put("petrov", "123");
        users.put("sidorov", "123");
    }*/

    public UserData startAuthentification(ClientConnection connection) throws IOException, SQLException, ClassNotFoundException {
        while (true) {
            Command command = connection.readCommand();
            switch (command.getType()) {
                case AUTH: {
                    AuthCommand commandData = (AuthCommand) command.getData();
                    UserData userName = ChatServer.getUserFromBase(commandData.getLogin(), commandData.getPasswod());
                    if (userName.getFullName() == null) {
                        Command authErrorCommand = Command.authErrorCommand("Пользователь не зарегистрирован в системе !");
                        connection.sendCommand(authErrorCommand);
                        continue;
                    } else {
                        return userName;
                    }
                }
                default:
                    System.err.println("Unknown type of command: " + command.getType());
            }
        }
    }

    private void sendMessage(String outMessage) throws IOException {
        out.writeUTF(outMessage);
    }

    public String getUserListString() {
        String userList = "/userlist=";
        for (String userName : users.keySet()) {
            userList = userList + userName + ",";
        }
        return userList.substring(0, userList.length() -1);
    }
}
