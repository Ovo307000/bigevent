package com.ovo307000.bigevent.config.configuation;

import com.ovo307000.bigevent.config.interceptor.LoginInterceptor;
import com.ovo307000.bigevent.config.properties.InterceptorProperties;
import com.ovo307000.bigevent.core.security.checker.PropertiesCheck;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration("webConfiguration")
public class WebConfiguration implements WebMvcConfigurer
{
    private static final Logger                log = LoggerFactory.getLogger(WebConfiguration.class);
    private final        LoginInterceptor      loginInterceptor;
    private final        InterceptorProperties interceptorProperties;
    private final        PropertiesCheck       propertiesCheck;

    public WebConfiguration(LoginInterceptor loginInterceptor,
                            InterceptorProperties interceptorProperties,
                            PropertiesCheck propertiesCheck)
    {
        this.loginInterceptor      = loginInterceptor;
        this.interceptorProperties = interceptorProperties;
        this.propertiesCheck       = propertiesCheck;
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry)
    {
        List<String> excludePathPatterns = this.interceptorProperties.getExcludePathPatterns();

        if (! this.propertiesCheck.checkPathFormat(excludePathPatterns))
        {
            throw new IllegalArgumentException("Exclude path patterns format error.");
        }

        log.info("Exclude path patterns: {}", excludePathPatterns);

        registry.addInterceptor(this.loginInterceptor)
                .excludePathPatterns(excludePathPatterns);
    }
}
