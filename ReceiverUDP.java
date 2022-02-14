import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

//Send
public class ReceiverUDP {
    public static void main(String[] args) throws IOException {
        // Create a socket to listen to port 8888
        DatagramSocket ds = new DatagramSocket(8888);
        byte[] receive = new byte[65535];

        DatagramPacket DpReceive = null;
        while(true) {
            // Create a packet to receive the data
            DpReceive = new DatagramPacket(receive, receive.length);

            // Receive the data in the byte buffer
            ds.receive(DpReceive);

            System.out.println("Client:-" + data(receive));

            if(data(receive).toString().equals("bye")) {
                System.out.println("Client sent bye........EXITING");
                break;
            }

            receive = new byte[65535];
        }

        ds.close();
    }

    public static StringBuilder data(byte[] a) {
        if (a == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0){
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

}
