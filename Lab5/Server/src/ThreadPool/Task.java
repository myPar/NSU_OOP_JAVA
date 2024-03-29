package ThreadPool;

import Data.UserData;
import Data.UserDataStorage;
import Message.ClientMessage;
import Message.ServerMessage;
import Server.Connector;

public class Task implements Runnable{
    private UserData userData;
    private ThreadPool threadPool;
    private UserDataStorage storage;

    @Override
    public void run() {
        // check connection
        Connector userConnector = userData.getConnector();
        if (userConnector.getClientSocket().isConnected()) {
            ClientMessage message;
            try {
                message = userConnector.getMessage();
            }
            catch (Connector.DataInputException e) {
                storage.sendErrorMessage(userConnector, e.getType() + ":" + e.getDescription());
                return;
            }
            int id = userData.getId();
            String name = userData.getName();
            // check user id
            int idInMessage = Integer.parseInt(message.getArgs()[0]);

            if (id != idInMessage) {
                storage.sendErrorMessage(userConnector, "Id on server is not equal to id in message");
                return;
            }
            switch (message.getType()) {
                case MESSAGE:
                    // send message to chat using UserDataStorage method
                    String text = message.getArgs()[1];
                    // success response
                    storage.sendMessage(userConnector, ServerMessage.MessageType.RESPONSE, ServerMessage.Command.MESSAGE);
                    // send message to chat
                    storage.sendToAll(name, text, ServerMessage.Command.MESSAGE_CHAT);
                    // add task again
                    threadPool.addTask(new Task(userData, threadPool, storage));
                    break;
                case LIST:
                    // response user
                    storage.sendMessage(userConnector, ServerMessage.MessageType.RESPONSE, ServerMessage.Command.LIST);
                    // add task again
                    threadPool.addTask(new Task(userData, threadPool, storage));
                    break;
                case LOGOUT:
                    // response user
                    storage.sendMessage(userConnector, ServerMessage.MessageType.RESPONSE, ServerMessage.Command.LOGOUT);
                    // remove user from UserDataStorage
                    storage.removeUser(id);
                    // send to chat
                    storage.sendToAll(name,"", ServerMessage.Command.LOGOUT_CHAT);
                default:
                    // could not be a login message
                    assert false;
            }
        }
        else {
            // TODO write disconnecting by timeout
        }
    }
// constructor:
    // task handles input messages from user and calls UserDataStorage's methods
    public Task(UserData data, ThreadPool threadPool, UserDataStorage storage) {
        this.threadPool = threadPool;
        this.userData = data;
        this.storage = storage;
    }
}
