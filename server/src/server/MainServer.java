package server;

/**
 *
 * @author arasu
 */

import java.util.HashMap;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class MainServer{
    ServerSocket serSocket;
    HashMap<String, String> list;
    ArrayList<ClientHandler> handlers;
    
    MainServer() throws IOException {
        serSocket = new ServerSocket(6789);
        list = new HashMap<>();
        list.put("admin", "admin");
    }
    
    public static void main(String args[]){
        try {
            MainServer ms = new MainServer();
            while(true) {
                try {
                    Socket connSocket = ms.serSocket.accept();
                    ClientHandler ch;
                    ch = new ClientHandler(connSocket, ms);
                    try {
                        ch.start();
                    } catch (IOException ex) {
                        System.out.println("Closing handler");
                        ch.close();
                    }
                } catch (IOException ex) {
                    System.out.println("Cannot accept conn");
                }
            }
        } catch (IOException ex) {
            System.out.println("Cannot start server");
        }
    }
}
