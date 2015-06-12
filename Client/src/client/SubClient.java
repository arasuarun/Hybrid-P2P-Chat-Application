package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SubClient extends ChatManager {
	
    public SubClient(String _hostname, String[] _usernames, Socket _socket, ChatWindow _cw)
            throws IOException {
        super(_hostname, _usernames);
        socket = _socket;
        cw = _cw;
    }

    @Override
    public void run() {
        cw.getButton().addActionListener(new SendButtonListener());
    }

    @Override
    public void start() {
        if (t==null) {
            t = new Thread(this);
            t.start();
        }
    }
    
    class SendButtonListener implements ActionListener {
        String text, labelText;
        @Override
        public void actionPerformed(ActionEvent e){
            if(!((text = cw.getText().trim()).equals(""))) {
                labelText = usernames[0] + " : " + text + " \n ";			
                try {
                    DataOutputStream dos = new DataOutputStream(
                                    socket.getOutputStream());
                    dos.writeBytes(labelText);
                    cw.addTextFromThisUser(labelText);	
                } catch (IOException e1) {
                    cw.addTextFromThisUser("CANNOT SEND");
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        // do nothing
                    }
                }					
            }
        }		
    }
}
