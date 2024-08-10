package com.ovo307000.bigevent.core.excaption;

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
