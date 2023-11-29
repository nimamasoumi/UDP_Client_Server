package server;

public enum PacketType
{
    Unknown,    
    ChangeStatePacket,
    DisconnectPacket,
    ErrorPacket,
    MessagePacket,
    MovePacket,
    StateChangePacket
}