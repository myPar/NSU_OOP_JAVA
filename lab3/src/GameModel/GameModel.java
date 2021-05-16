package GameModel;

import java.io.InputStream;
import java.util.*;

public class GameModel implements GameController, GraphicContext {
// constants:
    private static final String figureConfigFileName = "figure_config_file";
    private static final int figureTypeCount = 7;
    private static final int figureColourCount = 4;
    private static final int pointsIncrement = 100;
    private static final int maxRecordsCount = 5;
// fields:
    // game map object
    private Map gameMap;
    // current falling figure object
    private Figure fallingFigure;
    // line destructor (points payer) object
    private LineDestructor lineDestructor;
    // top figure cell Y-coordinate
    private int topMapY;
    private int totalPoints;
    // record table
    private static TreeSet<Integer> records;
    // record table update flag
    private static boolean recordUpdated;
    // current game model state
    private State currentState;
    // game map repaint flag
    private boolean isNeedRepaintMap;
    private boolean isNeedRepaintFigure;
// static block (recordQueue initializing)
    static {
        records = new TreeSet<>();
        recordUpdated = false;
    }
// constructor
    public GameModel(int mapWidth, int mapHeight) {
        // create map
        gameMap = new Map(mapWidth, mapHeight);
        // no falling figure yet
        fallingFigure = null;
        // config figure class
        Figure.config(figureConfigFileName);
        // create line destructor
        lineDestructor = new LineDestructor();
        // topMapY is equal to map height - 1 at the start of the game
        topMapY = mapHeight;
        // no points yet
        totalPoints = 0;
        // default state - Game is running
        currentState = State.GAME_RUNNING;
        // set repaint flags in true:
        isNeedRepaintFigure = true;
        isNeedRepaintMap = true;
    }
// enums:
    public enum Colour {WHITE, RED, GREEN, BLUE, YELLOW}    // enum of colours
    public enum State {GAME_RUNNING, OPEN_RECORDS_PANEL, OPEN_ABOUT_PANEL}
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
        private static final int figureCellsNumber = 4; // figure consist from 4 cells
        private int type;   // figure type (idx in rotations array)
        private int dimension;  // cell array dimension
        private Colour colour;
        private int rotationState;  // current rotation state (from 0 to 4)
        private static boolean isConfigured = false; // flag of configuration of rotation state array

