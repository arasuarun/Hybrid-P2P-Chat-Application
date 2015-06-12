package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ChatWindow {
    JFrame mainFrame;
    JTextField textBox;
    JTextArea msgArea;
    JPanel controlPanel;
    JButton sendButton;
    String[] usernames;

    ChatWindow(String[] _usernames) {
        mainFrame = new JFrame("");
        textBox = new JTextField();
        msgArea = new JTextArea();
        controlPanel = new JPanel();
        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(40, 60));
        usernames = _usernames;

        addComponents();
    }

    public void addComponents() {
        mainFrame.setLayout(new GridLayout(0,1));
        controlPanel.setLayout(new BorderLayout());

        mainFrame.add(msgArea);
        msgArea.append("STARTING CHAT WITH " + usernames[1]);
        mainFrame.add(textBox);
        mainFrame.add(controlPanel);
        controlPanel.add(sendButton, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
        mainFrame.setSize(600, 600);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public JButton getButton() {
        return sendButton;
    }

    public String getText() {
        return textBox.getText();
    }

    public void addTextFromThisUser(String string) {
        msgArea.append("\n" + string);
        textBox.setText("");
        textBox.requestFocus();
    }

    public void addTextFromOtherUser(String string) {
        msgArea.append("\n" + string);
    }
}
