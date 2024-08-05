package com.ovo307000.bigevent.global.excaption;

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
