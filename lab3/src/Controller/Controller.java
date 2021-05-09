package Controller;

import GUI.ControllerMessage;
import GUI.GUI;
import GameModel.GameModel;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class Controller {
// delta time constants
    private final long gameDeltaTime = 10000;
    private final long verticalMoveDeltaTime = 10000;
// message queue (from GUI)
    private Queue<ControllerMessage> messageQueue;
// constructor
    public Controller() {
        // queue is blocking (GUI thread adds messages, main game thread removes messages)
        messageQueue = new LinkedBlockingQueue<>(10);
    }
    public void runGame() throws InterruptedException {
        GameModel model = new GameModel(10, 20);
        GUI gameGUI = new GUI();
        long startTime = System.nanoTime();
        long curTime;

        while (true) {
            // get new message
            ControllerMessage message = getMessage();
            // update model
            updateModel(message, model);
            // get current time
            curTime = System.nanoTime();
            // if current delta time > verticalMoveDeltaTime: update y coordinate of figure
            if (curTime - startTime >= verticalMoveDeltaTime) {
                // move figure down
                boolean hasMoved = model.moveFigureDown();

                // check did figure moved down
                if (!hasMoved) {
                    // merge figure
                    model.mergeFigure();
                    // spawn new figure
                    model.spawn();
                }
                // update start time
                startTime = System.nanoTime();
            }
            // view graphic model
            gameGUI.viewGraphicModel(model.getGraphicMap(), model.getGraphicFigure());
            // wait gameDeltaTime in game thread
            sleep(gameDeltaTime);
        }
    }
// main update model method
    private void updateModel(ControllerMessage message, GameModel model) {
        assert model != null;
        // check empty message case
        if (message == null) {
            return;
        }
        // handle key events:
        if (message.getType() == ControllerMessage.MessageType.KEY_EVENT) {
            switch (message.getMessage()) {
                case MOVE_LEFT:
                    model.moveFigureLeft();
                    break;
                case MOVE_RIGHT:
                    model.moveFigureRight();
                    break;
                case ROTATE:
                    model.rotateFigure();
                    break;
                case ESC:
                    System.exit(0);
                    break;
                default:
                    assert false;
            }
        }
        else {
            // TODO: write mouse events case
        }
    }
    // add new message to queue (using by GUI)
    public void addMessage(ControllerMessage message) {
        assert message != null;
        messageQueue.add(message);
    }
    // get message from queue
    private ControllerMessage getMessage() {
        ControllerMessage message = null;

        if (!messageQueue.isEmpty()) {
            message = messageQueue.remove();
        }
        return message;
    }
}
