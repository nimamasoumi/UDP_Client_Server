// Echo the received packets back to the client
package utils;

class EchoPacket implements IPacketListener
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