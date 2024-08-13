package com.ovo307000.bigevent.core.constants.enumeration.status;

public enum RegisterStatus implements Status
{
    USER_ALREADY_EXISTS("UserDTO already exists", 1000),
    USER_NOT_EXISTS("UserDTO does not exist", 1010),

    USERNAME_INVALID("Username is invalid", 1020),

    NICKNAME_INVALID("Nickname is invalid", 1030),

    PASSWORD_INVALID("Password is invalid", 1040),
    PASSWORD_NOT_MATCH("Password does not match", 1041),

    IP_INVALID("IP is invalid", 1050),

    EMAIL_INVALID("Email is invalid", 1060),
    EMAIL_EXISTS("Email exists", 1061),

    USERPIC_INVALID("UserDTO picture is invalid", 1070),

    SUCCESS("UserDTO register success", 200),

    FAILED("UserDTO register failure", 400);

    private final String  message;
    private final Integer code;

    RegisterStatus(String massage, Integer code)
    {
        this.message = massage;
        this.code    = code;
    }

    @Override
    public Integer getCode()
    {
        return this.code;
    }

    @Override
    public String getMessage()
    {
        return this.message;
    }
}
