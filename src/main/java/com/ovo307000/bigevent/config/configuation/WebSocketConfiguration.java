package com.ovo307000.bigevent.config.configuation;

import com.ovo307000.bigevent.config.properties.WebsocketProperties;
import com.ovo307000.bigevent.core.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Objects;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer
{
    private final WebsocketProperties websocketProperties;

    public WebSocketConfiguration(WebsocketProperties websocketProperties)
    {
        this.websocketProperties = websocketProperties;
    }

    /**
     * 注册WebSocket处理器
     *
     * @param registry WebSocket处理器注册对象，用于注册WebSocket处理器和映射URL
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        // 获取WebSocket的端点URL，不能为空
        String endpoint = Objects.requireNonNull(this.websocketProperties.getEndpoint(),
                                                 "WebSocket endpoint must be set");

        // 注册自定义的WebSocket处理器，并映射到指定的URL路径
        registry.addHandler(new WebSocketHandler(), endpoint)
                // 设置允许访问的源，"*”表示允许所有源
                .setAllowedOrigins("*");
    }
}