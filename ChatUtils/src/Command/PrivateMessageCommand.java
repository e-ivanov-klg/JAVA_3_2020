package Command;

import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class PrivateMessageCommand implements Serializable {
    private final UserData recipient;
    private final UserData sender;
    private final String message;

    public PrivateMessageCommand(UserData recipient, UserData sender, String message) {
        this.recipient = recipient;
        this.sender = sender;
        this.message = message;
    }

    public UserData getRecipient()
    {
        return this.recipient;
    }

    public UserData getSender()
    {
        return this.sender;

    }

    public String getMessage()
    {
        return message;
    }
}
