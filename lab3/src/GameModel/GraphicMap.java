package GameModel;

public interface GraphicMap {
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract GameModel.GameModel.Colour getCellColour(int x, int y);
}
