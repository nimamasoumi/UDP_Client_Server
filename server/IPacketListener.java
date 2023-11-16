// Packet listener for the KUKAserver
package server;

public interface IPacketListener
{    
    // handlePacket is called when it is recevied from the client
    public void handlePacket(Packet _p);
}