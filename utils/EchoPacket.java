// Echo the received packets back to the client
package utils;

import server.IPacketListener;
import server.KUKAserver;
import server.Packet;

public class EchoPacket implements IPacketListener
{
    private KUKAserver server = KUKAserver.getInstance();

    @Override
    public void handlePacket(Packet _p)
    {
        System.out.println("Received packet: "+_p.serialize());
        // echo the packet
        server.send(_p);
    }
}