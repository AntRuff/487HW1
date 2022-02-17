import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;





public class manager {

    public static int PORT = 8888;
    public static void main(String[] args) {
        DatagramSocket udp = null;
        List<Agent> agentList = Collections.synchronizedList(new ArrayList<Agent>());

        try {
            udp = new DatagramSocket(PORT);
            udp.setReuseAddress(true);

            BeaconListener bl = new BeaconListener(udp, agentList);
            new Thread(bl).start();
            AgentMonitor am = new AgentMonitor(agentList);
            new Thread(am).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class BeaconListener implements Runnable {
    private final DatagramSocket clientSocket;
    byte[] receive;
    List<Agent> agentList;

    public BeaconListener(DatagramSocket ds, List<Agent> agentList){
        this.clientSocket = ds;
        receive = new byte[65535];
        this.agentList = agentList;
    }

    private void updateAgentList(Agent newAgent){
        synchronized(agentList) {
            ListIterator<Agent> i = agentList.listIterator();
            boolean isNew = true;
            while(i.hasNext()){
                Agent currAgent = i.next();
                if(currAgent.getID() == newAgent.getID()){
                    isNew = false;
                    currAgent.updateBeacon(newAgent.getLastBeacon());
                    if(currAgent.isDead()){
                        currAgent.revive();
                        System.out.println("Agent with ID " + currAgent.getID() + " has been revived.");
                    }
                    if(currAgent.getStartTime() != newAgent.getStartTime()){
                        currAgent.updateStartTime(newAgent.getStartTime());
                        System.out.println("Agent with ID " + currAgent.getID() + " has been restarted");
                    }
                    i.set(currAgent);
                    break;
                }
            }
            if (isNew){
                agentList.add(newAgent);
                ClientAgent client = new ClientAgent(newAgent);
                new Thread(client).start();
            }
        }
    }

    public void run() {
        DatagramPacket DpReceive = null;
        ReceiverUDP rUDP = new ReceiverUDP(receive, DpReceive, clientSocket);
        while (true) {
            try {
                Agent a = rUDP.listener();
                updateAgentList(a);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

class AgentMonitor implements Runnable {

    List<Agent> agentList;
    
    public AgentMonitor(List<Agent> agentsList){
        this.agentList = agentsList;
    }

    public void run() {
        while(true) {
            synchronized(agentList){
                ListIterator<Agent> i = agentList.listIterator();
                while (i.hasNext()){
                    Agent agent = i.next();
                    if(!agent.isDead()){
                        long curTime = System.currentTimeMillis() / 1000;
                        if (curTime - agent.getLastBeacon() > agent.getInterval() * 2) {
                            System.out.println("Agent with ID " + agent.getID() + " has died");
                            agent.kill();
                            i.set(agent);
                        }
                    }
                }
            }
        }
    }
}

class ClientAgent implements Runnable {
    
    Agent agent;

    public ClientAgent (Agent a) {
        this.agent = a;
    }

    public void run() {
        ClientTCP client = new ClientTCP((int) agent.getPort(), agent.getIP());
        client.run();
    }

}

class Agent {
    private long id;
    private long startTime;
    private long interval;
    private String ip;
    private long port;
    private long lastBeacon;
    private boolean isDead = false;

    public Agent(long id, long start, long interval, String ip, long port){
        this.id = id;
        this.startTime = start;
        this.interval = interval;
        this.ip = ip;
        this.port = port;
    }

    public void updateBeacon(long curtime){
        lastBeacon = curtime;
    }

    public long getLastBeacon() {
        return lastBeacon;
    }

    public long getStartTime() {
        return startTime;
    }

    public void updateStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getInterval() {
        return interval;
    }

    public long getPort() {
        return port;
    }

    public String getIP() {
        return ip;
    }

    public long getID() {
        return id;
    }

    public boolean isDead() {
        return isDead;
    }

    public void kill() {
        isDead = true;
    }

    public void revive() {
        isDead = false;
    }
}