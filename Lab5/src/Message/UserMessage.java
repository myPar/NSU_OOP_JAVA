package Message;

// Client message to server
public class UserMessage {
//enums:
    // user message types enum
    enum MessageType{LOGIN, LIST, MESSAGE, LOGOUT}
// fields:
    // message type
    private MessageType type;
    // message data
    private String[] args;
    // login: {user_name} list: {user_id} message: {user_id, message} logout: {user_id}
// constructor:
    public UserMessage(MessageType type, String[] data) {
        switch (type) {
            case LOGIN:
                // one arg: user name
                assert data.length == 1;

                args = new String[1];
                break;
            case MESSAGE:
                // two args: user id, message
                assert data.length == 2;
                assert isNumber(data[0]);

                args = new String[2];
                break;
            case LIST:
            case LOGOUT:
                // one arg: user id
                assert data.length == 1;
                assert isNumber(data[0]);

                args = new String[1];
            default:
                assert false;
        }
        copyData(data, args);
        this.type = type;
    }
// static methods:
    // need for id checking
    static boolean isNumber(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // copy string data from src to dst
    static void copyData(String[] src, String[] dst) {
        assert src != null;
        assert dst != null;

        int length = src.length;
        assert length == dst.length;

        for (int i = 0; i < length; i++) {
            dst[i] = src[i];
        }
    }
// getters:
    public MessageType getType() {
        return type;
    }

    public final String[] getArgs() {
        return args;
    }
}
