package com.ovo307000.bigevent.excaption;

public class NicknameInvalidException extends IllegalArgumentException
{
    NicknameInvalidException(String message)
    {
        super(message);
    }

    NicknameInvalidException()
    {
        super();
    }
}
