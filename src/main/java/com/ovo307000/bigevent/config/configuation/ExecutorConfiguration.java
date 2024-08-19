package com.ovo307000.bigevent.config.configuation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration("asyncConfiguration")
public class ExecutorConfiguration
{
    @Bean
    public Executor taskExecutor()
    {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
