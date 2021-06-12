package GUI;

import Client.Client;
import Message.ServerMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserGUI {
// enum:
    public enum ButtonType {LOGOUT, USER_LIST, SEND}
// Button listener:
    private static class ButtonListener implements ActionListener {
        private ButtonType type;
        private JTextField textEnterField;
        private Client client;

        ButtonListener(ButtonType type, JTextField field, Client client) {
            this.type = type;
            this.textEnterField = field;
            this.client = client;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (type == ButtonType.SEND) {
                // set current text to send
                client.setCurrentMessage(textEnterField.getText());
            }
            // add event to blocking event queue
            client.addEvent(type);
        }
    }
// fields:
    private Client client;
    // GUI fields
    private JFrame frame;
    private JPanel buttonPanel;
    private JPanel chatPanel;
    private JPanel inputPanel;
    // buttons:
    private JButton sendButton;
    private JButton logoutButton;
    private JButton listButton;
    //
    private JTextField input;
    // text
    private String chatText;
    private JTextArea chatTextArea;

    public UserGUI(Client client) {
        this.client = client;
        frame = new JFrame();
        frame.setSize(1280, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // panels:
        buttonPanel = new JPanel();
        chatPanel = new JPanel();
        inputPanel = new JPanel();

        frame.add(buttonPanel);
        frame.add(chatPanel);
        frame.add(inputPanel);
        frame.setBackground(Color.white);
        frame.setLayout(null);
        chatPanel.setBounds(0, 0, 800, 500);
        buttonPanel.setBounds(800, 0, 200, 500);
        inputPanel.setBounds(0, 500, 1000, 100);

        chatPanel.setBackground(Color.LIGHT_GRAY);
        inputPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBackground(Color.BLUE);

        input = new JTextField();
        input.setPreferredSize(new Dimension(500, 40));
        // create buttons:
        sendButton = new JButton("SEND");
        logoutButton = new JButton("LOGOUT");
        listButton = new JButton("USER LIST");
        // add action listeners:
        sendButton.addActionListener(new ButtonListener(ButtonType.SEND, input, client));
        logoutButton.addActionListener(new ButtonListener(ButtonType.LOGOUT, input, client));
        listButton.addActionListener(new ButtonListener(ButtonType.USER_LIST, input, client));

        sendButton.setPreferredSize(new Dimension(100, 50));
        logoutButton.setPreferredSize(new Dimension(100, 50));
        listButton.setPreferredSize(new Dimension(100, 50));

        chatTextArea = new JTextArea(20, 40);
        JScrollPane chat = new JScrollPane();
        chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chat.add(chatTextArea);

        buttonPanel.add(listButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(logoutButton);

        chatPanel.add(chat);
        inputPanel.add(input);

        frame.setVisible(true);
    }
    public void printMessage(ServerMessage message) {
        assert message.getType() == ServerMessage.MessageType.UPDATE_CHAT;

        String[] args = message.getArgs();
        String text = "";
        switch (message.getCommand()) {
            case LOGIN_CHAT:
                text = "<" + args[0] + ">" + ": JOINED\n\n";
                break;
            case LOGOUT_CHAT:
                text = "<" + args[0] + ">" + ": LEAVED\n\n";
                break;
            case MESSAGE_CHAT:
                text = args[0] + ": " + args[1] + "\n\n";
                break;
            default:
                assert false;
        }
        addText(text);
        chatTextArea.repaint();
    }
    private void addText(String text) {
        chatText += text;
        chatTextArea.setText(chatText);
    }
}
