import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

class KUKAClient
{
    public static void main(String[] args) throws Exception
    {
        /* args[0] is the ip address of the server 
         * args[1] is the port number of receiver
        */

        // Step 1: creating a socker
        var dsocket = new DatagramSocket();
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
}