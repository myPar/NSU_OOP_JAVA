package GameModel;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class GameModel {
// constants:
    private static final String figureConfigFileName = "figure_config_file";
    private static final int figureTypeCount = 7;
    private static final int figureColourCount = 4;
// enums:
    enum Colour {WHITE, RED, GREEN, BLUE, YELLOW}
// static classes of Game Model: game Map, game cell, falling figure
    // Game map class
    private static class Map implements GraphicMap {
        private Cell[][] map;
        private int width;
        private int height;

        // constructor
        Map(int w, int h) {
            // init width and height
            width = w;
            height = h;
            // create map
            map = new Cell[w][h];
            // set cell coordinates
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    map[x][y] = new Cell();
                    Cell cell = map[x][y];
                    // init fields
                    cell.x = x;
                    cell.y = y;
                }
            }
        }
        // Graphic map Interface methods implementation:
        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public Colour getCellColour(int x, int y) {
            assert x >= 0 && x < width;
            assert y >= 0 && y < height;
            return map[x][y].colour;
        }
    }
    // Cell class
    private static class Cell {
        private int x;
        private int y;
        private Colour colour;
        private boolean hasFigure;

        // default constructor
        Cell() {
            colour = Colour.WHITE;
            hasFigure = false;
        }
    }
    // Figure class
    private static class Figure implements GraphicFigure {
        // fields:
        private Cell[][] cells; // cell array
        private Cell[] figureCells; // only figure cells
        private static int[][][][] rotations; // rotation states array
        private static final int rotateStateNumber = 4; // there is 4 rotation states
        private static final int figureCellsNumber = 4;
        private int type;   // figure type (idx in rotations array)
        private int dimension;  // cell array dimension
        private Colour colour;
        private int rotationState;

        // config method
        static void config(String configFileName) {
            InputStream dataStream = Figure.class.getResourceAsStream(configFileName);
            if (dataStream == null) {
                System.err.println("Fatal: Can't config Figure class");
                System.exit(1);
            }
            // create scanner
            Scanner sc = new Scanner(dataStream);
            int figureNumber = sc.nextInt();
            // init rotations array
            rotations = new int[figureNumber][][][];

            // init each figure rotation states
            for (int i = 0; i < figureNumber; i++) {
                rotations[i] = new int[rotateStateNumber][][];
                int dimension = sc.nextInt();

                // init each rotation state
                for (int j = 0; j < rotateStateNumber; j++) {
                    rotations[i][j] = new int[dimension][dimension];
                    int[][] rotationStateArr = rotations[i][j];

                    // fill rotation state array
                    for (int y = 0; y < dimension; y++) {
                        for (int x = 0; x < dimension; x++) {
                            rotationStateArr[x][y] = sc.nextInt();
                        }
                    }
                }
            }
        }
        // constructor
        Figure(int type, Colour colour) {
            assert type >= 0 && type < figureTypeCount;
            this.type = type;
            this.dimension = rotations[type][0].length;
            this.colour = colour;
            this.rotationState = 0;

            cells = new Cell[dimension][dimension];
            figureCells = new Cell[figureCellsNumber];
            // init coordinates (by default)
            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    cells[x][y] = new Cell();
                    Cell cell = cells[x][y];
                    // init fields
                    cell.x = x;
                    cell.y = y;
                    cell.colour = colour;
                }
            }
            // set 0's rotation state
            rotationUpdate(rotationState);
        }
        // move figure methods, change coordinates (without collision checking!!!!):
        void moveDown() {    // move figure down
            // update y coordinates for all cells
            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    cells[x][y].y++;
                }
            }
        }
        void moveRight() {   // move figure right method
            // update y coordinates for all cells
            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    cells[x][y].x++;
                }
            }
        }
        void moveLeft() {    // move figure left method
            // update y coordinates for all cells
            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    cells[x][y].x--;
                }
            }
        }
        void rotate() {      // rotate figure
            // change rotation state
            rotationState = (rotationState + 1) % rotateStateNumber;
            // rotate
            rotationUpdate(rotationState);
        }
        // Collision methods:
        boolean hasLeftCollision(Map gameMap) {          // check left side collision
            for (Cell cell : figureCells) {
                int x = cell.x;
                int y = cell.y;
                // check map wall
                if (x <= 0) {
                   return true;
                }
                // check figure cell:
                Cell mapCell = gameMap.map[x - 1][y];
                // if neighbour map cell is a figure cell - there is a collision
                if (mapCell.hasFigure) {
                    return true;
                }
            }
            return false;
        }
        boolean hasRightCollision(Map gameMap) {         // check right side collision
            for (Cell cell : figureCells) {
                int x = cell.x;
                int y = cell.y;
                // check map wall
                if (x >= gameMap.width - 1) {
                    return true;
                }
                // check figure cell:
                Cell mapCell = gameMap.map[x + 1][y];
                // if neighbour map cell is a figure cell - there is a collision
                if (mapCell.hasFigure) {
                    return true;
                }
            }
            return false;
        }
        boolean hasLovCollision(Map gameMap) {           // check lov side collision
            for (Cell cell : figureCells) {
                int x = cell.x;
                int y = cell.y;
                // check map wall
                if (y >= gameMap.height - 1) {
                    return true;
                }
                // check figure cell:
                Cell mapCell = gameMap.map[x][y + 1];
                // if neighbour map cell is a figure cell - there is a collision
                if (mapCell.hasFigure) {
                    return true;
                }
            }
            return false;
        }
        boolean hasRotationCollision(Map gameMap) {                 // check rotation collision
            // update rotation state
            rotationUpdate((rotationState + 1) % rotateStateNumber);
            // check collision
            for (Cell cell : figureCells) {
                int x = cell.x;
                int y = cell.y;

                // check consistence of figure cell on current cell place
                if (gameMap.map[x][y].hasFigure) {
                    // update rotation state back
                    rotationUpdate(rotationState);

                    return true;
                }
            }
            // update rotation state back
            rotationUpdate(rotationState);

            return false;
        }
        // updating Figure cells after rotation (doesn't update rotationState field !!!!)
        private void rotationUpdate(int rotationStateIdx) {
            int[][] rotationState = rotations[type][rotationStateIdx];
            int idx = 0;

            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    Cell cell = cells[x][y];

                    if (rotationState[x][y] == 1) {
                        // now this cell is a figure cell
                        cell.hasFigure = true;
                        cell.colour = this.colour;
                        // update figure cells
                        assert idx < 4;
                        figureCells[idx] = cell;
                        idx++;
                    }
                    else {
                        // set cell as default
                        cell.hasFigure = false;
                        cell.colour = Colour.WHITE;
                    }
                }
            }
        }
        // implemented GraphicFigure Interface methods
        @Override
        public Colour getFigureColour() {
            return colour;
        }

        @Override
        public int getFigureCellCount() {
            assert figureCells != null;

            return figureCells.length;
        }

        @Override
        public int getFigureCellX(int cellIdx) {
            assert figureCells != null;
            assert cellIdx >= 0 && cellIdx < figureCells.length;

            return figureCells[cellIdx].x;
        }

        @Override
        public int getFigureCellY(int cellIdx) {
            assert figureCells != null;
            assert cellIdx >= 0 && cellIdx < figureCells.length;

            return figureCells[cellIdx].y;
        }
    }
