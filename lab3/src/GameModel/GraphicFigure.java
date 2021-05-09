package GameModel;

public interface GraphicFigure {
    public abstract GameModel.GameModel.Colour getFigureColour();
    public abstract int getFigureCellCount();
    public abstract int getFigureCellX(int cellIdx);
    public abstract int getFigureCellY(int cellIdx);
}
