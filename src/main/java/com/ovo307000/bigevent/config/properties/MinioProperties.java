package com.ovo307000.bigevent.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("minioProperties")
@ConfigurationProperties(prefix = "bigevent.minio")
public class MinioProperties
{
    private String endpoint;
    private String tokenValue;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public MinioProperties(String endpoint, String tokenValue, String accessKey, String secretKey, String bucketName)
    {
        this.endpoint   = endpoint;
        this.tokenValue = tokenValue;
        this.accessKey  = accessKey;
        this.secretKey  = secretKey;
        this.bucketName = bucketName;
    }

    public MinioProperties()
    {
    }

    public String getBucketName()
    {
        return this.bucketName;
    }

    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    public String getEndpoint()
    {
        return this.endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public String getTokenValue()
    {
        return this.tokenValue;
    }

    public void setTokenValue(String tokenValue)
    {
        this.tokenValue = tokenValue;
    }

    public String getAccessKey()
    {
        return this.accessKey;
    }

    public void setAccessKey(String accessKey)
    {
        this.accessKey = accessKey;
    }

    public String getSecretKey()
    {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }
}
