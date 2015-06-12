package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ChatManager implements Runnable {
	Thread t;
	String hostname;
	String [] usernames;
	Integer count;
	InetAddress hostAddress;
	Socket socket;
	ChatWindow cw;
	
	private SubClient sc;
	private SubServer ss;
	
	public ChatManager(String _hostname, String[] _usernames) throws IOException {
            hostname = _hostname;
            usernames = _usernames;
            count = 0;
            hostAddress = InetAddress.getByName(hostname);
            socket = null;
            t = null;
	}
        
        public ChatManager(String _hostname, String[] _usernames, Socket _socket) throws IOException {
            this(_hostname, _usernames);
            socket = _socket;
	}
        
        
	@Override
	public void run() {		
		sc.start();
		ss.start();
	}
	
	public void createFrame() {
		cw = new ChatWindow(usernames);
	}
        
        public void sendUsername() throws IOException {
            DataOutputStream dos = new DataOutputStream(
                socket.getOutputStream());
            
            dos.writeBytes(usernames[0] + "\n");
        }
	
	public void start() throws IOException {
		createFrame();
                if(socket == null) {
                    socket = new Socket(hostname, 5678);
                    sendUsername();
                }
		sc = new SubClient(hostname, usernames, socket, cw);
		ss = new SubServer(hostname, usernames, socket, cw);
		if (t==null) {
			t = new Thread(this);
			t.start();
		}
	}	
}
