package server;

import Command.Command;
import Command.PrivateMessageCommand;
import Command.UserData;
import server.Autentification;
import server.ChatServer;
import Command.ChangeUserData;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class ClientConnection {

    private ChatServer server;
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private UserData user;

    ClientConnection(ChatServer chatServer, Socket socket) throws IOException {
        this.clientSocket = socket;
        this.server = chatServer;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public String getUserLogin()
    {
        return this.user.getLogin();
    }

    public void run (){
        Thread clientThread = new Thread(()-> {
            try {
                if (!authNewClient()) {
                    System.out.println("Autentification failed !");
                    return;
                };
                //sendCommand(Command.updateUserList(server.getUserList()));
                readMessage();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }
    public Command readCommand () throws IOException{
        try {
            return (Command)in.readObject();
        } catch (ClassNotFoundException exc) {
            return null;
        }
    }

    public void sendCommand (Command command){
        try {
            out.writeObject(command);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private void readMessage() throws IOException, ClassNotFoundException, SQLException {
        while (true) {
            Command command = (Command) in.readObject();
            switch (command.getType()) {
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommand messageCommand = (PrivateMessageCommand) command.getData();
                    ClientConnection recipientConnection = server.getClient(messageCommand.getRecipient().getLogin());
                    if (recipientConnection == null) {
                        // replace sendComman (ERROR)
                        this.sendMessage("Адресат не подключен к серверу !!!");
                        continue;
                    }
                    recipientConnection.sendCommand(command);
                    break;
                }
                case UPDATE_USER_LIST: {
                    sendBroadcastMessage(Command.updateUserList(server.getUserList()));
                    break;
                }
                case AUTH: {
                    server.removeClient(this);
                    authNewClient();
                    break;
                }
                case CHANGE_USER_DATA: {
                    ChangeUserData changeUserDataCommand = (ChangeUserData) command.getData();
                    if (server.changeUserData (changeUserDataCommand.getCurrentUser(), changeUserDataCommand.getNewUser())){
                        sendCommand(Command.changeUserData(changeUserDataCommand.getCurrentUser(), changeUserDataCommand.getNewUser()));
                        sendBroadcastMessage(Command.updateUserList(server.getUserList()));
                    }
                    break;
                }
                case END: {
                    System.out.println("Clien close connection...");
                    sendCommand(Command.endConnection());
                    closeConnection();
                    server.removeClient(this);
                    return;
                }
            }
        }
    }

    private void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    private boolean authNewClient () throws IOException, SQLException, ClassNotFoundException {
        boolean result = false;
        UserData user = new Autentification().startAuthentification(this);
           if (user.getFullName() != null) {
               this.user = user;
               server.addClient(this.getUserLogin(), this);
               sendCommand(Command.authCommand(user));
               sendBroadcastMessage(Command.updateUserList(server.getUserList()));
               result = true;
           };
        return result;
    }

    public String getUserFullName() {
        return this.user.getFullName();
    }

    private void sendBroadcastMessage (Command outCommand){
        Map<String,ClientConnection> connections = server.getUsersConnectionsList();
        for (ClientConnection userConnection : connections.values()) {
            userConnection.sendCommand(outCommand);
        }
    }

    public void setUser(UserData newUser) {
        this.user = newUser;
    }

    public void setUserFullName(String fullName) {
        this.user.setFullName(fullName);
    }

    public void closeConnection() throws IOException {
        try {
            if (clientSocket != null) clientSocket.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
