package com.ovo307000.bigevent.core.constants.enumeration.status;

public class UserStatus
{
    // User existence related
    public static final String USER_NOT_EXISTS = "User does not exist";
    public static final String USER_ALREADY_EXISTS = "User already exists";

    // Username related
    public static final String USERNAME_INVALID = "Username is invalid";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String USERNAME_TOO_SHORT = "Username is too short";
    public static final String USERNAME_TOO_LONG = "Username is too long";
    public static final String USERNAME_CONTAINS_INVALID_CHARACTERS = "Username contains invalid characters";

    // Password related
    public static final String PASSWORD_INVALID = "Password is invalid";
    public static final String PASSWORD_TOO_SHORT = "Password is too short";
    public static final String PASSWORD_TOO_LONG = "Password is too long";
    public static final String PASSWORD_TOO_WEAK = "Password is too weak";
    public static final String PASSWORD_MISMATCH = "Password does not match";
    public static final String PASSWORD_CORRECT = "Password is correct";
    public static final String PASSWORD_CANNOT_BE_EMPTY = "Password cannot be empty";

    // Email related
    public static final String EMAIL_INVALID = "Email is invalid";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String EMAIL_NOT_VERIFIED = "Email not verified";

    // Account status related
    public static final String ACCOUNT_LOCKED = "Account is locked";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    public static final String ACCOUNT_NOT_ACTIVATED = "Account is not activated";
    public static final String ACCOUNT_SUSPENDED = "Account is suspended";

    // Authentication related
    public static final String AUTHENTICATION_FAILED = "Authentication failed";
    public static final String AUTHENTICATION_REQUIRED = "Authentication required";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_INVALID = "Token is invalid";

    // Registration related
    public static final String REGISTRATION_SUCCESSFUL = "Registration successful";
    public static final String REGISTRATION_FAILED = "Registration failed";

    // Profile related
    public static final String PROFILE_UPDATE_SUCCESSFUL = "Profile update successful";
    public static final String PROFILE_UPDATE_FAILED = "Profile update failed";

    // User operation related
    public static final String SUCCESS = "User operation successful";
    public static final String FAILED = "User operation failed";

    // General errors
    public static final String UNKNOWN_ERROR = "An unknown error occurred";
}
