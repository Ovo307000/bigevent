package com.ovo307000.bigevent.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("jwtProperties")
@ConfigurationProperties(prefix = "bigevent.jwt")
public class JWTProperties
{
    private String secret;
    private Long   expirationOfSeconds;
    private String algorithmNameUpperCase;

    public String getSecret()
    {
        return this.secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
    }

    public Long getExpirationOfSeconds()
    {
        return this.expirationOfSeconds;
    }


    public void setExpirationOfSeconds(Long expirationOfSeconds)
    {
        this.expirationOfSeconds = expirationOfSeconds;
    }

    public String getAlgorithmNameUpperCase()
    {
        return this.algorithmNameUpperCase;
    }

    public void setAlgorithmNameUpperCase(String algorithmNameUpperCase)
    {
        this.algorithmNameUpperCase = algorithmNameUpperCase;
    }
}
