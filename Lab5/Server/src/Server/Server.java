package Server;

import Data.UserData;
import Data.UserDataStorage;
import Message.ClientMessage;
import Message.ServerMessage;
import ThreadPool.ThreadPool;
import ThreadPool.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
// inner classes end enums:
    enum MessageType{XML, SERIALISE, TEXT}
    // Connection controller class: accepts newly connected sockets
    private class ConnectionController extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // accept connection (blocking method)
                    Socket socket = serverSocket.accept();
                    // create Connector obj with got socket
                    Connector connector = new Connector(type, socket);

                    // get message (Client accepts with login authentication data, so there is login data)
                    ClientMessage message = connector.getMessage();
                    // check message
                    if (message.getType() != ClientMessage.MessageType.LOGIN) {
                        // response fail
                        String[] args = {"Invalid message type: " + message.getType() +   ". LOGIN expected"};
                        ServerMessage failResponse = new ServerMessage(ServerMessage.Status.FAIL, ServerMessage.Command.LOGIN, args);
                        connector.sendMessage(failResponse);
                        // try accept next socket
                        continue;
                    }
                    // get User name
                    String userName = message.getArgs()[0];
                    // get new id
                    int id = getUniqueId();

                    // create User Data:
                    UserData data = new UserData(connector, id, userName);
                    // add user data to data storage
                    userDataStorage.addUser(data);
                    // add new Task to thread pool
                    threadPool.addTask(new Task(data, threadPool, userDataStorage));
                }
                catch (IOException e) {
                    System.err.println("ServerSocket exception: can't accept connection");
                    System.exit(1);
                }
            }
        }
    }
// static fields:
    // server configure flag
    private static boolean isConfigured = false;
    // server type (XML/Serialize message exchange)
    private static MessageType type;
    // max connected clients count
    private static int maxClientsCount;
    // port number
    private static int portNumber;
// non static fields:
    // locking thread-save map of pairs: <User_ID, Socket>
    private UserDataStorage userDataStorage;
    // server socket
    private ServerSocket serverSocket;
    // Connection controller
    private ConnectionController controller;
    // reference to ThreadPool
    private ThreadPool threadPool;
    // current max user id - 1
    private int maxId;
// constructor
    public Server() {
        assert isConfigured;
        // create server socket
        try {
            serverSocket = new ServerSocket(portNumber, maxClientsCount);
        } catch (IOException e) {
            System.err.println("Can't create a server on port: " + portNumber);
            System.exit(1);
        }
        // init other fields:
        userDataStorage = new UserDataStorage();
        controller = new ConnectionController();
        threadPool = new ThreadPool(maxClientsCount);
        maxId = -1;
    }
// other methods:
    // configure method
    public static void configure(String configFileName) {
        File configFile = new File(configFileName);
        Scanner sc = null;

        try {
            sc = new Scanner(configFile);
        } catch (FileNotFoundException e) {
            System.err.println("can't configure server: config file not found");
            System.exit(1);
        }
        // read port number
        int port = sc.nextInt();
        int maxCount = sc.nextInt();
        String type = sc.nextLine();
        // check port number:
        if (port < 0) {
            System.err.println("can't configure server: invalid port number: " + port + ". Should be a non negative value");
            System.exit(1);
        }
        // check max client count:
        if (maxCount <= 0) {
            System.err.println("can't configure server: invalid max client count: " + maxCount + ". Should be a positive value");
            System.exit(1);
        }
        // check message type:
        switch (type) {
            case "XML":
                Server.type = MessageType.XML;
                break;
            case "SERIALISE":
                Server.type = MessageType.SERIALISE;
                break;
            case "TEXT":
                Server.type = MessageType.TEXT;
                break;
            default:
                System.err.println("can't configure server: invalid message type: " + type + ". should be 'XML' or 'SERIALISE' or 'TEXT'");
                System.exit(1);
        }
        portNumber = port;
        maxClientsCount = maxCount;
    }
    // main server run method
    public void start() {
        assert isConfigured;

        controller.start();
    }
    // generate unique user id method
    private int getUniqueId() {
        return maxId++;
    }

}
