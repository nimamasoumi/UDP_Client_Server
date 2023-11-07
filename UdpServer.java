import java.net.DatagramPacket;
import java.net.DatagramSocket;

class UdpServer
{
    public static void main(String[] args) throws Exception
    {
        // Step 1 : create a DatagramSocket to listen to a port
        int port = 30005;
        var dsocket = new DatagramSocket(port);

        // Preparing to receive the data
        byte[] buf = new byte[65535];
        String msg;
        DatagramPacket dpacket;
        
        // Run the application until the client closes 
        boolean exitApp = false;

        while(!exitApp)
        {
            // Step 2: creating the packet to be received
            dpacket = new DatagramPacket(buf, buf.length);

            // Step 3: start receiving using the socket
            dsocket.receive(dpacket);

            // Step 4: processing the data
            msg = new String(buf);
            System.out.println("\nData received in server: "+ msg.trim());                        
            buf = new byte[65535];

            if(msg.trim().equals("exit"))
            {
                System.out.println("\nClosing the server ...");
                exitApp = true;
            }

        }

        // Step 5: closing the socket
        dsocket.close();

    }
}
    