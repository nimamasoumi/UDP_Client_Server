package packets;

import server.Packet;
import server.PacketType;

public class DisconnectPacket extends Packet
{
    public DisconnectPacket()
    {
        this.type=PacketType.DisconnectPacket;
    }
}