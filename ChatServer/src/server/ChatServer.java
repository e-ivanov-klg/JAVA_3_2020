package server;

import Command.UserData;
import com.sun.corba.se.impl.encoding.BufferManagerWrite;
/*
import org.apache.logging.log4j.EventLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
*/
import org.sqlite.jdbc3.JDBC3Connection;

import javax.print.attribute.standard.Severity;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;
//import java.util.logging.Handler;


public class ChatServer {
    private static DBConnection dbConnection;
    private final int port;
    private Map<String, ClientConnection> ConnectionList = new HashMap<String, ClientConnection>();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final Logger serverLogger = Logger.getLogger(ChatServer.class.getName());
    private Handler logHandler;
    //login, connection handler

    ChatServer(int port ) throws SQLException, ClassNotFoundException, IOException {
        this.port = port;
        dbConnection =  new DBConnection();
        logHandler = new FileHandler(System.getProperty("user.dir") + "/chatserver.log");
        logHandler.setFormatter(new SimpleFormatter());
        serverLogger.addHandler(logHandler);
    }

    public static UserData getUserFromBase(String login, String passwod) throws SQLException {
        return dbConnection.getUser(login, passwod);
    }

    public void start () throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен. Порт: " + port);
            serverLogger.log(Level.SEVERE, "Сервер запущен. Порт: " + port);
            while (true) {
                System.out.println("Ожидание подключения клиента...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен...");
                serverLogger.log(Level.SEVERE, "Клиент подключен...");
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("Register new client..");
                            registerNewClient (clientSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private void registerNewClient(Socket clientSocket) throws IOException {
        System.out.println(Thread.currentThread().getName());
        ClientConnection newClient = new ClientConnection(this, clientSocket);
        newClient.run();
    }

    public synchronized void addClient (String login, ClientConnection newConnection) {
        ConnectionList.put (login, newConnection);
        serverLogger.log(Level.SEVERE, "Клиент прошел авторизацию. " +  "login - " + login);
    }

    public synchronized void removeClient(ClientConnection clientConnection) {
        ConnectionList.remove(clientConnection.getUserLogin());
    }

    public synchronized ClientConnection getClient(String recipient) {
        return ConnectionList.get(recipient);
    }

    public synchronized List<UserData> getUserList(){
        List<UserData> userList = new ArrayList<>();
        for (String user : ConnectionList.keySet() ) {
            userList.add(new UserData(user, ConnectionList.get(user).getUserFullName()));
        }
        return userList;
    }

    public synchronized Map<String, ClientConnection> getUsersConnectionsList (){
        return this.ConnectionList;
    }

    public synchronized boolean changeUserData(UserData currentUser, UserData newUser) throws SQLException {
        if (dbConnection.changeUserData(currentUser, newUser)) {
            ConnectionList.get(currentUser.getLogin()).setUserFullName(newUser.getFullName());
            return true;
        }
        else return false;
    }

    public boolean checkCensor(String message) {
        String stringLine;
        File censorFile = new File(System.getProperty("user.dir") + "/censor.list");
        try (BufferedReader reader = new BufferedReader(new FileReader(censorFile))) {
            while ((stringLine = reader. readLine()) != null) {
                if (message.indexOf(stringLine) != -1) {
                    return false;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        };
        return true;
    }
    public Logger getServerLogger() {
        return serverLogger;
    }

}
