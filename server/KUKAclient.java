package server;

import java.net.Socket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import packets.*;

public class KUKAclient
{
    public static void main(String[] args) throws Exception
    {        
        // Step 1: creating a socker
        var ip = InetAddress.getByName("127.0.0.1");
        int port = 30005;
        var clientsocket = new Socket(ip, port);
        var reader = new BufferedReader(new InputStreamReader(
            clientsocket.getInputStream()));
        var writer = new BufferedWriter(new OutputStreamWriter(
            clientsocket.getOutputStream()));
        var packet = new MessagePacket();
        
        boolean exitApp = false;
        var usrIn = new Scanner(System.in);
        String usrData, readline;

        // Run the app until the user closes the connection
        while(!exitApp)
        {            
            System.out.println("\nPlease enter the data: ");
            usrData = usrIn.nextLine();
            packet = new MessagePacket(usrData);
            writer.write(packet.serialize()+"\n");     
            writer.flush();                

            Thread.sleep(1000);

            try
            {
                readline = reader.readLine();
            }
            catch(Exception e)
            {
                System.out.println("Problem reading server data"+e);
                continue;
            }

            System.out.println(readline);

            if(usrData.equals("exit"))
            {
                var _packet = new DisconnectPacket();
                writer.write(_packet.serialize()+"\n");
                writer.flush();
                exitApp = true;
            }
        }

        // Step 5: close the socket
        clientsocket.close();        
        usrIn.close();        
    }
}