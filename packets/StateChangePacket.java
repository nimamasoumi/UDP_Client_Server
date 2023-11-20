package packets;

import server.Packet;
import server.PacketType;
import server.data.KUKAAppStates;

/**
 * This packet is used to notify clients that this app's state has changed. A
 * state may be requested through the ChangeStatePacket.
 */
public class StateChangePacket extends Packet
{
    public KUKAAppStates state = KUKAAppStates.INITIALIZING;

    /**
     * Reason for change (optional)
     */
    public String reason = "";

    public StateChangePacket()
    {
        this.type = PacketType.StateChangePacket;
    }

    public StateChangePacket(KUKAAppStates _state)
    {
        this.type = PacketType.StateChangePacket;
        this.state = _state;
    }

    public StateChangePacket(KUKAAppStates _state, String _reason)
    {
        this.type = PacketType.StateChangePacket;
        this.state = _state;
        this.reason = _reason;
    }    
}
