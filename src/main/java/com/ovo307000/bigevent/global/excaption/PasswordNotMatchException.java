package com.ovo307000.bigevent.global.excaption;

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
