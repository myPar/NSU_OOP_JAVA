package Message;

import java.awt.*;
import java.lang.reflect.Array;

// Server answer to client
public class ServerMessage {
    enum MessageType {RESPONSE, UPDATE_CHAT}
    enum Status {SUCCESS, FAIL, NO}
    enum Command {LOGIN, LIST, MESSAGE, LOGOUT, MESSAGE_CHAT, LOGIN_CHAT, LOGOUT_CHAT}

    private MessageType type;
    private Status status;
    private Command command;
    String[] args = {};
// constructors:
    // RESPONSE message constructor
    public ServerMessage(Status status, Command command, String[] data) {
        // SUCCESS or FAIL only
        assert status != Status.NO;
        // response message type
        this.type = MessageType.RESPONSE;

        if (status == Status.SUCCESS) {
            switch (command) {
                case MESSAGE:
                case LOGOUT:
                    // empty answer
                    assert data.length == 0;
                    break;
                case LIST:
                    // args is  user names
                    args = new String[data.length];
                    break;
                case LOGIN:
                    // arg is user unique id
                    assert data.length == 1;
                    assert UserMessage.isNumber(data[0]);
                    args = new String[1];
                    break;
                default:
                    // only responsible commands
                    assert false;
            }
        }
        // FAIL case
        else {
            // data: reason of the fail
            assert data.length == 1;
            args = new String[1];
        }
        // copy data to args
        UserMessage.copyData(data, args);
    }
// UPDATE_CHAT message constructor
    public ServerMessage(Command command, String[] data) {
        // update chat message (has no status)
        type = MessageType.UPDATE_CHAT;
        status = Status.NO;
        switch (command) {
            case LOGIN_CHAT:
            case LOGOUT_CHAT:
                // one argument: user name
                assert data.length == 1;
                args = new String[1];
                break;
            case MESSAGE_CHAT:
                // two args: user name, message
                assert data.length == 2;
                args = new String[2];
                break;
            default:
                // only chat messages
                assert false;
        }
        UserMessage.copyData(data, args);
    }
// getters:
    public final String[] getArgs() {
        return args;
    }

    public MessageType getType() {
        return type;
    }

    public Command getCommand() {
        return command;
    }

    public Status getStatus() {
        return status;
    }
}
