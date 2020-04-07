package server;

import Command.UserData;
import com.sun.corba.se.impl.encoding.BufferManagerWrite;
import org.sqlite.jdbc3.JDBC3Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatServer {
    private static DBConnection dbConnection;
    private final int port;
    private Map<String, ClientConnection> ConnectionList = new HashMap<String, ClientConnection>();
    //login, connection handler

    ChatServer(int port ) throws SQLException, ClassNotFoundException {
        this.port = port;
         dbConnection =  new DBConnection();
    }

    public static UserData getUserFromBase(String login, String passwod) throws SQLException {
        return dbConnection.getUser(login, passwod);
    }

    public void start () throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен. Порт: " + port);
            while (true) {
                System.out.println("Ожидание подключения клиента...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен...");
                registerNewClient (clientSocket);
            }
        } catch (IOException exc) {

        }
    }

    private void registerNewClient(Socket clientSocket) throws IOException {
        ClientConnection newClient = new ClientConnection(this, clientSocket);
        newClient.run();
    }

    public synchronized void addClient (String login, ClientConnection newConnection) {
        ConnectionList.put (login, newConnection);
    }

    public synchronized void removeClient(ClientConnection clientConnection) {
        ConnectionList.remove(clientConnection.getUserLogin());
    }

    public synchronized ClientConnection getClient(String recipient) {
        return ConnectionList.get(recipient);
    }

    public List<UserData> getUserList(){
        List<UserData> userList = new ArrayList<>();
        for (String user : ConnectionList.keySet() ) {
            userList.add(new UserData(user, ConnectionList.get(user).getUserFullName()));
        }
        return userList;
    }

    public Map<String, ClientConnection> getUsersConnectionsList (){
        return this.ConnectionList;
    }

    public boolean changeUserData(UserData currentUser, UserData newUser) throws SQLException {
        if (dbConnection.changeUserData(currentUser, newUser)) {
            ConnectionList.get(currentUser.getLogin()).setUserFullName(newUser.getFullName());
            return true;
        }
        else return false;
    }
}
