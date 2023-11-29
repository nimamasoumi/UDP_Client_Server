package utils;

import server.IPacketListener;
import server.Packet;
import tasks.Task_Motion;

public class MotionHandler implements IPacketListener
{
    @Override
    public void handlePacket(Packet _p)
    {
        switch(_p.type)
        {
            case ChangeStatePacket:
                Task_Motion.isCancel = true;
                break;
            case MovePacket:
                // MoveCommand cmd = appTask.buildMoveCommand((MovePacket) _p);
                // appTask.handleMove(cmd);
                break;
            case Unknown:
            default:
                break;
        }
        
    }    
}
