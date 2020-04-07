package ClientChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import Command.*;

public class ClientConnection {
    private UserData user = new UserData(null,null);
    private String serverName;
    private int serverPort;
    private boolean startAsServer = false;
    private boolean startAsClient = false;
    private final String END_SESSION_TAG = "/exit";
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;
    //private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    ClientConnection(String serverName, int serverPort) throws IOException {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    private void setIOStreams (Socket socketName) throws IOException {
        try {
            in = new ObjectInputStream(socketName.getInputStream());
            out = new ObjectOutputStream(socketName.getOutputStream());
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    // start as client
    public void startAsClient (){
        try {
            if (startAsServer) throw new IOException();
            this.clientSocket = new Socket(serverName, serverPort);
            System.out.println("Connected = " + !(clientSocket == null));
            startAsClient = true;
            setIOStreams(this.clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read message from connection on inputThread
    public Command readCommand () throws IOException {
        Command command = null;
        try {
            command = (Command) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return command;
    }

    // Send message to connection from outputThread
    public void sendCommand (Command command) throws IOException {
        try {
            if (clientSocket.isClosed()) {
                System.out.println("Connection is closed!");
                throw new IOException();
            }
            out.writeObject(command);
        } catch (IOException exc) {
            throw exc;
        }
    }

    public void setUserLogin (String login)
    {
        this.user.setLogin(login);
    }

    public void closeConnection() throws IOException {
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public String getUserLogin() {
        return this.user.getLogin();
    }

    public UserData getUser() {
        return this.user;
    }

    public String getUserName() {
        return user.getFullName();
    }

    public void setUserName(String userName) {
        user.setFullName(userName);
    }

    public void setUser(UserData newUser) {
        this.user = newUser;
    }

    public void setPassword(String password) {
        this.user.setPassword(password);
    }
}