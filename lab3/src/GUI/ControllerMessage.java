package GUI;

// Message class (sends from GUI to Controller)
public class ControllerMessage {
    public enum MessageType{MOUSE_EVENT, KEY_EVENT}
    public enum Message{MOVE_LEFT, MOVE_RIGHT, ROTATE, ESC, RECORDS, ABOUT, NEW_GAME}
    private MessageType type;
    private Message message;
// constructor
    public ControllerMessage(MessageType type, Message message) {
        this.type = type;
        this.message = message;
    }
// getters:
    public Message getMessage() {
        return message;
    }

    public MessageType getType() {
        return type;
    }
}
