package com.ovo307000.bigevent.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration("interceptorProperties")
@ConfigurationProperties(prefix = "bigevent.interceptor")
public class InterceptorProperties
{
    private boolean enable;
    private List<String> excludePathPatterns;

    public boolean isEnable()
    {
        return this.enable;
    }

    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }

    public List<String> getExcludePathPatterns()
    {
        return this.excludePathPatterns;
    }

    public void setExcludePathPatterns(List<String> excludePathPatterns)
    {
        this.excludePathPatterns = excludePathPatterns;
    }
}
