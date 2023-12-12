import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;

class UdpClient
{
    public UdpClient(){}

    private static DatagramSocket dsocket = null;
    private int packetCounter = 0;
    private static final String packetIdentifier = "RobotStateProvider";
    private static final String PING_COMMAND = "Ping";
    private static final String PONG_COMMAND = "Pong";
    private static final String UTF_8 = "UTF-8";
    private static final int SOCKET_TIMEOUT = 20;
    private List<String[]> receivedMessages = new LinkedList<>();
    public static void main(String[] args) throws Exception
    {
        /* args[0] is the ip address of the server 
         * args[1] is the port number of receiver
        */

        var udpComm = new UdpClient();
        // Step 1: creating a socket
        dsocket = new DatagramSocket();

        int informer_port = 30008;
        while(!udpComm.ping(2000, InetAddress.getByName("127.0.0.1"),informer_port))
        {
            System.out.println("Pinging failed! Retrying ...");
        }

        var ip = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        DatagramPacket dpacket;
        
        boolean exitApp = false;
        var usrIn = new Scanner(System.in);
        byte[] buf = null;
        String usrData;

        // Run the app until the user closes the connection
        while(!exitApp)
        {
            // Step 2: convert the user data into bytes
            System.out.println("\nPlease enter the data: ");
            usrData = usrIn.nextLine();
            buf = usrData.getBytes();

            // Step 3: create the DatagramPacket for sending to the server
            dpacket = new DatagramPacket(buf, buf.length, ip, port);

            // Step 4: send the data
            dsocket.send(dpacket);

            if(usrData.equals("exit"))
            {
                exitApp = true;
            }
        }

        // Step 5: close the socket
        dsocket.close();
        usrIn.close();
        
    }

    public synchronized String[] takeRecMsg()
    {
        if(receivedMessages.isEmpty())
        {
            return new String[0];
        }
        return ((LinkedList<String[]>) receivedMessages).removeFirst();
    }

    private boolean receivePong(int _counter, int _timeout) throws IOException
    {
        //String lastMsg = null;
        long tStart = System.currentTimeMillis();
        while(System.currentTimeMillis() - tStart < _timeout)
        {
            String[] message = takeRecMsg();
            if(0!=message.length)
            {
                if(message[1].equals(PONG_COMMAND) && (Integer.parseInt(message[2])==_counter))
                {
                    System.out.printf("Successfully received Pong for %s\n",packetIdentifier);
                    return true;
                }
            }
            try
            {
                Thread.sleep(SOCKET_TIMEOUT);
            }
            catch(Exception e)
            {
                System.out.println("Something went wrong!");
            }
        }

        return false;
    }
    
    public boolean ping(int _timeout, InetAddress _ip, int _port) throws IOException
    {
        int counter = ++packetCounter;
        String fullMsgString = String.format("%s;%s;%d",packetIdentifier, PING_COMMAND, counter);

        byte[] msg = fullMsgString.getBytes(UTF_8);
        dsocket.send(new DatagramPacket(msg, msg.length, new InetSocketAddress(_ip, _port)));
        
        return receivePong(counter, _timeout);
    }
}