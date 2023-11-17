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
}