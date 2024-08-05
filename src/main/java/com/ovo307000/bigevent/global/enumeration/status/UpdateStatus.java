package com.ovo307000.bigevent.global.enumeration.status;

public enum UpdateStatus implements Status
{
    USER_NOT_EXISTS(1000, "User does not exist"),

    USERNAME_INVALID(1010, "Username is invalid"),

    NICKNAME_INVALID(1020, "Nickname is invalid"),

    EMAIL_INVALID(1030, "Email is invalid"),

    SUCCESS(200, "Status update successfully"),

    FAILED(400, "Status update failed");

    private final String  message;
    private final Integer code;

    UpdateStatus(Integer code, String message)
    {
        this.code    = code;
        this.message = message;
    }

    @Override
    public Integer getCode()
    {
        return 0;
    }

    @Override
    public String getMassage()
    {
        return "";
    }
}
