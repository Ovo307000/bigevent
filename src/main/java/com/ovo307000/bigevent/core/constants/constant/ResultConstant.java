package com.ovo307000.bigevent.core.constants.constant;

public enum ResultConstant
{
    USER_ALREADY_EXISTS("User already exists"),
    USER_NOT_EXISTS("User does not exist"),
    USERNAME_INVALID("Username is invalid"),
    NICKNAME_INVALID("Nickname is invalid"),
    PASSWORD_INVALID("Password is invalid"),
    PASSWORD_NOT_MATCH("Password does not match"),
    EMAIL_INVALID("Email is invalid"),
    USERPIC_INVALID("User picture is invalid"),
    PASSWORD_INCORRECT("Password is incorrect");

    private final String message;

    ResultConstant(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }
}
