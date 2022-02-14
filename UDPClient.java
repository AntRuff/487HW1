import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {
    public static String server = "127.0.0.1";
    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);

        //Create Socket
        DatagramSocket ds = new DatagramSocket();

        InetAddress ip = InetAddress.getByName(server);
        byte buf[] = null;

        while (true) {
            String inp = sc.nextLine();

            buf = inp.getBytes();

            //Create packet
            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 8888);

            //Invoke send call

            ds.send(DpSend);

            if (inp.equals("bye")){
                break;
            }
        }

        ds.close();
        sc.close();
    }

}
