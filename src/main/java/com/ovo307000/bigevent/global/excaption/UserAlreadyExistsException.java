package com.ovo307000.bigevent.global.excaption;

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
