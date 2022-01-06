package GUI;

import Controller.Controller;
import GameModel.GameModel;
import GameModel.GraphicMap;
import GameModel.GraphicFigure;
import GameModel.GraphicContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI {
// current graphic context field:
private GraphicContext context;
// game Frame fields:
    private JFrame gameFrame;
    private JPanel gamePanel;
    private GameFrameListener gameFrameListener;
    private Controller controllerReference;

    private GraphicFigure figure;
    private GraphicMap map;

    private static final int cellSize = 20;
    private static final int gamePanelWidth = 500;
    private static final int gamePanelHeight = 700;
// 'Records' Frame fields:
    private JFrame recordFrame;
    private RecordFrameListener recordFrameListener;

    private static final int recordPanelWidth = 500;
    private static final int recordPanelHeight = 700;
    private static int rowHeight = 15;
    private static String[] columnNames = {"place", "record"};
    private static String[][] tableData;
    private JTable recordTable;
    // init default table data
    static {
        int recordsCount = GameModel.getMaxRecordsCount();
        tableData = new String[recordsCount][2];

        // init table data
        for (int i = 0 ; i < recordsCount; i++) {
            tableData[i][0] = Integer.toString(i + 1);
            tableData[i][1] = "no record";
        }
    }
// 'About' Frame fields:
    private JFrame aboutFrame;
    private AboutFrameListener aboutFrameListener;

    private static final int aboutPanelWidth = 500;
    private static final int aboutPanelHeight = 700;
    // text data
    private static String aboutText = "<html> Game Keys: <br>" +
                                        "'A' - move figure left <br>" +
                                        "'D' - move figure right <br>" +
                                        "'S' - rotate figure <br><br>" +
                                        "Option Keys: <br>" +
                                        "'R' - open/close 'record' panel <br>" +
                                        "'T' - open/close 'about' panel <br>" +
                                        "'N' - start new game <br>" +
                                        "'ESC' - exit game";
// Map panel class
    private class MapPanel extends JPanel {
        MapPanel() {
            setSize(gamePanelWidth, gamePanelHeight);
        }
        private void paintCell(int x, int y, GameModel.Colour colour, Graphics g) {
            // set colour
            switch (colour) {
                case WHITE:
                    g.setColor(Color.WHITE);
                    break;
                case RED:
                    g.setColor(Color.RED);
                    break;
                case BLUE:
                    g.setColor(Color.BLUE);
                    break;
                case GREEN:
                    g.setColor(Color.GREEN);
                    break;
                case YELLOW:
                    g.setColor(Color.YELLOW);
                    break;
                default:
                    assert false;
            }
            g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
            g.setColor(Color.BLACK);
            g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
        }
        private void paintMap(Graphics g) {
            int width = map.getWidth();
            int height = map.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    paintCell(x, y, map.getCellColour(x,y), g);
                }
            }
        }
        private void paintFigure(Graphics g) {
            int cellCount = figure.getFigureCellCount();

            for (int i = 0; i < cellCount; i++) {
                int x = figure.getFigureCellX(i);
                int y = figure.getFigureCellY(i);
                paintCell(x, y, figure.getFigureColour(), g);
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setBackground(Color.GRAY);

            paintMap(g);
            paintFigure(g);
        }
    }
