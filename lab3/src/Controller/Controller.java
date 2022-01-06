package Controller;

import GUI.ControllerMessage;
import GUI.GUI;
import GameModel.GameModel;
import GameModel.GameController;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class Controller {
// delta time constants
    private final long gameDeltaTime = 10;
    private final long verticalMoveDeltaTime = 500;
// message queue (from GUI)
    private Queue<ControllerMessage> messageQueue;
// constructor
    public Controller() {
        // queue is blocking (GUI thread adds messages, main game thread removes messages)
        messageQueue = new LinkedBlockingQueue<>(10);
    }
    // main game launcher method
    public void runGame() throws InterruptedException {
        // create GUI
        GUI gameGUI = new GUI(this);

        // main game loop
        while(true) {
            GameModel model = new GameModel(10, 20);
            runNewGame(gameGUI, model);
            GameModel.addRecord(model);
        }
    }
    // run new game session method
    private void runNewGame(GUI gameGUI, GameController model) throws InterruptedException {
        model.spawn();

        long startTime = System.currentTimeMillis();
        long curTime;

        while (true) {
            // get new message
            ControllerMessage message = getMessage();
            // if NEW_GAME message was handled - break
            if (message != null && message.getMessage() == ControllerMessage.Message.NEW_GAME) {
                break;
            }
            // update model
            updateModel(message, model);

            // while any not game panel is open handle messages
            while(model.getModelState() != GameModel.State.GAME_RUNNING) {
                gameGUI.viewGraphicModel((GameModel) model);
                // reset record update
                GameModel.resetRecordUpdate();
                // get new message
                message = getMessage();
                // update model
                updateModel(message, model);
            }
            // get current time
            curTime = System.currentTimeMillis();
            // if current delta time > verticalMoveDeltaTime: update y coordinate of figure
            if (curTime - startTime >= verticalMoveDeltaTime) {
                // move figure down
                boolean hasMoved = model.moveFigureDown();
                // need to repaint figure
                model.setFigureRepaintFlag(true);

                // check did figure moved down
                if (!hasMoved) {
                    // merge figure
                    model.mergeFigure();
                    // need to repaint map
                    model.setMapRepaintFlag(true);

                    // spawn new figure
                    if (!model.spawn()) {
                        // figure can't spawn, so game ended
                        break;
                    }
                }
                // update start time
                startTime = System.currentTimeMillis();
            }
            // view graphic model
            gameGUI.viewGraphicModel((GameModel) model);
            // reset repaint flags:
            model.setFigureRepaintFlag(false);
            model.setMapRepaintFlag(false);

            // wait gameDeltaTime in game thread
            sleep(gameDeltaTime);
        }
    }
// main update model method
    private void updateModel(ControllerMessage message, GameController model) {
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
                    model.setFigureRepaintFlag(true);
                    break;
                case MOVE_RIGHT:
                    model.moveFigureRight();
                    model.setFigureRepaintFlag(true);
                    break;
                case ROTATE:
                    model.rotateFigure();
                    model.setFigureRepaintFlag(true);
                    break;
                case ESC:
                    System.exit(0);
                case ABOUT:
                    if (model.getModelState() == GameModel.State.OPEN_ABOUT_PANEL) {
                        // exit from ABOUT panel
                        model.setModelState(GameModel.State.GAME_RUNNING);
                    }
                    else {
                        // open ABOUT panel
                        model.setModelState(GameModel.State.OPEN_ABOUT_PANEL);
                    }
                    break;
                case RECORDS:
                    if (model.getModelState() == GameModel.State.OPEN_RECORDS_PANEL) {
                        // exit from RECORDS panel
                        model.setModelState(GameModel.State.GAME_RUNNING);
                    }
                    else {
                        // open RECORDS panel
                        model.setModelState(GameModel.State.OPEN_RECORDS_PANEL);
                    }
                    break;
                default:
                    assert false;
            }
        }
        else {
            assert false;
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
