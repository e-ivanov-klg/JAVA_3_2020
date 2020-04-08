package ClientChat;

import Command.UserData;
import com.sun.prism.shader.DrawCircle_ImagePattern_AlphaTest_Loader;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.Date;

public class ChatFileStream implements Serializable {
    private final String separator = "=>";
    private File logFile;
    private static BufferedWriter out;
    private static BufferedReader in;

    public static void closeStream () throws IOException {
        out.close();
        in.close();
    }

    public ChatFileStream(String userLogin) throws IOException {
        this.logFile = new File(System.getProperty("user.dir") + "/chatlog/" + "history_" + userLogin + ".log");
        if (!this.logFile.exists()) {
            this.logFile.createNewFile();
        }
        out = new BufferedWriter(new FileWriter(logFile,true ));
        in = new BufferedReader(new FileReader(logFile));
    }

    public void AddMessageToLog(UserData recipient , String message) {
        Date curentDateTime = new Date();
        String outString = curentDateTime.toString() + separator + recipient.getLogin() + separator + recipient.getFullName()  + ": " + message + "\n";
        try {
            out.write(outString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLogLine() throws IOException {
        return in.readLine();
    }

    public void fillFile(ObservableList items) {
        UserData user;
        String message;
        for (Object listUser : items){
            user = (UserData) listUser;
            try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/chatlog/" + "demo.txt"))) {
                for (int i=0; i < 51; i++) {
                    message = reader.readLine();
                    AddMessageToLog(user, message);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            };
        }
    }
}