// listeners classes:
    private class GameFrameListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {}

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int keyCode = keyEvent.getKeyCode();
            ControllerMessage message = null;

            switch (keyCode) {
                case KeyEvent.VK_A:
                    message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.MOVE_LEFT);
                    break;
                case KeyEvent.VK_D:
                    message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.MOVE_RIGHT);
                    break;
                case KeyEvent.VK_S:
                    message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.ROTATE);
                    break;
                case KeyEvent.VK_ESCAPE:
                    message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.ESC);
                    break;
                case KeyEvent.VK_R:
                    message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.RECORDS);
                    break;
                case KeyEvent.VK_T:
                    message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.ABOUT);
                    break;
                case KeyEvent.VK_N:
                    message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.NEW_GAME);
                    break;
                default:
            }
            // add not null message to controller message queue
            if (message != null) {
                controllerReference.addMessage(message);
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {}
    }
    private class RecordFrameListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {}

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int keyCode = keyEvent.getKeyCode();
            ControllerMessage message = null;

            if (keyCode == KeyEvent.VK_R) {
                message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.RECORDS);
            }
            // add not null message to controller message queue
            if (message != null) {
                controllerReference.addMessage(message);
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {}
    }
    private class AboutFrameListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {}

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int keyCode = keyEvent.getKeyCode();
            ControllerMessage message = null;

            if (keyCode == KeyEvent.VK_T) {
                message = new ControllerMessage(ControllerMessage.MessageType.KEY_EVENT, ControllerMessage.Message.ABOUT);
            }
            // add not null message to controller message queue
            if (message != null) {
                controllerReference.addMessage(message);
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {}
    }
// set records data to table
    private void setData(int[] data) {
        int dataSize = data.length;

        for (int i = 0; i < dataSize; i++) {
            recordTable.setValueAt(Integer.toString(data[i]), i, 1);
        }
        recordTable.repaint();
    }
// constructor
    public GUI(Controller reference) {
        // init controller reference
        assert reference != null;
        controllerReference = reference;
        // init 'game' frame:
        gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(null);
        gameFrame.setSize(500, 700);
        // init keyListener
        gameFrameListener = new GameFrameListener();
        // add listener to frame
        gameFrame.addKeyListener(gameFrameListener);
        // create game panel
        this.gamePanel = new MapPanel();
        gameFrame.add(gamePanel);

        // init 'about' frame:
        aboutFrame = new JFrame();
        aboutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aboutFrame.setLayout(null);
        aboutFrame.setSize(aboutPanelWidth, aboutPanelHeight);
        // init keyListener
        aboutFrameListener = new AboutFrameListener();
        // add key listener
        aboutFrame.addKeyListener(aboutFrameListener);
        // init text field
        JLabel textLabel = new JLabel();
        textLabel.setText(aboutText);
        JPanel panel = new JPanel();
        panel.add(textLabel);
        aboutFrame.setContentPane(panel);

        // init 'Records' frame:
        recordFrame = new JFrame();
        recordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recordFrame.setLayout(null);
        recordFrame.setSize(recordPanelWidth, recordPanelHeight);
        // init keyListener
        recordFrameListener = new RecordFrameListener();
        // add key listener
        recordFrame.addKeyListener(recordFrameListener);
        // init table field
        recordTable = new JTable(tableData, columnNames);
        Box contents = new Box(BoxLayout.Y_AXIS);
        // add listener to table
        recordTable.addKeyListener(recordFrameListener);
        contents.add(new JScrollPane(recordTable));
        recordFrame.setContentPane(contents);
    }
// view GUI method
    public void viewGraphicModel(GraphicContext context) {
        this.context = context;

        switch(context.getGameState()) {
            case GAME_RUNNING:
                this.map = context.getGraphicMap();
                this.figure = context.getGraphicFigure();
                // repaint game panel
                if (context.getMapRepaintFlag() || context.getFigureRepaintFlag()) {
                    gamePanel.repaint();
                }
                // view
                gameFrame.setVisible(true);
                recordFrame.setVisible(false);
                aboutFrame.setVisible(false);
                break;
            case OPEN_RECORDS_PANEL:
                if (GameModel.isRecordUpdated()) {
                    setData(context.getRecordData());
                }
                gameFrame.setVisible(false);
                recordFrame.setVisible(true);
                aboutFrame.setVisible(false);
                break;
            case OPEN_ABOUT_PANEL:
                gameFrame.setVisible(false);
                recordFrame.setVisible(false);
                aboutFrame.setVisible(true);
                break;
            default:
                assert false;
        }
    }
}
