package com.ovo307000.bigevent.global.excaption;

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
