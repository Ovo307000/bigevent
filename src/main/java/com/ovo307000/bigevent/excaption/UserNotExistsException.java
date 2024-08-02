package com.ovo307000.bigevent.excaption;

public class UserNotExistsException extends IllegalArgumentException
{
    public UserNotExistsException(String message)
    {
        super(message);
    }

    public UserNotExistsException()
    {
        super();
    }
}
