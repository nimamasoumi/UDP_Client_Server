class Packet
{
    public Packet(){};

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
}