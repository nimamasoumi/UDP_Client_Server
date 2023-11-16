package server;
/* Emulating the KUKA robot server
Unlike the UdpServer class, there might be a TCP connections as well.
In this app, ServerSocket is used instead of DatagramSocket
This class implements the singletons design pattern. 
 */

import java.util.List;
import java.util.ArrayList;
import java.io.Closeable;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;

import utils.EchoPacket;

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

    private ServerSocket serversocket = null;
    private Socket socket = null;
    private int listenport = 0;
    private static final int sockettimeout = 20;
    private List<IPacketListener> packetlisteners = new ArrayList<IPacketListener>();

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
        listen();
        while(serversocket!=null && serversocket.isBound())
        {
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
        this.listenport = _listenport;
    }
    public void addListener(IPacketListener _pl)
    {
        this.packetlisteners.add(_pl);
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
        server.addListener(echo_pl);

        // running the server thread
        var tserver = new Thread(server);
        tserver.start();

        safeSleep(1000);

        // Shutting down the server
        server.stop();
        System.out.println("The server is shut down ...\n");
    }
}