        // config method
        static void config(String configFileName) {
            InputStream dataStream = Figure.class.getResourceAsStream(configFileName);
            if (dataStream == null) {
                System.err.println("Fatal: Can't config Figure class, file is invalid");
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
            // Rotation state array has been configured
            isConfigured = true;
        }
        // constructor
        Figure(int type, Colour colour) {
            assert Figure.isConfigured;
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
            // update x coordinates for all cells
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
        // Collision checking methods:
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
            // update rotation
            rotationUpdate((rotationState + 1) % rotateStateNumber);
            // check collision
            for (Cell cell : figureCells) {
                int x = cell.x;
                int y = cell.y;
                // check is coordinates out of map bounds
                if (x < 0 || x >= gameMap.width || y >= gameMap.height) {
                    // update rotation state back
                    rotationUpdate(rotationState);

                    return true;
                }
                // check consistence of figure cell on current cell place
                if (gameMap.map[x][y].hasFigure) {
                    // update rotation state back
                    rotationUpdate(rotationState);

                    return true;
                }
            }
            // update rotation back
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
    // help class for points accrual and line's destructuring
    private class LineDestructor {
        // main method of points paying and line destructing
        void checkLines(int lowY, int highY) {
            int fullLineCount = 0;

            // check lines from lowY to highY (ATTENTION!!!! : highY < lowY because of Y coordinate orientation)
            for (int y = lowY; y >= highY; y--) {
                boolean isLineFull = true;

                // check if all cells in line has figure
                for (int x = 0; x < gameMap.width; x++) {
                    if (!gameMap.map[x][y].hasFigure) {
                        isLineFull = false;
                        break;
                    }
                }
                if (isLineFull) {
                    // increment full lines count
                    fullLineCount++;
                    // increment points
                    totalPoints += pointsIncrement;
                }
                else {
                    break;
                }
            }
            // remove full lines
            if (fullLineCount > 0) {
                // replace line lowY - i on line lowY - fullLineCount - i
                int moveLineCount = (lowY - fullLineCount + 1) - topMapY;

                for (int i = 0; i < moveLineCount; i++) {
                    replaceLine(lowY - fullLineCount - i,lowY - i);
                }
                // increment topMapY on number of full lines
                topMapY += fullLineCount;
            }
        }
        // cut srcY line and paste it on dstY line
        private void replaceLine(int srcY, int dstY) {
            for (int x = 0; x < gameMap.width; x++) {
                Cell srcCell = gameMap.map[x][srcY];
                Cell dstCell = gameMap.map[x][dstY];
                // copy fields values from src Cell to dst Cell:
                dstCell.hasFigure = srcCell.hasFigure;
                dstCell.colour = srcCell.colour;
                // clear fields values for src Cell
                srcCell.colour = Colour.WHITE;
                srcCell.hasFigure = false;
            }
        }
    }
// GameController methods implementation (methods which Controller called):
    // rotate figure
    @Override
    public void rotateFigure() {
        assert fallingFigure != null;
        // if no rotate collision - rotate
        if (!fallingFigure.hasRotationCollision(gameMap)) {
            fallingFigure.rotate();
        }
    }
    // move figure right
    @Override
    public void moveFigureRight() {
        assert fallingFigure != null;
        // if no right collision - move right
        if (!fallingFigure.hasRightCollision(gameMap)) {
            fallingFigure.moveRight();
        }
    }
    // move figure left
    @Override
    public void moveFigureLeft() {
        assert fallingFigure != null;
        // if no left collision - move left
        if (!fallingFigure.hasLeftCollision(gameMap)) {
            fallingFigure.moveLeft();
        }
    }
    @Override
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
    // spawn new Figure method (return 'true' if there was no collision and 'false' else)
    @Override
    public boolean spawn() {
        // spawn if figure has already fallen
        assert fallingFigure == null;
        // choose figure type
        Random rand = new Random();
        int type = Math.abs(rand.nextInt()) % figureTypeCount;
        // choose colour
        Colour colour = Colour.values()[(Math.abs(rand.nextInt()) % (figureColourCount)) + 1];
        Figure newFigure = new Figure(type, colour);
        // set cell coordinates:
        int startX = Math.abs(rand.nextInt()) % (gameMap.width - newFigure.dimension);
        int startY = 0;

        for (int y = 0; y < newFigure.dimension; y++) {
            for (int x = 0; x < newFigure.dimension; x++) {
                newFigure.cells[x][y].x = startX + x;
                newFigure.cells[x][y].y = startY + y;
            }
        }
        // update falling figure
        fallingFigure = newFigure;
        // check collision
        for (Cell cell: fallingFigure.figureCells) {
            int x = cell.x;
            int y = cell.y;

            if (gameMap.map[x][y].hasFigure) {
                // handled a collision - return false
                return false;
            }
        }
        // no collisions so can spawn new figure
        return true;
    }
    // merge fell figure with game map
    @Override
    public void mergeFigure() {
        assert  fallingFigure != null;
        int lowY = -1;
        int highY = 10000;

        for (Cell cell: fallingFigure.figureCells) {
            int x = cell.x;
            int y = cell.y;
            // update lowY
            if (lowY < y) {
                lowY = y;
            }
            // update highY
            if (highY > y) {
                highY = y;
            }
            // update topMapY
            if (topMapY > y) {
                topMapY = y;
            }
            // set hasFigure field in 'true' for each cells under figureCells
            gameMap.map[x][y].hasFigure = true;
            // set colour field on fallingFigure colour
            gameMap.map[x][y].colour = fallingFigure.colour;
        }
        // delete falling figure
        fallingFigure = null;
        lineDestructor.checkLines(lowY, highY);
    }
    // add total points to records
    public static void addRecord(GameModel model) {
        records.add(model.totalPoints);

        // check if tree set is overhead
        if (records.size() > maxRecordsCount) {
            // remove smallest record
            records.remove(records.last());
        }
        // set record update flag in 'true'
        recordUpdated = true;
    }
    // set new game model state
    @Override
    public void setModelState(State newState) {
        currentState = newState;
    }
    // get game state
    @Override
    public State getModelState() {
        return currentState;
    }
    // set repaint flags methods:
    @Override
    public void setMapRepaintFlag(boolean mapRepaintFlag) {
        isNeedRepaintMap = mapRepaintFlag;
    }
    @Override
    public void setFigureRepaintFlag(boolean figureRepaintFlag) {
        isNeedRepaintFigure = figureRepaintFlag;
    }
    // GraphicContext methods implementation (methods which GUI calls):
    @Override
    public GraphicMap getGraphicMap() {
        assert gameMap != null;

        return gameMap;
    }
    @Override
    public GraphicFigure getGraphicFigure() {
        assert fallingFigure != null;

        return fallingFigure;
    }
    @Override
    public int[] getRecordData() {
        int length = records.size();
        int[] result = new int[length];

        int idx = 0;
        Iterator<Integer> iterator = records.iterator();

        // copy tree set elements in sorted order
        while(iterator.hasNext()) {
            result[length - 1 - idx++] = iterator.next();
        }
        return result;
    }
    @Override
    public State getGameState() {
        return currentState;
    }
    @Override
    public boolean getMapRepaintFlag() {return isNeedRepaintMap;}
    @Override
    public boolean getFigureRepaintFlag() {return isNeedRepaintFigure;}
// get records count method
    public static int getMaxRecordsCount() {
        return maxRecordsCount;
    }
// get records updated flag method
    public static boolean isRecordUpdated() {
        return recordUpdated;
    }
//  reset record update method
    public static void resetRecordUpdate() {
        recordUpdated = false;
    }
}
