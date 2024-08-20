package com.ovo307000.bigevent.config.interceptor;

import com.ovo307000.bigevent.core.utils.JWTUtil;
import com.ovo307000.bigevent.core.utils.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component("loginInterceptor")
public class LoginInterceptor implements HandlerInterceptor
{

    private static final Logger log         = LoggerFactory.getLogger(LoginInterceptor.class);
    private static final String AUTH_HEADER = "Authorization";

    private static long START_TIME = System.currentTimeMillis();

    private final JWTUtil                 jwtUtil;
    private final ThreadLocalUtil<Claims> threadLocalUtil;
    private final StringRedisTemplate     stringRedisTemplate;

    public LoginInterceptor(JWTUtil jwtUtil,
                            ThreadLocalUtil<Claims> threadLocalUtil,
                            StringRedisTemplate stringRedisTemplate)
    {
        this.jwtUtil             = jwtUtil;
        this.threadLocalUtil     = threadLocalUtil;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws JwtException
    {
        String token = Optional.of(request.getHeader(AUTH_HEADER))
                               .orElseThrow((() ->
                                             {
                                                 log.error("Authorization header is missing");

                                                 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                                                 return new JwtException("Authorization header is missing");
                                             }));

        try
        {
            Claims claims   = this.jwtUtil.verifyAndParseToken(token);
            String username = claims.get("username", String.class);

            if (! this.isUserValid(username))
            {
                log.error("User not found in Redis: {}", username);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            this.threadLocalUtil.set(claims);
            log.debug("Token validated successfully for user: {}", username);
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        catch (JwtException e)
        {
            log.error("JWT verification failed: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private boolean isUserValid(String username)
    {
        return this.stringRedisTemplate.opsForValue()
                                       .get(username) != null;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler,
                                Exception ex)
    {
        this.threadLocalUtil.remove();
        log.info("Request completed in {} ms", this.getProcessingTime());
    }

    /**
     * 获取自上次调用以来的处理时间
     * <p>
     * 此方法用于计算自上一次调用以来所经过的时间，单位为毫秒
     * 它通过记录当前时间和开始时间的差值来实现
     *
     * @return 自上次调用以来经过的时间，以毫秒为单位
     */
    private long getProcessingTime()
    {
        // 记录当前时间
        long currentTime = System.currentTimeMillis();
        // 计算并存储自上次调用以来经过的时间
        long elapsedTime = currentTime - START_TIME;
        // 更新开始时间为当前时间，为下一次调用做准备
        START_TIME = currentTime;
        // 返回经过的时间
        return elapsedTime;
    }
}
