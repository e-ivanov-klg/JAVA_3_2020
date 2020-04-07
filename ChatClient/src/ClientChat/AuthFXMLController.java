package ClientChat;

import Command.*;
import com.sun.deploy.net.proxy.ProxyUnavailableException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.security.cert.CertificateRevokedException;
import java.util.Timer;
import java.util.TimerTask;

public class AuthFXMLController {
    private int loginTimeout = 120;
    @FXML
    private Label connecTimeOutLabel;
    @FXML
    private TextField loginText;
    @FXML
    private  PasswordField passwordText;
    @FXML
    private AnchorPane loginPane;

    private ClientConnection clientConnection;
    private Stage loginStage;
    private Thread timerThread;

    public void init(ClientConnection clientConnection, Stage stage) {
        this.clientConnection = clientConnection;
        this.loginStage = stage;
        loginStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                timerThread = new Thread (()-> {
                    try {
                        startLoginTimer(connecTimeOutLabel);
                    } catch (InterruptedException e) {
                        return;
                    }
                });
                timerThread.start();
            }
        });
    }

    private void startLoginTimer(Label label) throws InterruptedException {
        while (loginTimeout > 0) {
            Thread.currentThread().sleep(1000);
            if (Thread.currentThread().isInterrupted()) return;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    label.setText("Осталось - " + loginTimeout + " сек.");
                }
            });
            loginTimeout --;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loginStage.close();
            }
        });
    }

    @FXML
    public void loginBtnOnPress(ActionEvent actionEvent) throws IOException {
        if (loginText.getLength() < 1 || passwordText.getLength() < 1) {
            showAlert("Неварный формат логина или пароля пользователя!");
            return;
        }
        System.out.println(Thread.currentThread().getName());
        System.out.println("Send AUTH mesaage!");
        clientConnection.sendCommand(Command.authCommand(new UserData(loginText.getText(), null, passwordText.getText())));
        while (true) {
            System.out.println("Wait (read) answer from server...");
            Command inCommand = clientConnection.readCommand();
            System.out.println("Server ansver - " + inCommand.getType());
            switch (inCommand.getType()) {
                case AUTH: {
                    AuthCommand command = (AuthCommand) inCommand.getData();
                    clientConnection.setUser(command.getUser());
                    clientConnection.setPassword(passwordText.getText());
                    timerThread.interrupt();
                    loginStage.close();
                    return;
                }
                case AUTH_ERROR: {
                    ErrorCommand command = (ErrorCommand) inCommand.getData();
                    showAlert(command.getErrorMessage());
                    return;
                }
            }
        }
    }

    @FXML
    public void ccancellBtnOnPress(ActionEvent actionEvent) {
        loginStage.close();
    }

    private void showAlert (String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Авторизация пользователя !");
        alert.setHeaderText("Ошибка авторизации.");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
