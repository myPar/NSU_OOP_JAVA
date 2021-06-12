import Server.Server;

public class Main {
    public static void main(String[] args) {
        String fileName = "./src/config";
        Server.configure(fileName);
        Server server = new Server();
        server.start();
    }
}
