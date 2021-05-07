package GameModel;

import java.io.InputStream;
import java.util.Scanner;

public class GameModel {
// constants:
    private static final String figureConfigFileName = "figure_config_file";
// enums:
    enum Colour {WHITE, RED, GREEN, BLUE, YELLOW}
// static classes of Game Model: game Map, game cell, falling figure
    // Game map class
    private static class Map {
        private Cell[][] map;
        private int width;
        private int height;

        Cell getCell(int x, int y) {
            assert x >= 0 && x < width;
            assert y >= 0 && y < height;

            return map[x][y];
        }
        // setters
        // replace cell on new one
        void setCell(Cell newCell) {
            int x = newCell.x;
            int y = newCell.y;

            map[x][y] = newCell;
        }
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
                    Cell cell = map[x][y];
                    cell.x = x;
                    cell.y = y;
                }
            }
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
    private static class Figure {
        // fields:
        private Cell[][] cells; // cell array
        private static int[][][][] rotations; // rotation states array
        private static final int rotateStateNumber = 4; // there is 4 rotation states
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
        // move figure methods, move if it's possible:
        private void moveDown(Map gameMap) {    // move figure down method

        }
        private void moveRight(Map gameMap) {   // move figure right method

        }
        private void moveLeft(Map gameMap) {    // move figure left method

        }
        // rotate method, returns true if it is possible to rotate
        private void rotate(Map gameMap) {

        }
        // check map collision method
        private void checkMapCollision() {

        }
        
        // updating Figure cells after rotation
        private void rotationUpdate(int rotationStateIdx) {
            int[][] rotationState = rotations[type][rotationStateIdx];

            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    Cell cell = cells[x][y];

                    if (rotationState[x][y] == 1) {
                        cell.hasFigure = true;
                        cell.colour = this.colour;
                    }
                    else {
                        cell.hasFigure = false;
                        cell.colour = Colour.WHITE;
                    }
                }
            }
        }
        // constructor
        Figure(int type, Colour colour) {
            assert type >= 1 && type <= 7;
            this.type = type;
            this.dimension = rotations[type][0].length;
            this.colour = colour;
            this.rotationState = 0;

            cells = new Cell[dimension][dimension];
            // init coordinates (by default)
            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    Cell cell = cells[x][y];
                    cell.x = x;
                    cell.y = y;
                }
            }
            // set 0's rotation state
            rotationUpdate(rotationState);
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
// spawn new Figure method
    private void spawn() {
        // spawn if figure has already fallen
        assert fallingFigure == null;
    }
// methods which Controller called
    public void rotateFigure() {
        assert fallingFigure != null;
        fallingFigure.rotate(gameMap);
    }
    public void moveFigureRight() {
        assert fallingFigure != null;
        fallingFigure.moveRight(gameMap);
    }
    public void moveFigureLeft() {
        assert fallingFigure != null;
        fallingFigure.moveLeft(gameMap);
    }
    public void moveFigureDown() {
        assert fallingFigure != null;
    }

}
