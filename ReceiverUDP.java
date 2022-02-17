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
import java.nio.ByteBuffer;

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

    public Agent listener() throws IOException{
        DpReceive = new DatagramPacket(receive, receive.length);

        ds.receive(DpReceive);

        Agent a = parseAgent(DpReceive);

        a.updateBeacon(System.currentTimeMillis()/1000);

        return a;
    }

    private Agent parseAgent(DatagramPacket packet) throws IOException{
        receive = packet.getData();

        ByteArrayInputStream bais = new ByteArrayInputStream(receive);
        DataInput di = new DataInputStream(bais);

        int readID = di.readInt();
        readID = Integer.reverseBytes(readID);
        long ID = Integer.toUnsignedLong(readID);

        int readStartTime = di.readInt();
        readStartTime = Integer.reverseBytes(readStartTime);
        long startTime = Integer.toUnsignedLong(readStartTime);

        int readTimeInterval = di.readInt();
        readTimeInterval = Integer.reverseBytes(readTimeInterval);
        long timeInterval = Integer.toUnsignedLong(readTimeInterval);

        byte[] readIP = new byte[16];
        di.readFully(readIP);
        String IP = new String(ByteBuffer.wrap(readIP).array());

        int readPort = di.readInt();
        readPort = Integer.reverseBytes(readPort);
        long port = Integer.toUnsignedLong(readPort);

        return (new Agent(ID, startTime, timeInterval, IP, port));
    }

}
