package utils;

import server.IPacketListener;
import server.Packet;

public class StateChanger implements IPacketListener{

    private boolean isCancel = false;

    public boolean getIsCancel()
    {
        return this.isCancel;
    }
    
    @Override
    public void handlePacket(Packet _p)
    {
        switch(_p.type)
        {
            case ChangeStatePacket:
                this.isCancel = true;
                break;
            case Unknown:
                break;
            default:
                break;
        }
    }
}
