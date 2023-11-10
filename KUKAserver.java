/* Emulating the KUKA robot server
Unlike the UdpServer class, there might be a TCP connections as well.
In this app, ServerSocket is used instead of DatagramSocket
This class implements the singletons design pattern. 
 */

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetSocketAddress;

class KUKAserver implements Runnable, Closeable
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
    private int listenport = 0;
    private static final int sockettimeout = 20;

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
        }
        catch(IOException e)
        {
            throw e;
        }
        finally
        {
            serversocket=null;
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
    public static void main(String[] args)
    {
        final int port = 30005;

        System.out.println("The server is starting ... ");
        final var server = new KUKAserver();
        server.setListenPort(port);
        var tserver = new Thread(server);
        tserver.start();

        // Shutting down the server
        server.stop();
        System.out.println("The server is shut down ...\n");
    }
}
