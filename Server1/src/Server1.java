/*  Single-Threaded  Server
 *  Server1.java
 *  usage:   java Server1
 *  opens a socket on port 6001
 *  this server will shut down on receipt of the string "bye"
 *     from a client
 *   Written by A R Grundy March 2000
 *   Adapted and updated by Gary Allen, March 2002
 */

import java.net.*;
import java.io.*;

public class Server1 {
        public static void main(String args[]) {
        SimpleServer ss = new SimpleServer(6001) ;
    }        
} // class Server1

class SimpleServer {
    ServerSocket sock;
    Socket conn;
    
    BufferedReader instream;
    BufferedWriter outstream;
    String str;
    
    //  establish server socket and listen for client 
    SimpleServer(int port) {
	try {
	    sock = new ServerSocket(port);
	    System.out.println("Started on port " + port);

	    conn = sock.accept();       //  listen for client connection
	}
	catch(Exception e) {
	    System.out.println("Error : " + e);
	    System.exit(1);
	}
	
	//  socket between client and server now established	
	//     socket has input and output streams asssociated with it
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
       
	//  read all client data from socket until "bye" is sent
	do {
	    try {
		// wait for client to send data
		str = instream.readLine();;         

		// send a reply to client
		outstream.write("You said " + str); 
		outstream.newLine();
		outstream.flush();
	    } catch(IOException e) {
		System.out.println("Error : " + e);
		System.exit(1);
	    }	    

	    System.out.println("Client sent string " + str.trim());
	}  while ( !str.startsWith("bye") );
	
	System.out.println("Closing server now");
	System.exit(0);
    }  // SimpleServer constructor    
} // class SimpleServer
