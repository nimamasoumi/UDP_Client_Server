package tasks;

import server.IPacketListener;
import server.KUKAserver;
import server.data.KUKAAppStates;
import packets.StateChangePacket;
import utils.StateChanger;

public class Task_Idle implements ITask{
    
    private KUKAserver medServer = KUKAserver.getInstance();
    private boolean isCancel = false;
    private IPacketListener listener = null;

    private static volatile Task_Idle instance = null;

    private Task_Idle(){}

    public static Task_Idle getInstance()
    {
        if(instance == null)
        {
            synchronized(Task_Idle.class)
            {
                if(instance == null)
                {
                    instance = new Task_Idle();
                }
            }
        }
        return instance;
    }
    
    private void addPacketHandler()
    {
        this.listener = new StateChanger();    
    }
    
    @Override
    public boolean run()
    {

    }

    @Override
    public boolean initialize()
    {
        System.out.println("Initializing the idle state task manager.");
        addPacketHandler();
        medServer.send(new StateChangePacket(KUKAAppStates.IDLE));
        return true;
    }

    @Override
    public boolean reset()
    {

    }
}
