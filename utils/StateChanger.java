package utils;

import server.IPacketListener;
import server.Packet;
import tasks.Task_Idle;

public class StateChanger implements IPacketListener{

    //private Task_Idle taskIdle = Task_Idle.getInstance();

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
                Task_Idle.isCancel = true;
                break;
            case Unknown:
            default:
                break;
        }
    }
}
