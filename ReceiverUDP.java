import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

//Send
public class ReceiverUDP {
    DatagramSocket ds;
    byte[] receive;
    DatagramPacket DpReceive;

    public ReceiverUDP(byte[] receive, DatagramPacket DpReceive, DatagramSocket ds){
        this.ds = ds;
        this.receive = receive;
        this.DpReceive = DpReceive;
    }

    public void listener() throws IOException{
        DpReceive = new DatagramPacket(receive, receive.length);

        ds.receive(DpReceive);

        Agent a = parseAgent(DpReceive);
    }

    private Agent parseAgent(DatagramPacket packet) throws IOException{
        receive = packet.getData();

        ByteArrayInputStream bais = new ByteArrayInputStream(receive);
        DataInput di = new DataInputStream(bais);

        int ID = di.readInt();
        int startTime = di.readInt();
        int timeInterval = di.readInt();
        byte[] IP = new byte[20];
        di.readFully(IP);
        int port = di.readInt();
        System.out.println("" + ID + "," + startTime + "," + timeInterval
            + "," + IP + "," + port);

        return null;
    }

}
