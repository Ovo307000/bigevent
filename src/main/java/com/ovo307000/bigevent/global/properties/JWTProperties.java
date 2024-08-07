package com.ovo307000.bigevent.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("jwtProperties")
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties
{
    private String secret;
    private Long   expirationOfMinutes;

    public String getSecret()
    {
        return this.secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
    }

    public Long getExpirationOfMinutes()
    {
        return this.expirationOfMinutes;
    }

    public void setExpirationOfMinutes(Long expirationOfMinutes)
    {
        this.expirationOfMinutes = expirationOfMinutes;
    }
}
