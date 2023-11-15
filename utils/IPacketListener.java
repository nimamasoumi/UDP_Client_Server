// Packet listener for the KUKAserver
package utils;

interface IPacketListener
{    
    // handlePacket is called when it is recevied from the client
    public void handlePacket(Packet _p);
}