package server;

import com.google.gson.Gson;
import packets.*;

public class Packet
{
    public Packet(){};

    // packet type is the signal that we are communicating with the client
    public PacketType type = PacketType.Unknown;        

    // decoding a packet
    public String serialize()
    {
        // Using JSON string to encode the packet type
        var gson = new Gson();        
        return gson.toJson(this);
    }

    // encoding a packet
    public static Packet deserialize(String _message)
    {
        var gson = new Gson();
        var packet = gson.fromJson(_message, Packet.class);

        if (packet==null)
        {
            return null;
        }

        switch(packet.type)
        {
            case Unknown:
                break;

            case MessagePacket:
                packet = gson.fromJson(_message, MessagePacket.class);
                break;

            case DisconnectPacket:
                packet = gson.fromJson(_message, DisconnectPacket.class);
                break;

            default:
                break;
        }
        
        return packet;
    }
}