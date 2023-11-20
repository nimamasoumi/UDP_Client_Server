package packets;

import server.Packet;
import server.PacketType;

public class MessagePacket extends Packet
{
    public String message = "";
    public MessagePacket()
    {
        this.type=PacketType.MessagePacket;
    }
    public MessagePacket(String _message)
    {
        this.type=PacketType.MessagePacket;
        this.message=_message;
    }
}