package server;
/* Emulating the KUKA robot server
Unlike the UdpServer class, there might be a TCP connections as well.
In this app, ServerSocket is used instead of DatagramSocket
This class implements the singletons design pattern. 
 */

import java.util.List;

import server.data.KUKAAppStates;

import java.util.ArrayList;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;

import utils.EchoPacket;
import tasks.*;

public class KUKAserver implements Runnable, Closeable
{
    private static volatile KUKAserver instance = null;

    private KUKAserver(){};

    public static KUKAserver getInstance()
    {
        if(instance == null)
        {
            synchronized(KUKAserver.class)
            {
                if(instance==null)
                {
                    instance = new KUKAserver();
                }
            }
        }
        return instance;
    }

    private static ServerSocket serversocket = null;
    private static Socket socket = null;
    private static int listenport = 0;
    private static final int sockettimeout = 20;
    private static List<IPacketListener> packetlisteners = new ArrayList<IPacketListener>();
    private static KUKAAppStates medAppState = KUKAAppStates.INITIALIZING;

    private static void safeSleep(long _s)
    {
        try
        {
            Thread.sleep(_s);            
        }
        catch(Exception e)
        {

        }
    }

    public boolean isConnected()
    {
        return serversocket!=null && serversocket.isBound();
    }
    
    public boolean isClientConnected()
    {
        return isConnected() && socket != null && socket.isConnected();
    }

    private synchronized boolean listen()
    {
        System.out.println("Initializing the server socket ... ");
        try
        {
            serversocket = new ServerSocket();
            serversocket.setReuseAddress(true);
            serversocket.bind(new InetSocketAddress(listenport));
            serversocket.setSoTimeout(sockettimeout);
        }
        catch(Exception e)
        {
            System.out.println("problem initializing the server socket."+e);
            return false;
        }
        return true;
    }

    @Override
    public void run()
    {
        // server starts listening on a port passively
        listen();

        // the server thread keeps running until the server is listening
        while(serversocket!=null && serversocket.isBound())
        {
            // accepting clients and creating sockets for them
            if (socket == null || !socket.isConnected() || socket.isClosed())
            {
                try
                {
                    socket = serversocket.accept();
                }
                catch(IOException e)
                {
                    safeSleep(1000);
                    continue;
                }
            }

            // creating a stream to read the client data
            BufferedReader reader = null;
            System.out.println("Client connected ...");
            try
            {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            catch (Exception e)
            {
                System.out.println("Problem getting client socket stream.\n"+e);
                safeSleep(1000);
                continue;
            }

            // serve the client by reading the client socket stream
            // continue until the client disconnects
            while(socket.isConnected() && !socket.isClosed())
            {
                // reading the client packet as a String
                String linepacket;
                try
                {
                    linepacket = reader.readLine();
                }
                catch (Exception e)
                {
                    if(socket.isConnected())
                    {
                        System.out.println("Problems reading client socket.\n"+e);
                        try
                        {
                            socket.close();
                        }
                        catch (IOException e2)
                        {
                            System.out.println("Problem closing the client socket.\n"+e2);
                        }
                    }
                    break;
                }
                if(linepacket==null)
                {
                    if(socket.isConnected())
                    {
                        System.out.println("Problems reading client socket.\n");
                        try
                        {
                            socket.close();
                        }
                        catch (IOException e)
                        {
                            System.out.println("Problem closing the client socket.\n"+e);
                        }
                    }
                    break;
                }

                // converting the string into the packet and handle the data
                final var recPacket = Packet.deserialize(linepacket);
                
                /* the server does not process the packets directly
                 except for the disconnect packet which requests for shutting down the client
                 it lets the packet listeners take action!
                 e.g. EchoPacket prints the entire packet and sends it back to the client */
                if(recPacket.type==PacketType.DisconnectPacket)
                {                    
                    System.out.println("Client request to disconnect.\n");
                    try
                    {
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        System.out.println("Closing the client socket.\n"+e);
                    }                        
                    break;
                }

                notifyPacketListeners(recPacket);
            }

            System.out.println("Client disconnected.");

            if(socket!=null)
            {
                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                    System.out.println("Closing the client socket.\n"+e);
                }
            }
        }
        stop();
    }

    private void notifyPacketListeners(final Packet _packet)
    {
        synchronized(this)
        {
            for(final var listener: packetlisteners)
            {
                // Handle packets in a separate thread so that we do not
                // block processing new packets.
                new Thread(new Runnable() 
                {
                    @Override
                    public void run()
                    {   
                        listener.handlePacket(_packet);
                    }
                }).start();
            }
        }
    }

    @Override
    public void close() throws IOException
    {
        System.out.println("Closing the socket ... ");
        try
        {
            if(serversocket!=null)
            {
                serversocket.close();
            }
            if(socket!=null)
            {
                socket.close();
            }
        }
        catch(IOException e)
        {
            throw e;
        }
        finally
        {
            serversocket=null;
            socket=null;
        }
    }

    public synchronized boolean stop()
    {
        try
        {
            close();
        }
        catch(IOException e)
        {
            System.out.println("Problem closing the socket ... "+e);
            return false;
        }
        return true;
    }

    public void setListenPort(int _listenport)
    {
        KUKAserver.listenport = _listenport;
    }
    public void addListener(IPacketListener _pl)
    {
        KUKAserver.packetlisteners.add(_pl);
    }

    // send packets to connected clients
    public synchronized void send(Packet _p)
    {
        if(!isClientConnected() || socket.isClosed())
        {  
            System.out.println("No client is connected, dropping packet: "+_p.serialize());
            return;
        }

        try
        {
            System.out.println("Sending a packet to the client.");
            var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(_p.serialize()+"\n");
            writer.flush();
        }
        catch(Exception e)
        {
            System.out.println("Failed sending packet: "+
                _p.serialize()+"\n"+e);
        }
    }
    public static void main(String[] args)
    {
        final int port = 30005;
        var echo_pl = new EchoPacket();

        // initializing the server
        System.out.println("The server is starting ... ");
        final var server = new KUKAserver();
        server.setListenPort(port);

        // running the server thread
        var tserver = new Thread(server);
        tserver.start();

        safeSleep(1000);

        server.addListener(echo_pl);

        // try
        // {
        //     tserver.join();
        // }
        // catch (Exception e)
        // {
        //     System.out.println("Server thread encountered an issue.\n"+e);
        // }

        boolean exitApp = false;
        while(!exitApp)
        {
            // Find the proper task to run
            // task managers are listeners of KUKAserver as well
            // Below we are deciding which one to run based on the client's request
            ITask task = null;
            switch(medAppState)
            {
                case IDLE:
                    break;
                case BASIC_MOVE:
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }

            // If we found a task we can run
            if (task!=null)
            {
                // Tasks will return false if they fail to initialize,
                // We should return to the initialization/idle state if
                // this happens. 
                // You can also default to IDLE by returning false if that
                // is the desired result.
                if (!task.run())
                {
                    medAppState = KUKAAppStates.IDLE;
                }
            }
        };
        

        // Shutting down the server
        server.stop();
        System.out.println("The server is shut down ...\n");
    }
}
