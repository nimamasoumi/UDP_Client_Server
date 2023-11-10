/* Emulating the KUKA robot server
Unlike the UdpServer class, there might be a TCP connections as well.
In this app, ServerSocket is used instead of DatagramSocket
This class implements the singletons design pattern. 
 */

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;

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

    @Override
    public void run()
    {

    }

    @Override
    public void close() throws IOException
    {

    }
    public static void main(String[] args)
    {

    }
}
