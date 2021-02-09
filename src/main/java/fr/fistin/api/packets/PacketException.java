package fr.fistin.api.packets;

public class PacketException extends RuntimeException
{
    public PacketException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PacketException(String message, Object... format)
    {
        super(String.format(message, format));
    }

    public PacketException(String message, Throwable cause, Object... format)
    {
        super(String.format(message, format), cause);
    }
}
