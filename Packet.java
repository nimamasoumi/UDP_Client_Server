class Packet
{
    public Packet(){};

    public PacketType type = PacketType.Unknown;
    private String message;
    public String getMessage()
    {
        return this.message;
    }
    public void setMessage(String _message)
    {
        this.message = _message;
    }
    public String serialize()
    {
        return this.message;
    }
    public static Packet deserialize(String _message)
    {
        var packet = new Packet();
        return packet;
    }
}