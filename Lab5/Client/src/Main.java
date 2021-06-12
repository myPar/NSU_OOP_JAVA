import Client.Client;
import GUI.UserGUI;

public class Main {
    public static void main(String[] args) {
        assert args.length == 1;

        String fileName = "./src/config";
        Client.config(fileName);
        Client client = new Client(args[0]);

        client.start();
    }
}
