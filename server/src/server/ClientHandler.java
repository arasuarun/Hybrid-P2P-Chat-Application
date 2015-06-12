package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler implements Runnable {
	Thread t;
	private final Socket cSocket;
	String hostname;
        MainServer ps; // parent server
	
	public ClientHandler(Socket _cSocket, MainServer _parentServer){
		t = null;
		cSocket = _cSocket;
                ps = _parentServer;
		hostname = cSocket.getInetAddress().getCanonicalHostName();
	}

	public void start() throws IOException {
		addToList();
		showUsers();
		if (t==null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	@Override
	public void run() {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(
                        cSocket.getOutputStream());
                
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("giving list");
                            oos.writeObject(ps.list);
                        } catch (IOException ex) {
                            removeFromList();
                            System.out.println("Closing socket");
                            close();
                        }
                    }
                }, 10000);
            } catch (IOException ex) {
                // do someting
            }
 	}

	private void addToList() throws IOException {
            BufferedReader inFromUser;
            inFromUser = new BufferedReader(new InputStreamReader(
                            cSocket.getInputStream()));
            String username;
            username = inFromUser.readLine();
            ps.list.put(hostname, username);		
	}
	
	private void removeFromList() {
            System.out.println("removing...");
            ps.list.remove(hostname);
	}
	
	private void showUsers(){
            try {
                ObjectOutputStream oos = 
                        new ObjectOutputStream((cSocket.getOutputStream())) ;
                oos.writeObject(ps.list);
            } catch(IOException e) {
                System.out.println("unable to send");
                System.out.println(e);
            }
            
        }	
        
        public void close() {
            try {
                cSocket.close();
            } catch (IOException ex) {
                //
            }
        }
}
