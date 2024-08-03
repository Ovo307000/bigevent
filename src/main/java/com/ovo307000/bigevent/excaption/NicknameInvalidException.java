package com.ovo307000.bigevent.excaption;

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
