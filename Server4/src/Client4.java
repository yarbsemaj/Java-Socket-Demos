/*  Java client application
 *  Client4.java
 *  usage:   java Client4 <hostname> <port number>   
 *     hostname and port number are for the Java server application Server4
 *     which must already be running
 *   once connected you can type in messages which will be echoed back
 *   type bye to disconnect from the server
 *   A R Grundy 27th March 2000
 *   Adapted by Gary Allen, March 2002
 */

import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Client4 { 
    public static void main(String args[]) {
	String host;
	int port;
	ClientInterface cl;
	
	host = "";
	port = 0;
	
	try {
	    host = args[0];
	    port = Integer.parseInt(args[1]);
	} catch (Exception e) {
	    System.out.println("host and/or port not supplied");
	    System.exit(1);
	}
	
	cl = new ClientInterface("Client Interface",host,port);
	cl.setVisible(true);
    } // main       
} // class Client4


class ClientInterface extends JFrame implements ActionListener {   
    Socket conn;
    String host;
    int port;
    
    BufferedReader instream;
    BufferedWriter outstream;
    
    JTextField message, reply;
    JLabel messageLabel, replyLabel;  
    JButton send;
    String str;
    String text;
  
    // connect to server
    ClientInterface(String frameTitle, String hostName, int portNo) {
	
	host = hostName;
	port = portNo; 
	
	try {
	    conn = new Socket(host,port);
	    System.out.println("ClientInterface started"); 
	} catch(Exception e) {
	    System.out.println("Error : " + e);
	    System.out.println("Can't connect to " + host + " on port " + port);
	    System.exit(1);
	}
	
	try {
	    instream = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	} catch (Exception e) {
	    System.out.println("Error : " + e);
	    System.out.println("Can't get input stream for socket");
	    System.exit(1);
	}

	try {
	    outstream = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	} catch (Exception e) {
	    System.out.println("Error : " + e);
	    System.out.println("Can't get output stream for socket");
	    System.exit(1);
	}

	// build interface, register as listener and make visible
	Container cp = this.getContentPane();
	cp.setLayout(new GridLayout(5,1));
	
	messageLabel = new JLabel("Enter message and click Send");
	message = new JTextField("",20);
	replyLabel = new JLabel("reply from server : ");
	
	try {
	    str = instream.readLine();   // wait for server's first message
	} catch (Exception e) {
	    System.out.println("Error : " + e);
	    System.exit(1);
	}

	reply = new JTextField(str,40);
	send = new JButton("Send");
	send.addActionListener(this);   

	cp.add(messageLabel);
	cp.add(message);
	cp.add(replyLabel);
	cp.add(reply);
	cp.add(send);
		
	addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		dispose();
		System.exit(0);
	    }
	} );

	setTitle(frameTitle);
	setSize(400, 200 );	
	setVisible( true );

    } // end ClientInterface constructor

   
    public void actionPerformed(ActionEvent event) {
	send.removeActionListener(this);   // ignore button clicks
	text = message.getText();
	try {
	    outstream.write(text);         // send message to server
	    outstream.newLine();
	    outstream.flush();
	    
	    str = instream.readLine();     // wait for server reply
       
	    if (text.startsWith("bye") )   // final reply from server 
		str = instream.readLine();
	} catch(IOException e) {
	    System.out.println("Error : " + e);
	    System.exit(1);
	}
	
	reply.setText(str);                  // display reply in interface
	send.addActionListener(this);        // listen for button clicks again
    }  // end method actionPerformed
}  // end class Client

