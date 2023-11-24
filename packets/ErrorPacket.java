package packets;

import server.Packet;
import server.PacketType;

public class ErrorPacket extends Packet
{
    /*
     * Error is critical and client should reset state
     */
    public boolean critical = false;
    
    public String message = "";

    /*
     * The error is reponse to a specific packet
     */
    public PacketType responseTo = PacketType.ErrorPacket;

    public ErrorPacket()
    {
        this.type = PacketType.ErrorPacket;
    }

    public ErrorPacket(String _message)
    {
        this.type = PacketType.ErrorPacket;
        this.message = _message;
    }
}
