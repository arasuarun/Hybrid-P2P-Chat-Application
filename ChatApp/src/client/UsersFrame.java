package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class UsersFrame {
    Socket socket;
    JFrame userFrame;
    JPanel buttonPanel;
    JFrame userButton;
    String username;
    JLabel statusLabel;
    HashMap<String, String> list;
    ArrayList<JButton> buttonList;

    public UsersFrame(HashMap<String, String> _list, String _username, Socket _socket) {
            list = _list;
            username = _username;
            socket = _socket;
            createFrame();
    }

    private void createFrame() {
        JButton newButton;
        buttonList = new ArrayList<>();
        userFrame = new JFrame("Active Users");
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1));
        statusLabel = new JLabel("Press a button to connect to that user");
        userFrame.setLayout(new GridLayout(0, 1));
        userFrame.add(statusLabel, BorderLayout.SOUTH);

        for (Entry<String, String> entry : list.entrySet()) {
            newButton = new JButton(entry.getKey() + " " + entry.getValue());
            newButton.setPreferredSize(new Dimension(80, 120));
            userFrame.add(newButton);
            newButton.setActionCommand(entry.getKey() + " " + entry.getValue());
            newButton.addActionListener(new UserButtonListener());	
            buttonList.add(newButton);
        }
        
//        updateList();
        userFrame.setVisible(true);
        userFrame.setSize(400, 400);
    }	
    
    public void updateList() {
        buttonPanel.removeAll();
        ObjectInputStream ois = null;
        try {
            JButton newButton;
            ois = new ObjectInputStream(
                    socket.getInputStream());
            for (Entry<String, String> entry : list.entrySet()) {
                newButton = new JButton(entry.getKey() + " " + entry.getValue());
                newButton.setPreferredSize(new Dimension(80, 120));
                buttonPanel.add(newButton);
                newButton.setActionCommand(entry.getKey() + " " + entry.getValue());
                newButton.addActionListener(new UserButtonListener());
                buttonList.add(newButton);
            }
        } catch (IOException ex) {
            Logger.getLogger(UsersFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(UsersFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    class UserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] params = e.getActionCommand().split(" ");
            String hostname = params[0];
            String[] usernames = {username, params[1]}; 

            ChatManager cm;
            try {
                cm = new ChatManager(hostname, usernames);
                cm.start();
            } catch (IOException e1) {
                statusLabel.setText("Could not connect to that user");
            }
        }		
    }
}