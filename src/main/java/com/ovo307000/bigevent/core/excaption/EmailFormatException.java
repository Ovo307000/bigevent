package com.ovo307000.bigevent.core.excaption;

public class EmailFormatException extends IllegalArgumentException
{
    public EmailFormatException(String message)
    {
        super(message);
    }

    public EmailFormatException()
    {
        super();
    }
}
