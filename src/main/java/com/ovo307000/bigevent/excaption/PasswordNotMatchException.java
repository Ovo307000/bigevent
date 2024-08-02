package com.ovo307000.bigevent.excaption;

public class PasswordNotMatchException extends IllegalArgumentException
{
    public PasswordNotMatchException(String message)
    {
        super(message);
    }

    public PasswordNotMatchException()
    {
        super();
    }
}
