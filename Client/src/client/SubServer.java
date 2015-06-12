package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SubServer extends ChatManager {
	
    public SubServer(String _hostname, String[] _usernames, Socket _socket, ChatWindow _cw)
            throws IOException {
        super(_hostname, _usernames);
        socket = _socket;
        cw = _cw;
    }

    @Override
    public void run() {
        String text;
        try {
            BufferedReader inFromOther = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            while(true) {
                if(!(text = inFromOther.readLine().trim()).equals("")) {
                        cw.addTextFromOtherUser(text);
                }
            }
        } catch (IOException e) {
            cw.addTextFromOtherUser("CANNOT RECEIVE");
            try {                 
                socket.close();
            } catch (IOException ex) {
                // do nothing
            }
        }
    }

    @Override
    public void start() {
        if (t==null) {
            t = new Thread(this);
            t.start();
        }
    }
}
