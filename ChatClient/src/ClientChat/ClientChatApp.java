package ClientChat;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Command.Command;

import java.io.IOException;


public class ClientChatApp extends Application {
    private static ClientConnection clientConnection;
    public static void main(String[] args) throws IOException {
        clientConnection = new ClientConnection("localhost", 8189);
        clientConnection.startAsClient();
        launch(args);
    }
    @Override
    public void start(Stage mainStage) throws  Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ChatMainForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.setTitle("Мой первый сетевой чат.");
        mainStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    clientConnection.sendCommand(Command.endConnection());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        FXMLController controller = loader.getController();
        mainStage.show();
        controller.start(clientConnection, mainStage);
    }
}
