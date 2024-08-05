package com.ovo307000.bigevent.global.excaption;

public class EmailFormatException extends IllegalArgumentException
{
    public EmailFormatException(String message)
    {
        super(message);
    }

    EmailFormatException()
    {
        super();
    }
}
