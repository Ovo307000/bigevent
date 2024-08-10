package com.ovo307000.bigevent.global.interceptor;

import com.ovo307000.bigevent.global.properties.InterceptorProperties;
import com.ovo307000.bigevent.global.utils.JWTUtil;
import com.ovo307000.bigevent.global.utils.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component("loginInterceptor")
public class LoginInterceptor implements HandlerInterceptor
{
    private final JWTUtil                 jwtUtil;
    private final InterceptorProperties   interceptorProperties;
    private final ThreadLocalUtil<Claims> threadLocalUtil;

    public LoginInterceptor(JWTUtil jwtUtil,
                            InterceptorProperties interceptorProperties,
                            ThreadLocalUtil<Claims> threadLocalUtil)
    {
        this.jwtUtil               = jwtUtil;
        this.interceptorProperties = interceptorProperties;
        this.threadLocalUtil       = threadLocalUtil;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws JwtException
    {
        // 如果不需要拦截，直接放行
        if (! this.interceptorProperties.isEnable())
        {
            response.setStatus(200);
            return true;
        }

        String requestHeader = Objects.requireNonNull(request.getHeader("Authorization"), "Authorization is null");

        // 如果 token 有效，则返回 200
        try
        {
            final Claims claims = this.jwtUtil.verifyAndParseToken(requestHeader);

            this.threadLocalUtil.set(claims);

            response.setStatus(200);

            return true;
        }
        // 如果 token 无效，则返回 401
        catch (JwtException jwtException)
        {
            response.setStatus(401);

            return false;
        }
    }
}
