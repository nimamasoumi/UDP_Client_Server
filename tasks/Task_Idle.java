package tasks;

import server.IPacketListener;
import server.KUKAserver;
import server.data.KUKAAppStates;
import packets.StateChangePacket;
import utils.StateChanger;

/*This singletons class manages the server in the idle status.
 * It does nothing until there is command from the client.
 */
public class Task_Idle implements ITask{
    
    private KUKAserver medServer = KUKAserver.getInstance();
    public static boolean isCancel = false;
    private static IPacketListener listener = null;

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
        Task_Idle.listener = new StateChanger();    
        medServer.addListener(listener);
    }
    
    @Override
    public boolean run()
    {
        System.out.println("Running the IDLE task");
        if (!this.initialize())
        {
            return false;
        }
        // waiting for the commands from the client and doing nothing
        while(!Task_Idle.isCancel)
        {
            if(medServer.isClientDisconnected())
            {
                System.out.println("Client Disconnected.");
                isCancel = true;
                break;
            }

            try
            {
                Thread.sleep(100);
            }
            catch (Exception e){}            
        }
        return this.reset();
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
        System.out.println("Reseting the IDLE task controller.");
        medServer.removeListener(listener);
        isCancel = false;
        return true;
    }
}
