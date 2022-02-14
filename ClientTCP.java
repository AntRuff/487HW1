import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class ClientTCP {
    public void run() {
        try {
            int serverPort = 8888;
            InetAddress host = InetAddress.getByName("127.0.0.1");
            System.out.println("Connecting to server on port " + serverPort);

            Socket socket = new Socket(host, serverPort);
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toServer.println("Hello from " + socket.getLocalSocketAddress());
            String line = fromServer.readLine();
            System.out.println("Client received: " + line + " from Server");
            toServer.close();
            fromServer.close();
            socket.close();
        } catch(UnknownHostException ex) {
            ex.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientTCP client = new ClientTCP();
        client.run();
    }
}