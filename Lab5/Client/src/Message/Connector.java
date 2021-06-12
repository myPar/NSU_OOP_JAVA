package Message;

import Client.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// implements reading/writing messages
public class Connector {
    // reader and writer
    private ObjectInputStream serialReader = null;
    private ObjectOutputStream serialWriter = null;
    // message types
    private Client.MessageType type;
    // opened socket
    private Socket clientSocket;

    public Connector(Client.MessageType type, Socket socket) {
        //try {
            this.type = type;
            this.clientSocket = socket;
         /*   this.serialReader = new ObjectInputStream(clientSocket.getInputStream());
            this.serialWriter = new ObjectOutputStream(clientSocket.getOutputStream());
        }
        catch (IOException e) {
            //TODO handle exception
            assert false;
        }

        */
    }
    // main get message method
    public ServerMessage getMessage() {
        ServerMessage result = null;
        try {
            if (serialReader == null) {
                this.serialReader = new ObjectInputStream(clientSocket.getInputStream());
            }

        switch (type) {
            case SERIALISE:
                result = getSerialize();
                break;
            case XML:
            case TEXT:
                //TODO write branch
                break;
            default:
                assert false;
        }
            //serialReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }
    // main send message method
    public void sendMessage(ClientMessage message) {

        try {
            if (serialWriter == null) {
                this.serialWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            }
            switch (type) {
                case XML:
                    //TODO sendXML(message);
                    break;
                case SERIALISE:
                    sendSerialize(message);
                    break;
                case TEXT:
                    //TODO sendText(message);
                default:
                    assert false;
            }
            //serialWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// other methods:
    // send serialize message method
    private void sendSerialize(ClientMessage message) {
        try {
            serialWriter.writeObject(message);
        } catch (IOException e) {
            // TODO handle exception
        }
    }
    // get serialize message
    private ServerMessage getSerialize() {
        ServerMessage message = null;
        try {
            message = (ServerMessage) serialReader.readObject();
        }
        catch (Exception e) {
            //TODO handle exception
            assert false;
        }
        return message;
    }

    public void close() {
        try {
            serialReader.close();
            serialWriter.close();
            clientSocket.close();
        } catch (IOException e) {
            //TODO write exception handling
        }
    }
}
