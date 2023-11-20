package packets;

import server.Packet;
import server.PacketType;
import server.data.KUKAAppStates;

public class ChangeStatePacket extends Packet{
    
    // requested state
    public KUKAAppStates state = KUKAAppStates.ERROR;
    
    public ChangeStatePacket()
    {
        this.type = PacketType.ChangeStatePacket;
    }
    public ChangeStatePacket(KUKAAppStates _state)
    {
        this.type = PacketType.ChangeStatePacket;
        this.state = _state;
    }
}
