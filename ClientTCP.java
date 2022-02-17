import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class ClientTCP {

    int port;
    String IP;

    public ClientTCP (int p, String i){
        this.port = p;
        this.IP = i;
    }

    public void run() {
        try {
            int serverPort = port;
            InetAddress host = InetAddress.getByName(IP);
            System.out.println("Connecting to agent on port " + serverPort);

            Socket socket = new Socket(host, serverPort);
            //System.out.println("Just connected to " + socket.getRemoteSocketAddress());
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toServer.println("void GetLocalOS(char OS[16], int *valid)");
            String line = fromServer.readLine();
            System.out.println("Client received: " + line + " from Server");
            toServer.println("void GetLocalTime(int *time, int *valid)");
            line = fromServer.readLine();
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
}