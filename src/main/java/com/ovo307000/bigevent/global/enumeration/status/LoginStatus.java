package com.ovo307000.bigevent.global.enumeration.status;

public enum LoginStatus implements Status
{
    // 用户不存在
    USER_NOT_EXISTS("User not exists", 1010),

    // 密码相关
    PASSWORD_INCORRECT("Password is incorrect", 1020),
    PASSWORD_INVALID("Password is invalid", 1021),
    PASSWORD_NOT_MATCH("Password does not match", 1022),

    // 安全问题相关
    SECURITY_QUESTION_INVALID("Security question is invalid", 1030),
    SECURITY_ANSWER_INCORRECT("Security answer is incorrect", 1040),

    // 账户相关
    ACCOUNT_LOCKED("Account is locked", 1050),

    // 成功
    SUCCESS("User login success", 200),

    // 失败
    FAILED("User login failure", 400);

    private final String  message;
    private final Integer code;

    LoginStatus(String message, Integer code)
    {
        this.message = message;
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
