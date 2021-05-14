package GameModel;

public interface GraphicFigure {
    GameModel.Colour getFigureColour();
    int getFigureCellCount();
    int getFigureCellX(int cellIdx);
    int getFigureCellY(int cellIdx);
}