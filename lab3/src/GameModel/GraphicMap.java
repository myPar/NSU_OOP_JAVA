package GameModel;

public interface GraphicMap {
    int getWidth();
    int getHeight();
    GameModel.Colour getCellColour(int x, int y);
}