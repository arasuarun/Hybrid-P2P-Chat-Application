/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author arasu
 */
public class ChatListener implements Runnable{
    Thread t;
    String username;
    ServerSocket listener;
    ArrayList<Socket> sockets;
    

    public ChatListener(String _username) throws IOException {
        listener = new ServerSocket(5678);
        System.out.println("Set up server socket");
        sockets = new ArrayList<>();
        username = _username;
        t = null;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Socket newChat = listener.accept();
                System.out.println("Got new connection");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                    newChat.getInputStream()));
                String otherusername = br.readLine();
                String[] usernames = {username, otherusername};
                ChatManager cm = new ChatManager(newChat.getInetAddress().getCanonicalHostName(), 
                        usernames, newChat);
                sockets.add(newChat);                
            } catch (IOException ex) {
                try {
                    listener.close();
                } catch (IOException ex1) {
                    // do nothing
                }
            }
        }
    }
    
    public void start() {
        if(t==null) {
            t = new Thread(this);
            t.start();
        }
    }
}
