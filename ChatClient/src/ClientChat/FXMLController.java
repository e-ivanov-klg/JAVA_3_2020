package ClientChat;

import Command.*;
import com.sun.webkit.dom.MouseEventImpl;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class FXMLController {

    private ClientConnection clientConnection;

    @FXML
    private Button btn1;
    @FXML
    private ListView usersListView;
    @FXML
    private ListView dialogListView;
    @FXML
    private TextField messageTextBox;
    @FXML
    private Label userNameLabel;

    //private  Map<String, UserData> userList = new HashMap<>();
    // Map<login, userName>
    //FXCollections.observableArrayList();
    private Map<String, List<String>> userMessageList = new HashMap<>();
    // Map<login, message list>
    @FXML
    private void onSendButtonClick(ActionEvent actionEvent) throws IOException {
        int selectedIndex = usersListView.getFocusModel().getFocusedIndex();
        if ( selectedIndex == -1) { return; };
        if (messageTextBox.getText().length() == 0) { return; }
        UserData recipient = (UserData) usersListView.getItems().get(selectedIndex);
        String message = messageTextBox.getText();
        clientConnection.sendCommand(Command.privateMessage(recipient, clientConnection.getUser(), message));
        updateDialogList(recipient.getLogin(), clientConnection.getUserName(), message);
        System.out.println("Message is send !!!");
    }

    @FXML
    private void messageTextBoxKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onSendButtonClick(new ActionEvent());
        }
    }

    public void start(ClientConnection clConnection, Stage mainStage) throws IOException, InterruptedException {
        // Create and show login dialog
        Stage loginStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AuthForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        loginStage.setScene(scene);
        loginStage.setTitle("Авторизация в чат.");
        AuthFXMLController controller = loader.getController();
        this.clientConnection = clConnection;
        controller.init(clientConnection, loginStage);
        loginStage.initModality(Modality.APPLICATION_MODAL);
        System.out.println(Thread.currentThread().getName());
        loginStage.showAndWait();
        if (clientConnection.getUserLogin() == null) {
            mainStage.hide();
            return;
        }
        System.out.println("AfterLogin");
        userNameLabel.setText(clientConnection.getUserName());
        clientConnection.sendCommand(Command.updateUserList(null));
        Thread inThread = new Thread(()-> {
            try {
                readCommand();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        inThread.start();
    }

    private void readCommand() throws IOException {
        while (true){
            Command inCommand = clientConnection.readCommand();
            switch (inCommand.getType()) {
                case UPDATE_USER_LIST: {
                     updateUserList((UpdateUserListCommand) inCommand.getData());
                     break;
                }
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommand command = (PrivateMessageCommand)inCommand.getData();
                    updateDialogList(command.getSender().getLogin(), command.getSender().getFullName(), command.getMessage());
                    break;
                }
                case CHANGE_USER_DATA:{
                    ChangeUserData user = (ChangeUserData) inCommand.getData();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            userNameLabel.setText(user.getNewUser().getFullName());
                        }
                    });
                    changeUserList (user.getNewUser());
                    clientConnection.getUser().getCopy(user.getNewUser());
                    break;
                }
                case END: {
                    System.out.println("Server close client connection...");
                    clientConnection.closeConnection();
                    return;
                }
            }
        }
    }

    private void changeUserList(UserData newUser) {
        UserData user;
        for (int index = 0; index < usersListView.getItems().size(); index++) {
            user = (UserData) usersListView.getItems().get(index);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (user.equals(newUser)) usersListView.getItems().set(index, newUser);
                }
            });
            return;
        }
    }

    private void updateUserList(UpdateUserListCommand command) {
        List<UserData> usersList = command.getUsersList();
        for (UserData user : usersList) {
            if (!usersListView.getItems().contains(user)) {
                if (!user.equals(clientConnection.getUser())) {
                    usersListView.getItems().add(user);
                    if (!userMessageList.containsKey(user)) {
                        userMessageList.put(user.getLogin(), new ArrayList());
                    }
                }
            } else {
                changeUserList(user);
            }
        }
    }

    public void userListOnMouseClick(MouseEvent mouseEvent) {
        int selectedIndex = usersListView.getFocusModel().getFocusedIndex();
        System.out.println(selectedIndex);
        if ( selectedIndex == -1) { return; };
        UserData user = (UserData) usersListView.getItems().get(selectedIndex);
        dialogListView.setItems(FXCollections.observableArrayList(userMessageList.get(user.getLogin())));
    }

    private void updateDialogList(String recipient, String sender, String newMessage) {
        userMessageList.get(recipient).add(sender + ": " + newMessage);
        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {*/
                dialogListView.setItems(null);
                dialogListView.setItems(FXCollections.observableArrayList(userMessageList.get(recipient)));
            /*}
        });*/
    }

    public void onUserNameLabelClick(MouseEvent mouseEvent) {
        TextInputDialog dialog = new TextInputDialog("Input new user full name...");
        dialog.setTitle("Change user name.");
        dialog.setHeaderText("Enter your new name:");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            UserData newUser = new UserData();
            newUser.getCopy(clientConnection.getUser());
            try {
                newUser.setFullName(newName);
                clientConnection.sendCommand(Command.changeUserData(clientConnection.getUser(), newUser));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
