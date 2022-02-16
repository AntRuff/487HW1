import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;




public class manager {

    public static int PORT = 8888;
    public static void main(String[] args) {
        ServerSocket tcp = null;
        DatagramSocket udp = null;

        try {
            //server listening to 8888
            /*tcp = new ServerSocket(PORT);
            tcp.setReuseAddress(true);*/

            udp = new DatagramSocket(PORT);
            udp.setReuseAddress(true);

            BeaconListener bl = new BeaconListener(udp);
            new Thread(bl).start();
            //client requests
            /*while (true) {
                //socket to receive requests
                Socket client = server.accept();

                //Display new client
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                //Create new thread
                ClientHandler clientSock = new ClientHandler(client);

                new Thread(clientSock).start();
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            if (tcp != null) {
                try { tcp.close(); udp.close();}
                catch (IOException e) { e.printStackTrace();}
            }
        }*/
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    //Constructor
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run(){
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            //get outputstream of client
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            //get the input stream of the client
            in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                //writing the message from client
                System.out.printf(" Sent from the client: %s\n", line);
                //out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


class BeaconListener implements Runnable {
    private final DatagramSocket clientSocket;
    byte[] receive;

    public BeaconListener(DatagramSocket ds){
        this.clientSocket = ds;
        receive = new byte[65535];
    }

    public void run() {
        DatagramPacket DpReceive = null;
        ReceiverUDP rUDP = new ReceiverUDP(receive, DpReceive, clientSocket);
        while (true) {
            try {
                rUDP.listener();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

/*class AgentMonitor implements Runnable {
    private List<Agent> agentList;
    
    public AgentMonitor() {
        agentList = new ArrayList<Agent>();
    }
}

class ClientAgent implements Runnable {
    
}*/

class Agent {
    int id;
    int startTime;
    int interval;
    String ip;
    int port;
    int lastBeacon;
}