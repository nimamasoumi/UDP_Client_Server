package packets;

import server.Packet;
import server.PacketType;

public class MovePacket extends Packet
{    
    /**
     * Tip offset from flange
     */
    //public Transform    tip     = new Transform();

    /**
     * Desired pose for the end-effector. Pose orientation is a Quaternion,
     * robot should convert to appropriate a,b,c
     */
    //public Transform    pose    = new Transform();

    /**
     * Desired controller type for motion
     */
    //public ControlTypes control = ControlTypes.SmartServo;

    /**
     * Robot application speed override (0 - 1)
     */
    public float        speed   = 0.1f;
    
    public MovePacket()
    {
        this.type = PacketType.MovePacket;
    }

    // public MovePacket(Transform t) {
    //     this.type = PacketType.MovePacket;
    //     this.pose = t;
    // }
}
