package server;

import java.io.IOException;
import java.sql.SQLException;

public class ServerApp {

    private static final int DEFAULT_PORT = 8189;

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        int port = getServerPort(args);
        new ChatServer(port).start();
    }

    private static int getServerPort(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный формат порта, будет использоваться порт по умолчанию");
            }
        }
        return port;
    }
}