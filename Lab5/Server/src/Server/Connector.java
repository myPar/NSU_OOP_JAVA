package Server;

import Message.ServerMessage;
import Message.ClientMessage;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Connector {
// inner static exception class
    public static class DataInputException extends Exception {
        public enum ExceptionType{SOCKET_EXCEPTION, COMMAND_TYPE_EXCEPTION, ARGUMENT_EXCEPTION}
        private ExceptionType type;
        private String description;

        DataInputException(ExceptionType t, String d) {
            type = t;
            description = d;
        }
        // getters:
        public ExceptionType getType() {
            return type;
        }

        public final String getDescription() {
            return description;
        }
    }
// fields:
    // opened socket
    private Socket clientSocket;
    // input and output socket streams
    private BufferedReader reader;
    private BufferedWriter writer;
    private ObjectInputStream serialReader;
    private ObjectOutputStream serialWriter;
    // message type (TEXT, XML, SERIALIZE)
    private Server.MessageType type;
// constructor:
    Connector(Server.MessageType type, Socket socket) {
        try {
            this.type = type;
            this.clientSocket = socket;

            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            // init serialize input output
            this.serialReader = new ObjectInputStream(clientSocket.getInputStream());
            this.serialWriter = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            // TODO write exception handling
        }
    }
// methods:    
    public ClientMessage getMessage() throws DataInputException{
        ClientMessage result = null;

        switch (type) {
            case SERIALISE:
                result = getSerializeMessage();
                break;
            case XML:
            case TEXT:
                result = parseMessage();
                break;
            default:
                assert false;
        }
        return result;
    }

    public void sendMessage(ServerMessage message) {
        switch (type) {
            case XML:
                sendXML(message);
                break;
            case SERIALISE:
                sendSerialize(message);
                break;
            case TEXT:
                sendText(message);
            default:
                assert false;
        }
    }
// other methods:
// send:
    // send serialize message method
    private void sendSerialize(ServerMessage message) {
        try {
            serialWriter.writeObject(message);
        } catch (IOException e) {
            // TODO handle exception
        }
    }

    // send XML message method
    private void sendXML(ServerMessage message) {
    }

    // send text message method
    private void sendText(ServerMessage message) {
    }
// receive:
    // get next serialize message method
    private ClientMessage getSerializeMessage() throws DataInputException {
        ClientMessage message;
        try {
            message = (ClientMessage) serialReader.readObject();
        }
        catch (Exception e) {
            throw new DataInputException(DataInputException.ExceptionType.SOCKET_EXCEPTION, "can't read serialize message");
        }
        return message;
    }
    // read next XML/TEXT message method
    private String[] readNextMessage() throws IOException {
        switch (type) {
            case TEXT:
                return reader.readLine().split(" ");
            case XML:
            default:
                assert false;
        }
        // TODO write other cases
        return null;
    }
    // check message tokens
    private ClientMessage parseMessage() throws DataInputException {
        try {
            String[] tokens = readNextMessage();
            assert tokens != null;
            assert tokens.length >= 1;

            String commandStr = tokens[0];
            ClientMessage.MessageType command;

            try {
                command = ClientMessage.MessageType.valueOf(commandStr);

                switch (command) {
                    case MESSAGE:
                        if (tokens.length != 3) {
                            throw new DataInputException(DataInputException.ExceptionType.ARGUMENT_EXCEPTION, "invalid args: " + command.name());
                        }
                    case LIST:
                    case LOGOUT:
                    case LOGIN:
                        if (tokens.length != 2) {
                            throw new DataInputException(DataInputException.ExceptionType.ARGUMENT_EXCEPTION, "invalid args: " + command.name());
                        }
                    default:
                        assert false;
                }
                // login command have no id
                if (command != ClientMessage.MessageType.LOGIN) {
                    try {
                        Integer.parseInt(tokens[1]);
                    }
                    catch (NumberFormatException e) {
                        throw new DataInputException(DataInputException.ExceptionType.ARGUMENT_EXCEPTION, "can't get id: " + command.name());
                    }
                }
            }
            catch (IllegalArgumentException e) {
                throw new DataInputException(DataInputException.ExceptionType.COMMAND_TYPE_EXCEPTION, "Invalid command type");
            }
            // return checked message:
            return new ClientMessage(command, Arrays.copyOfRange(tokens, 1, tokens.length));
        }
        catch (IOException e) {
            throw new DataInputException(DataInputException.ExceptionType.SOCKET_EXCEPTION, "Can't read data from socket");
        }
    }
    // get socket method
    public final Socket getClientSocket() {
        return clientSocket;
    }
// close socket method
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            //TODO write exception handling
        }
    }

}
