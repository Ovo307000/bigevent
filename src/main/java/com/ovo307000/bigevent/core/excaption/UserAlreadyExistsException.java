package com.ovo307000.bigevent.core.excaption;

public class UserAlreadyExistsException extends Exception
{
    public UserAlreadyExistsException(String message)
    {
        super(message);
    }

    public UserAlreadyExistsException()
    {
        super();
    }
}
