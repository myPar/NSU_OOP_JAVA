import Controller.Controller;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        try {
            controller.runGame();
        } catch (InterruptedException e) {
            System.out.println("Fatal: InterruptedException was thrown");
            e.printStackTrace();
        }
    }
}
