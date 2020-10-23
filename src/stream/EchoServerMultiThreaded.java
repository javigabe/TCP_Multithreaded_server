/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class EchoServerMultiThreaded  {

    /**
     * main method
     * @param EchoServer port
     *
     **/
    private static List<Socket> outputServiceClientSockets = new ArrayList<Socket>();
    private static int nbClient = 0;
    private static List<String> messages = new ArrayList<>();

    public static void main(String args[]){
        ServerSocket listenSocket;

        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }

        getStoredMessages();
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from:" + clientSocket.getInetAddress());
                nbClient ++;
                ClientThread ct = new ClientThread(clientSocket,"Client " + nbClient);
                EchoServerMultiThreaded.addServiceClientSocket(clientSocket);
                recoverMessages(clientSocket);

                ct.start();
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
            e.printStackTrace();

        }
    }


    /** Send a message to all connected clients
     *
     * @param message
     */
    synchronized public static void sendMessageToAll(String message) {
        messages.add(message);
        storeMessage(message);

        for (Socket s: outputServiceClientSockets) {
            try {
                PrintStream out = new PrintStream(s.getOutputStream());
                out.println(message);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private synchronized static void recoverMessages(Socket s) {
        PrintStream out = null;
        for (String message: messages) {
            try {
                out = new PrintStream(s.getOutputStream());
                out.println(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized static void getStoredMessages() {
        try (BufferedReader br = new BufferedReader(new FileReader(".storage"))) {
            String line;
            while ((line = br.readLine()) != null) {
                messages.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized static void storeMessage(String message) {
        FileWriter fw;
        try {
            fw = new FileWriter(".storage", true);
            fw.write(message + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Insert in a collection, new service client thread output stream
     *
     * @param out, service client thread output stream
     */
    synchronized public static void addServiceClientSocket(Socket s){
        outputServiceClientSockets.add(s);
    }
}
