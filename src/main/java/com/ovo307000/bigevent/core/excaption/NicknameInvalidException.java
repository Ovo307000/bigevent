package com.ovo307000.bigevent.core.excaption;

public class NicknameInvalidException extends IllegalArgumentException
{
    public NicknameInvalidException(String message)
    {
        super(message);
    }

    public NicknameInvalidException()
    {
        super();
    }
}
