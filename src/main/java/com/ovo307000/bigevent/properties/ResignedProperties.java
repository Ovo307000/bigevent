package com.ovo307000.bigevent.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("resignedProperties")
@ConfigurationProperties(prefix = "resigned")
public class ResignedProperties
{
    private Integer passwordLength;
    private Integer nicknameLength;
    private String  defaultPassword;


    public ResignedProperties()
    {
    }

    public ResignedProperties(Integer passwordLength, Integer nicknameLength, String defaultPassword)
    {
        this.passwordLength  = passwordLength;
        this.nicknameLength  = nicknameLength;
        this.defaultPassword = defaultPassword;
    }

    public Integer getPasswordLength()
    {
        return this.passwordLength;
    }

    public void setPasswordLength(Integer passwordLength)
    {
        this.passwordLength = passwordLength;
    }

    public Integer getNicknameLength()
    {
        return this.nicknameLength;
    }

    public void setNicknameLength(Integer nicknameLength)
    {
        this.nicknameLength = nicknameLength;
    }

    public String getDefaultPassword()
    {
        return this.defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword)
    {
        this.defaultPassword = defaultPassword;
    }
}
