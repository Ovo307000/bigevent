package com.ovo307000.bigevent.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("resignedProperties")
@ConfigurationProperties(prefix = "resigned")
public class ResignedProperties
{
    private String usernameRegex;
    private String nicknameRegex;
    private String emailRegex;
    private String userPicRegex;
    private String passwordRegex;

    public String getUsernameRegex()
    {
        return this.usernameRegex;
    }

    public void setUsernameRegex(String usernameRegex)
    {
        this.usernameRegex = usernameRegex;
    }

    public String getNicknameRegex()
    {
        return this.nicknameRegex;
    }

    public void setNicknameRegex(String nicknameRegex)
    {
        this.nicknameRegex = nicknameRegex;
    }

    public String getEmailRegex()
    {
        return this.emailRegex;
    }

    public void setEmailRegex(String emailRegex)
    {
        this.emailRegex = emailRegex;
    }

    public String getUserPicRegex()
    {
        return this.userPicRegex;
    }

    public void setUserPicRegex(String userPicRegex)
    {
        this.userPicRegex = userPicRegex;
    }

    public String getPasswordRegex()
    {
        return this.passwordRegex;
    }

    public void setPasswordRegex(String passwordRegex)
    {
        this.passwordRegex = passwordRegex;
    }
}
