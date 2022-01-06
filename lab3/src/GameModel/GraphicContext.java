package GameModel;

public interface GraphicContext {
// get map graphic context method
    GraphicMap getGraphicMap();
// get figure graphic context method
    GraphicFigure getGraphicFigure();
// get game state method
    GameModel.State getGameState();
// get records data
    int[] getRecordData();
// repaint flags getters
    boolean getMapRepaintFlag();
    boolean getFigureRepaintFlag();
}
