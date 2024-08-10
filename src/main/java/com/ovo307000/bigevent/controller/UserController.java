package com.ovo307000.bigevent.controller;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.global.enumeration.status.LoginStatus;
import com.ovo307000.bigevent.global.enumeration.status.RegisterStatus;
import com.ovo307000.bigevent.global.result.Result;
import com.ovo307000.bigevent.global.surety.encryptor.SHA256Encrypted;
import com.ovo307000.bigevent.global.utils.JWTUtil;
import com.ovo307000.bigevent.service.UserService;
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
@RestController("userController")
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JWTUtil     jwtUtil;

    public UserController(@Qualifier("userService") UserService userService, JWTUtil jwtUtil)
    {
        this.userService = userService;
        this.jwtUtil     = jwtUtil;
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

    @PutMapping("/updateUser")
    public Result<?> updateUser(@NotNull String username,
                                String password,
                                String nickname,
                                String email,
                                String userPic)
    {
        log.info("Updating user: {}", username);

        return switch (this.userService.updateUser(new User(username, password, nickname, email, userPic)))
        {
            case RegisterStatus.SUCCESS -> Result.success("User updated successfully");
            case RegisterStatus.USER_ALREADY_EXISTS -> Result.fail(RegisterStatus.USER_ALREADY_EXISTS.getMessage());

            default -> Result.fail(RegisterStatus.FAILED.getMessage());
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

    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader("Authorization") String token)
    {
        log.info("Getting user info by token: {}", token);

        return Optional.ofNullable(this.userService.queryCurrentUserInfo(token))
                       .map(Result::success)
                       .orElse(Result.fail(RegisterStatus.USER_NOT_EXISTS.getMessage(), null));
    }
}
