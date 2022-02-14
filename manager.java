import java.io.*;
import java.net.*;

public class manager {
    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            //server listening to 8888
            server = new ServerSocket(8888);
            server.setReuseAddress(true);

            //client requests
            while (true) {
                //socket to receive requests
                Socket client = server.accept();

                //Display new client
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                //Create new thread
                ClientHandler clientSock = new ClientHandler(client);

                new Thread(clientSock).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try { server.close(); }
                catch (IOException e) { e.printStackTrace();}
            }
        }
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
                System.out.printf(" Send from the client: %s\n", line);
                out.println(line);
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
