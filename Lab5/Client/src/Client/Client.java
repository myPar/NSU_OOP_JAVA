package Client;
import GUI.UserGUI;
import Message.ClientMessage;
import Message.Connector;
import Message.ServerMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
// enums:
    public enum MessageType{XML, SERIALISE, TEXT}
// Inner classes:
    // Handler of server messages, updates GUI if needed
    private class ServerMessageHandler extends Thread {
        @Override
        public void run() {
            while(true) {
                ServerMessage message = connector.getMessage();
                // RESPONSE messages:
                if (message.getType() == ServerMessage.MessageType.RESPONSE) {
                    System.out.println("Response on command: " + message.getCommand() + ": " + message.getStatus());

                    if (message.getCommand() == ServerMessage.Command.LIST) {
                        gui.addUserList(message.getArgs());
                    }
                    else if (message.getCommand() == ServerMessage.Command.LOGOUT) {
                        System.out.println("Client LOGOUT from Server");
                        System.exit(0);
                    }
                }
                // TO CHAT messages:
                else {
                    gui.printMessage(message);
                }
            }
        }
    }
    // Handles output messages
    private class OutputMessageHandler extends Thread {
        @Override
        public void run() {
            while(true) {
                try {
                    // blocking operation
                    UserGUI.ButtonType event = events.take();
                    switch (event) {
                        case LOGOUT: {
                            String[] args = new String[1];
                            args[0] = String.valueOf(id);
                            connector.sendMessage(new ClientMessage(ClientMessage.MessageType.LOGOUT, args));
                            break;
                        }
                        case SEND: {
                            if (currentMessage.equals("")) {
                                break;
                            }
                            String[] args = new String[2];
                            args[0] = String.valueOf(id);
                            args[1] = currentMessage;
                            connector.sendMessage(new ClientMessage(ClientMessage.MessageType.MESSAGE, args));
                            // reset message
                            currentMessage = "";
                            break;
                        }
                        case USER_LIST: {
                            String[] args = new String[1];
                            args[0] = String.valueOf(id);
                            connector.sendMessage(new ClientMessage(ClientMessage.MessageType.LIST, args));
                            break;
                        }
                        default:
                            assert false;
                    }

                } catch (InterruptedException e) {
                    System.err.println("Fatal error: exception while events handling");
                    System.exit(1);
                }
            }
        }
    }
    private Connector connector;
    // number of port
    private static int portNumber;
    // type XML/TEXT/SERIAL
    private static MessageType type;
    // unique client id
    private int id;
    // user name
    private String userName;
    // handles:
    private ServerMessageHandler serverMessageHandler;
    private OutputMessageHandler outputMessageHandler;
    // GUI
    private UserGUI gui;
    // button event queue
    private LinkedBlockingQueue<UserGUI.ButtonType> events;
    // current message to send
    private String currentMessage;
    // client config flag
    private static boolean isConfigured = false;
// constructor:
    public Client(String userName) {
        assert isConfigured;

        this.userName = userName;
        this.gui = new UserGUI(this);
        serverMessageHandler = new ServerMessageHandler();
        outputMessageHandler = new OutputMessageHandler();
        events = new LinkedBlockingQueue<>(10);
    }
// configure method
    public static void config(String configFileName) {
        isConfigured = true;
        File configFile = new File(configFileName);
        Scanner sc = null;

        try {
            sc = new Scanner(configFile);
        } catch (FileNotFoundException e) {
            System.err.println("can't configure client: config file not found");
            System.exit(1);
        }
        portNumber = Integer.parseInt(sc.nextLine());
        String type = sc.nextLine();

        // check port number
        if (portNumber < 0) {
            System.err.println("can't configure server: invalid port number: " + portNumber + ". Should be a non negative value");
            System.exit(1);
        }
        // check message type:
        switch (type) {
            case "XML":
                Client.type = MessageType.XML;
                break;
            case "SERIALISE":
                Client.type = MessageType.SERIALISE;
                break;
            case "TEXT":
                Client.type = MessageType.TEXT;
                break;
            default:
                System.err.println("can't configure server: invalid message type: " + type + ". should be 'XML' or 'SERIALISE' or 'TEXT'");
                System.exit(1);
        }
    }
// methods:
    // run Client method
    public void start() {
        // login
        if (!login(userName)) {
            System.exit(0);
        }
        // start handlers
        gui.viewGUI();
        serverMessageHandler.start();
        outputMessageHandler.start();
    }
    private boolean login(String userName) {
        try {
            // client socket
            Socket clientSocket = new Socket("localhost", portNumber);
            if (clientSocket.isConnected()) {
                System.out.println("Connected");
            }
            else {
                System.out.println("No connection");
            }
            Connector connector = new Connector(type, clientSocket);
            this.connector = connector;
            // send authentication data to server:
            String[] args = {userName};
            ClientMessage message = new ClientMessage(ClientMessage.MessageType.LOGIN, args);
            connector.sendMessage(message);
            // get response from server
            ServerMessage response = connector.getMessage();
            System.out.println(response.getCommand().toString());
            if (response.getStatus() == ServerMessage.Status.SUCCESS) {
                if (response.getCommand() == ServerMessage.Command.LOGIN) {
                    System.out.println("Login succeeded");
                    this.id = Integer.parseInt(response.getArgs()[0]);
                    // login succeed
                    return true;
                }
                else {
                    System.err.println("Failed response: invalid command type");
                    return false;
                }
            }
            else {
                System.err.println("Failed response: can't login to server");
                return false;
            }
        }
        catch (IOException e) {
            System.err.println("Socket error: can't login to server");
            return false;
        }
    }
// help methods
    private void printUsers(String[] args) {
        int length = args.length;
        System.out.println("User List:");
        int i = 1;

        for (String arg: args) {
            System.out.println(i + ". " + arg);
            i++;
        }
    }

    public Connector getConnector() {
        return connector;
    }

    public int getId() {
        return id;
    }

    public void setCurrentMessage(String text) {
        currentMessage = text;
    }
    public void addEvent(UserGUI.ButtonType event) {
        try {
            events.put(event);
        } catch (InterruptedException e) {
            System.err.println("Fatal error: exception while events handling");
        }
    }
}
