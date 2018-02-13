/*  Multi-Threaded  Server
 *  Server4.java
 *  usage:   java Server4 <port number>   --  defaults to 6001 if omitted
 *  can handle multiple simultaneous clients
 *  this server must be shut down by external means
 *     otherwise remains online listening for clients
 *   A R Grundy 24th March 2000
 *   Adapted by Gary Allen, March 2002
 */

import java.net.*;
import java.io.*;

public class Server4 {     
    public static void main(String args[]) {
	
	int port;
	
	try {
	    port = Integer.parseInt(args[0]);
	} catch (Exception e) {
	    port = 6001;
	}
	
	MultiThreadServer mts = new MultiThreadServer(port);
    }        
} // class Server4


class MultiThreadServer {

    ServerSocket sock;
    Socket conn;
    int port;
   
    //  establish server socket and listen for client 
    MultiThreadServer(int port) {
	this.port = port;
	
	try {
	    sock = new ServerSocket(port);
	    System.out.println("Server started on port " + port); 
	} catch(Exception e) {
	    System.out.println("Error : " + e);
	    System.out.println("Can't start server on port " + port);
	    System.exit(1);
	}

	while (true) {
	    try {
		conn = sock.accept();       //  listen for client connection
		ClientHandler ch = new ClientHandler(conn);
		System.out.println("new client connection");
	    } catch(Exception e) {
		System.out.println("Error : " + e);
		System.out.println("Can't create socket for client");
		System.exit(1);
	    }
	}
    } // end MultiThreadServer constructor
}  // end class MultiThreadedServer


class ClientHandler implements Runnable {
    Socket conn;
    Thread clientThread;
    
    BufferedReader instream; 
    BufferedWriter outstream;
    
    String str;
    
    ClientHandler(Socket connin) {
	conn = connin;
	clientThread = new Thread(this);
	clientThread.start();
    } // end ClientHandler constructor

    public void stop() {
	try {
	    conn.close();
	}
	catch(Exception e) {
	    System.out.println("Error : " + e);
	}
	clientThread = null;
    } // end method stop
    
    public void run() {
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

	try {
	    outstream.write("Your wish is my command . . .");
	    outstream.newLine();
	    outstream.flush();
	} catch(IOException e) {
	    System.out.println("Error : " + e);
	    System.exit(1);
	}
	
	do {
	    try {
		str = instream.readLine();           // wait for client to send data   
		outstream.write("You said " + str);  // send a reply to client
		outstream.newLine();
		outstream.flush();
      
		System.out.print("Client sent string " + str.trim());           
		System.out.println(" on socket " + conn.toString());
	    } catch(IOException e) {
		System.out.println("Error : " + e);
		System.exit(1);
	    }
	}  while ( !str.startsWith("bye") );

	try { 
	    outstream.write("Missing you already . . ");
	    outstream.newLine();
	    outstream.flush();
	} catch(IOException e) {
	    System.out.println("Error : " + e);
	    System.exit(1);
	}

	System.out.println("closing client connecton");
	this.stop();
    }  // end method run   
} // class ClientHandler
