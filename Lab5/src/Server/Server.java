package Server;

import ThreadPool.ThreadPool;

import javax.security.auth.callback.TextInputCallback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
// inner classes end enums:
    public enum MessageType{XML, SERIALISE}
    // Server.Socket handler class: handles events of sockets state update
    private class SocketHandler extends Thread {
        @Override
        public void run() {

        }
    }
    // Connection controller class: accepts newly connected sockets
    private class ConnectionController extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // accept connection
                    Socket socket = serverSocket.accept();

                } catch (IOException e) {
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
    private SocketMap socketMap;
    // server socket
    private ServerSocket serverSocket;
    // SocketHandler
    private SocketHandler socketHandler;
    // Connection controller
    private ConnectionController controller;
    // reference to ThreadPool
    private ThreadPool threadPool;
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
        socketMap = new SocketMap();
        socketHandler = new SocketHandler();
        controller = new ConnectionController();
        threadPool = new ThreadPool(maxClientsCount);
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
        if (type.equals("XML")) {
            Server.type = MessageType.XML;
        }
        else if (type.equals("SERIALISE")) {
            Server.type = MessageType.SERIALISE;
        }
        else {
            System.err.println("can't configure server: invalid message type: " + type + ". should be 'XML' or 'SERIALISE'");
            System.exit(1);
        }
        portNumber = port;
        maxClientsCount = maxCount;
    }
    // main server run method
    public void start() {
        assert isConfigured;

        controller.start();
        socketHandler.start();
    }
}
