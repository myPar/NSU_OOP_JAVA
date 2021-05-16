package GameModel;

// Controller interface for Game Model
public interface GameController {
// move Figure methods:
    void rotateFigure();
    void moveFigureRight();
    void moveFigureLeft();
    boolean moveFigureDown();
    boolean spawn();
    void mergeFigure();
// set new model state method
    void setModelState(GameModel.State newState);
// get model state value method
    GameModel.State getModelState();
// set repaint flags method
    void setMapRepaintFlag(boolean mapRepaintFlag);
    void setFigureRepaintFlag(boolean figureRepaintFlag);
}
