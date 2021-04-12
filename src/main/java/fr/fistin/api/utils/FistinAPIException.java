package fr.fistin.api.utils;

public class FistinAPIException extends RuntimeException
{
    public FistinAPIException(String message)
    {
        super(message);
    }

    public FistinAPIException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public FistinAPIException(Throwable cause)
    {
        super(cause);
    }
}
