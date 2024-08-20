package com.ovo307000.bigevent.controller.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.UserStatus;
import com.ovo307000.bigevent.core.utils.JWTUtil;
import com.ovo307000.bigevent.entity.dto.UserDTO;
import com.ovo307000.bigevent.response.Result;
import com.ovo307000.bigevent.service.user.UserService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Validated
@RequestMapping("/user")
@RestController("userUserController")
public class UserController
{
    private static final Logger log         = LoggerFactory.getLogger(UserController.class);
    private static final String AUTH_HEADER = "Authorization";

    private final UserService         userService;
    private final JWTUtil             jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public UserController(@Qualifier(value = "userUserService") UserService userService,
                          JWTUtil jwtUtil,
                          StringRedisTemplate stringRedisTemplate)
    {
        this.userService         = userService;
        this.jwtUtil             = jwtUtil;
        this.stringRedisTemplate = stringRedisTemplate;
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

        return switch (this.userService.register(new UserDTO(username, password)))
        {
            case UserStatus.SUCCESS -> Result.success(UserStatus.SUCCESS);
            case UserStatus.USER_ALREADY_EXISTS -> Result.fail(UserStatus.USER_ALREADY_EXISTS);

            default -> Result.fail(UserStatus.FAILED);
        };
    }

    @PostMapping("/login")
    public Result<String> login(@NotNull String username, @NotNull String password) throws NoSuchAlgorithmException
    {
        log.info("Trying to login user: {}", username);

        String token = this.jwtUtil.generateTokenByUsernameAndPassword(username, password);

        log.debug("Token generated: {}", token);

        this.saveUserTokenToRedis(username, token);

        return switch (this.userService.login(new UserDTO(username, password)))
        {
            case UserStatus.SUCCESS -> Result.success(UserStatus.SUCCESS, token);
            case UserStatus.PASSWORD_MISMATCH -> Result.fail(UserStatus.PASSWORD_MISMATCH, null);
            case UserStatus.USER_NOT_EXISTS -> Result.fail(UserStatus.USER_NOT_EXISTS, null);

            default -> Result.fail(UserStatus.FAILED, null);
        };
    }

    private void saveUserTokenToRedis(String username, String token)
    {
        this.stringRedisTemplate.opsForValue()
                                .set(username, token, 72, TimeUnit.HOURS);
    }

    @GetMapping("/findUserByNickname")
    public Result<List<UserDTO>> findUserByNickname(@NotNull String nickname)
    {
        log.info("Finding user by nickname: {}", nickname);

        return this.processFindResultList(this.userService.findUserByNickname(nickname));
    }

    private <T> Result<List<T>> processFindResultList(List<T> result)
    {
        return result.isEmpty() ? Result.fail(UserStatus.USER_NOT_EXISTS, null) : Result.success(result);
    }

    @GetMapping("/findUserByUsernameLikeIgnoreCase")
    public Result<List<UserDTO>> findUserByUsernameLikeIgnoreCase(@NotNull String username)
    {
        log.info("Finding user by username like: {}", username);

        return this.processFindResultList(this.userService.findUserByUsernameLikeIgnoreCase(username));
    }

    @GetMapping("/findUserByNicknameLikeIgnoreCase")
    public Result<List<UserDTO>> findUserByNicknameLikeIgnoreCase(@NotNull String nickname)
    {
        log.info("Finding user by nickname like: {}", nickname);

        return this.processFindResultList(this.userService.findUserByNicknameLikeIgnoreCase(nickname));
    }

    @GetMapping("/findUserByUsername")
    public Result<UserDTO> findUserByUsername(@NotNull String username)
    {
        log.info("Finding user by username: {}", username);

        return this.processFindResult(this.userService.findUserByUsername(username));
    }

    private <T> Result<T> processFindResult(T result)
    {
        return Optional.ofNullable(result)
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.FAILED, null));
    }

    @GetMapping("/userInfo")
    public Result<UserDTO> userInfo(@RequestHeader(AUTH_HEADER) String token)
    {
        log.info("Getting user info by token: {}", token);

        return this.processFindResult(this.userService.findUserByThreadLocal());
    }

    @PutMapping("/update")
    public Result<UserDTO> update(@RequestBody @Validated(UserDTO.Update.class) UserDTO user)
            throws NoSuchAlgorithmException
    {
        log.info("Updating user: {}", user);

        return this.processFindResult(this.userService.update(user));
    }

    @PatchMapping("/updateAvatar")
    public Result<UserDTO> updateAvatar(@RequestParam @NotNull String avatarUrl)
    {
        log.info("Updating user avatar: {}", avatarUrl);

        return this.processFindResult(this.userService.updateAvatar(avatarUrl));
    }

    /**
     * 通过PATCH请求更新用户密码
     *
     * @param params 包含旧密码、新密码和重复新密码的参数映射
     *
     * @return 更新操作的结果和用户数据传输对象
     *
     * @throws NoSuchAlgorithmException 如果密码加密算法不存在
     */
    @PatchMapping("/updatePwd")
    public Result<UserDTO> updatePassword(@RequestBody Map<String, Object> params) throws NoSuchAlgorithmException
    {
        // 从参数中获取密码信息
        String oldPassword    = (String) params.get("old_pwd");
        String newPassword    = (String) params.get("new_pwd");
        String repeatPassword = (String) params.get("re_pwd");

        // 验证输入的密码是否符合要求
        if (! this.isPasswordInputValid(oldPassword, newPassword, repeatPassword))
        {
            // 如果密码输入不合法，返回失败结果
            return Result.fail(UserStatus.PASSWORD_CANNOT_BE_EMPTY, null);
        }

        // 调用用户服务来更新密码
        UserDTO result = this.userService.updateUserPassword(newPassword, oldPassword, repeatPassword);
        // 删除用户在Redis中的令牌，因为密码更新了，令牌失效，务必要在更新完成后删除，否则如果更新时抛出异常，令牌删除，导致拦截器无法正常放行
        this.deleteUserTokenFromRedis(Objects.requireNonNull(this.userService.findUserByThreadLocal())
                                             .getUsername());
        // 处理并返回更新结果
        return this.processFindResult(result);
    }

    private boolean isPasswordInputValid(@NotNull String... passwords)
    {
        return Arrays.stream(passwords)
                     .allMatch(StringUtils::hasLength);
    }

    private void deleteUserTokenFromRedis(String username)
    {
        this.stringRedisTemplate.opsForValue()
                                .getOperations()
                                .delete(username);
    }
}
