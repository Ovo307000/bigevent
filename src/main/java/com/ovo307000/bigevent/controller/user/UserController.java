package com.ovo307000.bigevent.controller.user;

import com.ovo307000.bigevent.config.properties.InterceptorProperties;
import com.ovo307000.bigevent.core.constants.enumeration.status.LoginStatus;
import com.ovo307000.bigevent.core.constants.enumeration.status.RegisterStatus;
import com.ovo307000.bigevent.core.constants.enumeration.status.UpdateStatus;
import com.ovo307000.bigevent.core.security.encryptor.SHA256Encrypted;
import com.ovo307000.bigevent.core.utils.JWTUtil;
import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.response.Result;
import com.ovo307000.bigevent.service.user.UserService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Validated
@RequestMapping("/user")
@RestController("userUserController")
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService           userService;
    private final JWTUtil               jwtUtil;
    private final InterceptorProperties interceptorProperties;

    public UserController(@Qualifier(value = "userUserService") UserService userService,
                          JWTUtil jwtUtil,
                          InterceptorProperties interceptorProperties)
    {
        this.userService           = userService;
        this.jwtUtil               = jwtUtil;
        this.interceptorProperties = interceptorProperties;
    }

    /**
     * 注册用户
     *
     * @param username 用户名，格式为3-20位的字母、数字、下划线
     * @param password 密码，格式为8-20位的字母、数字、特殊字符，必须包含大小写字母、数字和特殊字符
     *
     * @return 注册结果
     *
     * @throws NoSuchAlgorithmException 加密算法不存在
     */
    @PostMapping("/register")
    public Result<?> register(@NotNull String username, @NotNull String password) throws NoSuchAlgorithmException
    {
        log.info("Registering user: {}", username);

        return switch (this.userService.register(new User(username, password)))
        {
            case RegisterStatus.SUCCESS -> Result.success(RegisterStatus.SUCCESS.getMessage());
            case RegisterStatus.USER_ALREADY_EXISTS -> Result.fail(RegisterStatus.USER_ALREADY_EXISTS.getMessage());

            default -> Result.fail(RegisterStatus.FAILED.getMessage());
        };
    }

    @PostMapping("/login")
    public Result<?> login(@NotNull String username, @NotNull String password) throws NoSuchAlgorithmException
    {
        log.info("Trying to login user: {}", username);

        String token = this.jwtUtil.generateToken(Map.of("username",
                                                         username,
                                                         "password",
                                                         SHA256Encrypted.encrypt(password)));

        log.debug("Token generated: {}", token);

        return switch (this.userService.login(new User(username, password)))
        {
            case LoginStatus.SUCCESS -> Result.success(LoginStatus.SUCCESS.getMessage(), token);
            case LoginStatus.PASSWORD_NOT_MATCH -> Result.fail(LoginStatus.PASSWORD_NOT_MATCH.getMessage());
            case LoginStatus.USER_NOT_EXISTS -> Result.fail(LoginStatus.USER_NOT_EXISTS.getMessage());

            default -> Result.fail(LoginStatus.FAILED.getMessage());
        };
    }

    @GetMapping("/findUserByNickname")
    public Result<?> findUserByNickname(@NotNull String nickname)
    {
        log.info("Finding user by nickname: {}", nickname);

        return Optional.ofNullable(this.userService.findUserByNickname(nickname))
                       .filter((List<User> users) -> ! users.isEmpty())
                       .map(Result::success)
                       .orElse(Result.fail(RegisterStatus.USER_NOT_EXISTS.getMessage(), null));
    }

    @GetMapping("/findUserByUsernameLikeIgnoreCase")
    public Result<?> findUserByUsernameLikeIgnoreCase(@NotNull String username)
    {
        log.info("Finding user by username like: {}", username);

        return Optional.ofNullable(this.userService.findUserByUsernameLikeIgnoreCase(username))
                       .filter((List<User> users) -> ! users.isEmpty())
                       .map(Result::success)
                       .orElse(Result.fail(RegisterStatus.USER_NOT_EXISTS.getMessage(), null));
    }

    @GetMapping("/findUserByNicknameLikeIgnoreCase")
    public Result<?> findUserByNicknameLikeIgnoreCase(@NotNull String nickname)
    {
        log.info("Finding user by nickname like: {}", nickname);

        return Optional.ofNullable(this.userService.findUserByNicknameLikeIgnoreCase(nickname))
                       .filter((List<User> users) -> ! users.isEmpty())
                       .map(Result::success)
                       .orElse(Result.fail(RegisterStatus.USER_NOT_EXISTS.getMessage(), null));
    }

    @GetMapping("/findUserByUsername")
    public Result<?> findUserByUsername(@NotNull String username)
    {
        log.info("Finding user by username: {}", username);

        return Optional.ofNullable(this.userService.findUserByUsername(username))
                       .map(Result::success)
                       .orElse(Result.fail(RegisterStatus.USER_NOT_EXISTS.getMessage(), null));
    }

    // TODO: 由于不启用拦截，无法获取到当前登录用户的信息，需要完善
    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader("Authorization") String token)
    {
        log.info("Getting user info by token: {}", token);

        if (this.interceptorProperties.isEnable())
        {
            return Optional.ofNullable(this.userService.queryCurrentUserInfo())
                           .map(Result::success)
                           .orElse(Result.fail(RegisterStatus.USER_NOT_EXISTS.getMessage(), null));
        }
        else
        {
            return Optional.ofNullable(this.userService.queryCurrentUserInfo(token))
                           .map(Result::success)
                           .orElse(Result.fail(RegisterStatus.USER_NOT_EXISTS.getMessage(), null));
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody User user)
    {
        if (this.userService.update(user))
        {
            return Result.success(UpdateStatus.SUCCESS.getMessage());
        }
        else
        {
            return Result.fail(UpdateStatus.FAILED.getMessage());
        }
    }
}
