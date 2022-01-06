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
    private JScrollPane chatPanel;
    private JScrollPane userListPanel;
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
    private JTextArea userListTextArea;
    private String userList;

    public UserGUI(Client client) {
        this.client = client;
        chatText = "";

        frame = new JFrame();
        frame.setSize(1280, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // panels:
        buttonPanel = new JPanel();
        chatPanel = new JScrollPane();
        inputPanel = new JPanel();
        //userListPanel = new JScrollPane();

        frame.add(buttonPanel);
        frame.add(chatPanel);
        frame.add(inputPanel);
        //frame.add(userListPanel);

        frame.setBackground(Color.white);
        frame.setLayout(null);

        chatPanel.setBounds(0, 0, 800, 500);
        buttonPanel.setBounds(800, 0, 200, 500);
        inputPanel.setBounds(0, 500, 1000, 100);
        //userListPanel.setBounds(1020, 0, 200, 600);

        chatPanel.setBackground(Color.LIGHT_GRAY);
        inputPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBackground(Color.BLUE);
        //userListPanel.setBackground(Color.LIGHT_GRAY);

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

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        chatTextArea.setBounds(20, 20, 760, 460);
        chatTextArea.setWrapStyleWord(true);

        chatPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatPanel.add(chatTextArea);

        userListTextArea = new JTextArea();
        userListTextArea.setBounds(1020, 0, 200, 600);
        userListTextArea.setEditable(false);
        userListTextArea.setLineWrap(true);
        userListTextArea.setBackground(Color.LIGHT_GRAY);
        //userListTextArea.setBackground(Color.white);
        userListTextArea.setWrapStyleWord(true);

        //userListPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //userListPanel.add(userListTextArea);
        frame.add(userListTextArea);
        userListTextArea.setLayout(null);
        buttonPanel.add(listButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(logoutButton);

        inputPanel.add(input);
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
    }
    public void addUserList(String[] list) {
        userList = "";
        int i = 1;

        for (String user: list) {
            System.out.println(user);
            userList += i + ". " + user + "\n";
            i++;
        }

        userListTextArea.setText(userList);
        userListTextArea.repaint();
    }
    private void addText(String text) {
        chatText += text;
        chatTextArea.setText(chatText);
        chatTextArea.repaint();
        //chatPanel.repaint();
        //System.out.println("Chat text updated: " + chatText);
    }
    public void viewGUI() {
        frame.setVisible(true);
    }
}
