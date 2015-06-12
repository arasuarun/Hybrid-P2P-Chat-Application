package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MainClient {
    ChatListener cl;
    Socket cSocket;
    private String hostname;
    private int port;
    private String username;
    HashMap<String, String> list;
    UsersFrame uf;
    JFrame startFrame;
    JTextField hostBox, portBox, userBox;
    JButton submitButton;
    JLabel statusLabel;
	
    public MainClient(){
        username = null;
        uf = null;
        createFrame();
    }

    private void createFrame() {
            startFrame = new JFrame();
            hostBox = new JTextField();
            portBox = new JTextField();
            userBox = new JTextField();
            statusLabel = new JLabel();
            submitButton = new JButton("Submit");

            startFrame.setLayout(new GridLayout(8,1));	

            startFrame.add(new JLabel("Enter hostname"));
            startFrame.add(hostBox);
            startFrame.add(new JLabel("Enter port"));
            startFrame.add(portBox);
            startFrame.add(new JLabel("Enter desired username"));
            startFrame.add(userBox);
            startFrame.add(submitButton);
            startFrame.add(statusLabel);

            submitButton.addActionListener(new SubmitButtonListener());

            startFrame.setSize(600, 600);
            startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            startFrame.setVisible(true);		
    }

    public void printList() throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(
            cSocket.getInputStream());
        list = (HashMap<String, String>) ois.readObject();
        uf = new UsersFrame(list, username, cSocket);  
    }

    public void sendUsername() throws IOException {
        System.out.println("Sending username " + username);
        DataOutputStream dos = new DataOutputStream(
            cSocket.getOutputStream());
        dos.writeBytes(username + "\n");            
    }

    public static void main(String[] args) {
        System.out.println("starting client");
        MainClient mc = new MainClient();		
    }
    
    private void startListening() throws IOException {
        cl = new ChatListener(username);
        cl.start();
    }

    class SubmitButtonListener implements ActionListener {
        boolean correct = true;

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!hostBox.getText().trim().equals("")) {
                hostname = hostBox.getText().trim();
            } else {
                correct = false;
            }
            if(!portBox.getText().trim().equals("")) {
                port = Integer.parseInt(portBox.getText().trim());
            } else {
                correct = false;
            }
            if(!userBox.getText().trim().equals("")) {
                username = userBox.getText().trim();
            } else {
                correct = false;
            }
            if(correct) {
                try {
                    cSocket = new Socket(hostname, port);
                    sendUsername();
                    printList();
                    startListening();
                    } catch (IOException e1) {
                        System.out.println(e1);
                        statusLabel.setText("Could not connect to server");
                    } catch (ClassNotFoundException e2) {
                        statusLabel.setText("Some error");
                    }			
            } else {
                    statusLabel.setText("Credentials incorrect");
            }
        }
    }
}
