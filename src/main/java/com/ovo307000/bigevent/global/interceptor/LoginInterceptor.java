package com.ovo307000.bigevent.global.interceptor;

import com.ovo307000.bigevent.global.properties.InterceptorProperties;
import com.ovo307000.bigevent.global.utils.JWTUtil;
import com.ovo307000.bigevent.global.utils.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

@Component("loginInterceptor")
public class LoginInterceptor implements HandlerInterceptor
{
    private static final Logger                  log = LoggerFactory.getLogger(LoginInterceptor.class);
    private final        JWTUtil                 jwtUtil;
    private final        InterceptorProperties   interceptorProperties;
    private final        ThreadLocalUtil<Claims> threadLocalUtil;

    public LoginInterceptor(JWTUtil jwtUtil,
                            InterceptorProperties interceptorProperties,
                            ThreadLocalUtil<Claims> threadLocalUtil)
    {
        this.jwtUtil               = jwtUtil;
        this.interceptorProperties = interceptorProperties;
        this.threadLocalUtil       = threadLocalUtil;
    }

    /**
     * 在处理请求之前进行预处理，主要用于检查JWT令牌的有效性
     *
     * @param request  当前的HTTP请求对象，用于获取请求头等信息
     * @param response 当前的HTTP响应对象，用于设置响应状态码
     * @param handler  当前请求的处理对象，未直接使用但不能为null
     *
     * @return 返回true表示放行请求，false表示拦截请求
     *
     * @throws JwtException 如果解析令牌失败，则抛出此异常
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws JwtException
    {
        // 检查配置，如果拦截器未启用，则直接放行
        if (! this.interceptorProperties.isEnable())
        {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取请求头中的Authorization信息，这是存放JWT令牌的地方
        String requestHeader = Objects.requireNonNull(request.getHeader("Authorization"), "Authorization is null");

        // 尝试验证和解析JWT令牌
        try
        {
            // 验证并解析令牌，如果成功，将claims（载荷）信息保存到线程局部变量中，以供后续使用
            final Claims claims = this.jwtUtil.verifyAndParseToken(requestHeader);
            this.threadLocalUtil.set(claims);

            // 设置响应状态码为200，表示请求有效，放行
            response.setStatus(HttpServletResponse.SC_OK);

            return true;
        }
        // 如果解析令牌失败，则捕获JwtException异常
        catch (JwtException jwtException)
        {
            // 设置响应状态码为401，表示请求无效（令牌无效），拦截
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return false;
        }
    }


    /**
     * 在请求处理完成后调用此方法，但在视图渲染之前（即Controller方法调用之后）
     * 该方法主要用于清理与当前请求关联的线程本地变量，以避免内存泄漏和数据污染
     *
     * @param request  HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于控制响应行为
     * @param handler  请求处理方法本身或其代理对象，可用于获取处理方法的相关信息
     * @param ex       在请求处理过程中可能抛出的异常，如果存在的话
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler,
                                Exception ex)
    {
        // 清理线程本地变量中的JWT载荷信息
        // 这一步是必要的，因为每个请求都可能包含不同的JWT，为避免内存泄漏和数据混淆，
        // 在处理完请求后应该清除与该请求相关的数据
        Optional.ofNullable(this.threadLocalUtil.get())
                .ifPresent((Claims claims) -> this.threadLocalUtil.remove());
    }
}
