package com.ovo307000.bigevent.core.constants.enumeration.status;

public enum UserStatus implements Status
{
    // User existence related
    USER_NOT_EXISTS(1000, "User does not exist"),
    USER_ALREADY_EXISTS(1001, "User already exists"),

    // Username related
    USERNAME_INVALID(1010, "Username is invalid"),
    USERNAME_ALREADY_EXISTS(1011, "Username already exists"),
    USERNAME_TOO_SHORT(1012, "Username is too short"),
    USERNAME_TOO_LONG(1013, "Username is too long"),
    USERNAME_CONTAINS_INVALID_CHARACTERS(1014, "Username contains invalid characters"),

    // Password related
    PASSWORD_INVALID(1020, "Password is invalid"),
    PASSWORD_TOO_SHORT(1021, "Password is too short"),
    PASSWORD_TOO_LONG(1022, "Password is too long"),
    PASSWORD_TOO_WEAK(1023, "Password is too weak"),
    PASSWORD_MISMATCH(1024, "Password does not match"),
    PASSWORD_CORRECT(1025, "Password is correct"),

    // Email related
    EMAIL_INVALID(1030, "Email is invalid"),
    EMAIL_ALREADY_EXISTS(1031, "Email already exists"),
    EMAIL_NOT_VERIFIED(1032, "Email not verified"),

    // Account status related
    ACCOUNT_LOCKED(1040, "Account is locked"),
    ACCOUNT_DISABLED(1041, "Account is disabled"),
    ACCOUNT_NOT_ACTIVATED(1042, "Account is not activated"),
    ACCOUNT_SUSPENDED(1043, "Account is suspended"),

    // Authentication related
    AUTHENTICATION_FAILED(1050, "Authentication failed"),
    AUTHENTICATION_REQUIRED(1051, "Authentication required"),
    TOKEN_EXPIRED(1052, "Token has expired"),
    TOKEN_INVALID(1053, "Token is invalid"),

    // Registration related
    REGISTRATION_SUCCESSFUL(1060, "Registration successful"),
    REGISTRATION_FAILED(1061, "Registration failed"),

    // Profile related
    PROFILE_UPDATE_SUCCESSFUL(1070, "Profile update successful"),
    PROFILE_UPDATE_FAILED(1071, "Profile update failed"),

    // User operation related
    SUCCESS(200, "User operation successful"),
    FAILED(400, "User operation failed"),

    // General errors
    UNKNOWN_ERROR(1099, "An unknown error occurred");


    private final Integer code;
    private final String  message;

    UserStatus(Integer code, String message)
    {
        this.code    = code;
        this.message = message;
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
