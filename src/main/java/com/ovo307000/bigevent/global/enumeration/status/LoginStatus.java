package com.ovo307000.bigevent.global.enumeration.status;

public enum LoginStatus
{
    // 用户不存在
    USER_NOT_EXISTS("User not exists", 1010),

    // 密码相关
    PASSWORD_INCORRECT("Password is incorrect", 1020),
    PASSWORD_INVALID("Password is invalid", 1021),

    // 安全问题相关
    SECURITY_QUESTION_INVALID("Security question is invalid", 1030),
    SECURITY_ANSWER_INCORRECT("Security answer is incorrect", 1040),

    // 账户相关
    ACCOUNT_LOCKED("Account is locked", 1050),

    // 成功
    SUCCESS("Success", 2000),

    // 其他
    OTHER("Other", 1999);

    private final String  message;
    private final Integer code;

    LoginStatus(String message, Integer code)
    {
        this.message = message;
        this.code    = code;
    }

    public String getMessage()
    {
        return this.message;
    }

    public Integer getCode()
    {
        return this.code;
    }
}
