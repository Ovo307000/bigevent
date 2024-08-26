package com.ovo307000.bigevent.core.handler;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler
{
    private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);

    /**
     * 在WebSocket连接建立后调用的方法
     * 主要用于记录连接建立的日志信息
     *
     * @param session 建立的WebSocket会话，不为null
     *
     * @throws Exception 如果处理过程中出现异常，则抛出
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception
    {
        log.info("Connection established: {}", session);
    }

    /**
     * 处理接收到的文本消息
     *
     * @param session 建立的WebSocket会话，用于识别和管理WebSocket连接
     * @param message 接收到的文本消息，包含消息内容
     *
     * @throws Exception 如果处理消息时发生错误，抛出异常
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws Exception
    {
        // 记录接收到的消息内容
        log.info("Received message: {}", message.getPayload());
    }


    /**
     * 当WebSocket连接关闭时执行的方法
     *
     * @param session 唯一的WebSocketSession对象，表示与特定客户端的连接会话
     * @param status  与关闭连接相关的CloseStatus对象，提供了关闭的原因和状态码
     *
     * @throws Exception 如果方法执行中出现异常，则抛出
     */
    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception
    {
        log.info("Connection closed: {}", session);
    }
}
