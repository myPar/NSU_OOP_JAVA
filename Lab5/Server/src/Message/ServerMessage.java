package Message;

import java.io.Serializable;

// Server answer to client
public class ServerMessage implements Serializable {
// enums:
    public enum MessageType {RESPONSE, UPDATE_CHAT}
    public enum Status {SUCCESS, FAIL, NO}
    public enum Command {LOGIN, LIST, MESSAGE, LOGOUT, MESSAGE_CHAT, LOGIN_CHAT, LOGOUT_CHAT}
// fields:
    private MessageType type;
    private Status status;
    private Command command;
    private String[] args = {};
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
                    assert ClientMessage.isNumber(data[0]);
                    args = new String[1];
                    break;
                default:
                    // only responsible commands
                    assert false;
            }
        }
        // FAIL case
        else if (status == Status.FAIL){
            // data: reason of the fail
            assert data.length == 1;
            args = new String[1];
        }
        else {
            // no NO status expected
            assert false;
        }
        this.command = command;
        // copy data to args
        ClientMessage.copyData(data, args);
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
        this.command = command;
        ClientMessage.copyData(data, args);
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
