/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;


public class EchoClient {

    private Socket echoSocket = null;
    private PrintStream socOut = null;
    private Integer port;
    
    /**
     * main method
     * accepts a connection, receives a message from client then sends an echo to the client
     **/

    public EchoClient(Integer port) {
        this.port = port;
        setUpClient();
    }


    private void setUpClient() {
        try {
            // Usage: java EchoClient <EchoServer host> <EchoServer port>
            InetAddress ip = InetAddress.getLocalHost();
            echoSocket = new Socket(ip.getHostAddress(), port);
            socOut = new PrintStream(echoSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        socOut.println(message);
    }

    public Socket getEchoSocket() {
        return this.echoSocket;
    }
}