// fields:
    // game map object
    private Map gameMap;
    // current falling figure object
    private Figure fallingFigure;
// constructor
    public GameModel(int mapWidth, int mapHeight) {
        // create map
        gameMap = new Map(mapWidth, mapHeight);
        // no falling figure yet
        fallingFigure = null;
        // config figure class
        Figure.config(figureConfigFileName);
    }
// methods which Controller called
    // rotate figure
    public void rotateFigure() {
        assert fallingFigure != null;
        // if no rotate collision - rotate
        if (!fallingFigure.hasRotationCollision(gameMap)) {
            fallingFigure.rotate();
        }
    }
    // move figure right
    public void moveFigureRight() {
        assert fallingFigure != null;
        // if no right collision - move right
        if (!fallingFigure.hasRightCollision(gameMap)) {
            fallingFigure.moveRight();
        }
    }
    // move figure left
    public void moveFigureLeft() {
        assert fallingFigure != null;
        // if no left collision - move left
        if (!fallingFigure.hasLeftCollision(gameMap)) {
            fallingFigure.moveLeft();
        }
    }
    // move figure down
    public boolean moveFigureDown() {
        assert fallingFigure != null;
        if (!fallingFigure.hasLovCollision(gameMap)) {
            fallingFigure.moveDown();
            // figure moved down (send it to controller)
            return true;
        }
        else {
            // figure can't move down (send it to controller)
            return false;
        }
    }
    // spawn new Figure method
    public void spawn() {
        // spawn if figure has already fallen
        assert fallingFigure == null;
        // choose figure type
        Random rand = new Random();
        int type = rand.nextInt() % figureTypeCount;
        // choose colour
        Colour colour = Colour.values()[(rand.nextInt() % figureColourCount) + 1];
        Figure newFigure = new Figure(type, colour);
        // set cell coordinates:
        int startX = rand.nextInt() % (gameMap.width - newFigure.dimension);
        int startY = 0;

        for (int y = 0; y < newFigure.dimension; y++) {
            for (int x = 0; x < newFigure.dimension; x++) {
                newFigure.cells[x][y].x = startX + x;
                newFigure.cells[x][y].y = startY + y;
            }
        }
        // update falling figure
        fallingFigure = newFigure;
    }
    // merge fell figure with game map
    public void mergeFigure() {
        assert  fallingFigure != null;

        for (Cell cell: fallingFigure.figureCells) {
            int x = cell.x;
            int y = cell.y;
            // set hasFigure field in 'true' for each cells under figureCells
            gameMap.map[x][y].hasFigure = true;
            // set colour field on fallingFigure colour
            gameMap.map[x][y].colour = fallingFigure.colour;
        }
        // delete falling figure
        fallingFigure = null;
    }

// methods which GUI calls:
    public GraphicMap getGraphicMap() {
        assert gameMap != null;

        return gameMap;
    }
    public GraphicFigure getGraphicFigure() {
        assert fallingFigure != null;

        return fallingFigure;
    }
}
