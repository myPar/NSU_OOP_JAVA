package Data;

import Message.ServerMessage;
import Server.Connector;

import java.util.*;

public class UserDataStorage {
// map of pairs <User id: user data>
    private HashMap<Integer, UserData> userData;
// constructor:
    public UserDataStorage() {
        userData = new HashMap<>();
    }
// synchronized methods of interaction with user's data
    // send error response to user
    public synchronized void sendErrorMessage(Connector connector, String errorDescription) {
        // init args
        String[] args = new String[1];
        args[0] = errorDescription;
        // create error message (doesn't matter what command in fail status)
        ServerMessage errorMessage = new ServerMessage(ServerMessage.Status.FAIL, ServerMessage.Command.LIST, args);
        // send fail message
        connector.sendMessage(errorMessage);
    }
    // send reply from server to user
    public synchronized void sendMessage(Connector userConnector, ServerMessage.MessageType type, ServerMessage.Command command) {
        assert type == ServerMessage.MessageType.RESPONSE;
        // default empty args
        String[] args = {};
        ServerMessage message;

        switch (command) {
            case MESSAGE:
            case LOGOUT:
                break;
            case LIST:
                args = getUserList();
                break;
            default:
                assert false;
        }
        message = new ServerMessage(ServerMessage.Status.SUCCESS, command, args);
        userConnector.sendMessage(message);
    }
    // send message to chat
    public synchronized void sendToAll(String userName, String messageText) {
        Set<Integer> keySet = userData.keySet();

        String[] args = new String[2];
        args[0] = userName;
        args[1] = messageText;
        // create message
        ServerMessage message = new ServerMessage(ServerMessage.Command.MESSAGE_CHAT, args);

        // send message to all users
        for (int key: keySet) {
            Connector userConnector = userData.get(key).getConnector();
            userConnector.sendMessage(message);
        }
    }
    // remove User data by user id
    public synchronized void removeUser(int userId) {
        UserData user = userData.get(userId);
        // close connector
        user.getConnector().close();
        // remove user
        userData.remove(userId);
    }
    // add new User data to map
    public synchronized void addUser(UserData user) {
        userData.put(user.getId(), user);
    }
    // get user list (use inside synchronized context)
    private String[] getUserList() {
        Set<Integer> keySet = userData.keySet();
        LinkedList<String> names = new LinkedList<>();

        for (int key : keySet) {
            names.add(userData.get(key).getName());
        }
        String[] result = new String[names.size()];

        return names.toArray(result);
    }
}
