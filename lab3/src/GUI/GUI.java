package GUI;

import Controller.Controller;
import GameModel.GameModel;
import GameModel.GraphicMap;
import GameModel.GraphicFigure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI {
    private JFrame gameFrame;
    private JPanel gamePanel;
    private myKeyListener gameFrameListener;
    private Controller controllerReference;

    private GraphicFigure figure;
    private GraphicMap map;

    private static final int cellSize = 20;
    private static final int gamePanelWidth = 500;
    private static final int gamePanelHeight = 700;

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

    private class myKeyListener implements KeyListener {
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
// constructor
    public GUI(Controller reference) {
        // init controller reference
        assert reference != null;
        controllerReference = reference;
        // init game frame
        gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(null);
        gameFrame.setSize(500, 700);
        // init keyListener
        gameFrameListener = new myKeyListener();
        // add listener to frame
        gameFrame.addKeyListener(gameFrameListener);
        // create game panel
        this.gamePanel = new MapPanel();
        gameFrame.add(gamePanel);
    }
// view GUI method
    public void viewGraphicModel(GraphicMap map, GraphicFigure figure) {
        this.map = map;
        this.figure = figure;
        // repaint game panel
        gamePanel.repaint();
        // view
        gameFrame.setVisible(true);
    }
}
