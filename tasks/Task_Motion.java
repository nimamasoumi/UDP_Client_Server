package tasks;

import server.KUKAserver;
import server.data.KUKAAppStates;
import utils.MotionHandler;
import packets.StateChangePacket;
import packets.ErrorPacket;
import server.IPacketListener;

public class Task_Motion implements ITask
{
    private static volatile Task_Motion instance = null;
    private Task_Motion(){}
    public static Task_Motion getInstance()
    {
        if(instance == null)
        {
            synchronized(Task_Motion.class)
            {
                if(instance == null)
                {
                    instance = new Task_Motion();
                }
            }
        }
        return instance;
    }

    private KUKAserver medServer = KUKAserver.getInstance();
    public static boolean isCancel = false;
    private static IPacketListener listener = null;

    private void addPacketHandler()
    {
        Task_Motion.listener = new MotionHandler();
        medServer.addListener(listener);
    }

    @Override
    public boolean initialize()
    {
        System.out.println("Initializing the motion task manager.");
        // in the app, ControlManager should be initialized
        boolean isMotionManagerInitialized = true; 
        if (isMotionManagerInitialized)
        {
            addPacketHandler();
            medServer.send(new StateChangePacket(KUKAAppStates.BASIC_MOVE));
            return true;
        }
        else
        {
            medServer.send(new ErrorPacket("Could not start the basic move session."));
            isCancel = true;
            return false;
        }        
    }

    @Override
    public boolean run()
    {
        System.out.println("Running the motion task.");
        if(!this.initialize())
        {
            return false;
        }
        while(isCancel)
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
    public boolean reset()
    {
        System.out.println("Reseting the the motion task manager.");
        medServer.removeListener(listener);
        isCancel = false;
        return true;
    }
}
