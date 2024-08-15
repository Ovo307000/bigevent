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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Validated
@RequestMapping("/user")
@RestController("userUserController")
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JWTUtil     jwtUtil;

    public UserController(@Qualifier(value = "userUserService") UserService userService, JWTUtil jwtUtil)
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

        return switch (this.userService.login(new UserDTO(username, password)))
        {
            case UserStatus.SUCCESS -> Result.success(UserStatus.SUCCESS, token);
            case UserStatus.PASSWORD_MISMATCH -> Result.fail(UserStatus.PASSWORD_MISMATCH, null);
            case UserStatus.USER_NOT_EXISTS -> Result.fail(UserStatus.USER_NOT_EXISTS, null);

            default -> Result.fail(UserStatus.FAILED, null);
        };
    }

    @GetMapping("/findUserByNickname")
    public Result<List<UserDTO>> findUserByNickname(@NotNull String nickname)
    {
        log.info("Finding user by nickname: {}", nickname);

        return Optional.ofNullable(this.userService.findUserByNickname(nickname))
                       .filter((List<UserDTO> users) -> ! users.isEmpty())
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.USER_NOT_EXISTS, null));
    }

    @GetMapping("/findUserByUsernameLikeIgnoreCase")
    public Result<List<UserDTO>> findUserByUsernameLikeIgnoreCase(@NotNull String username)
    {
        log.info("Finding user by username like: {}", username);

        return Optional.ofNullable(this.userService.findUserByUsernameLikeIgnoreCase(username))
                       .filter((List<UserDTO> users) -> ! users.isEmpty())
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.USER_NOT_EXISTS, null));
    }

    @GetMapping("/findUserByNicknameLikeIgnoreCase")
    public Result<List<UserDTO>> findUserByNicknameLikeIgnoreCase(@NotNull String nickname)
    {
        log.info("Finding user by nickname like: {}", nickname);

        return Optional.ofNullable(this.userService.findUserByNicknameLikeIgnoreCase(nickname))
                       .filter((List<UserDTO> users) -> ! users.isEmpty())
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.USER_NOT_EXISTS, null));
    }

    @GetMapping("/findUserByUsername")
    public Result<UserDTO> findUserByUsername(@NotNull String username)
    {
        log.info("Finding user by username: {}", username);

        return Optional.ofNullable(this.userService.findUserByUsername(username))
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.USER_NOT_EXISTS, null));
    }

    // TODO: 由于不启用拦截，无法获取到当前登录用户的信息，需要完善
    @GetMapping("/userInfo")
    public Result<UserDTO> userInfo(@RequestHeader("Authorization") String token)
    {
        log.info("Getting user info by token: {}", token);

        return Optional.ofNullable(this.userService.findUserByThreadLocal())
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.USER_NOT_EXISTS, null));
    }

    @PutMapping("/update")
    public Result<UserDTO> update(@RequestBody UserDTO user) throws NoSuchAlgorithmException
    {
        return Optional.ofNullable(this.userService.update(user))
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.FAILED, null));
    }

    @PatchMapping("/updateAvatar")
    public Result<UserDTO> updateAvatar(@RequestParam @NotNull String avatarUrl)
    {
        return Optional.ofNullable(this.userService.updateAvatar(avatarUrl))
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.FAILED, null));
    }

    @PatchMapping("/updatePwd")
    public Result<UserDTO> updatePassword(@RequestBody Map<String, Object> params) throws NoSuchAlgorithmException
    {
        String repeatPassword = (String) Objects.requireNonNull(params.get("re_pwd"),
                                                                UserStatus.PASSWORD_CANNOT_BE_EMPTY);
        String oldPassword = (String) Objects.requireNonNull(params.get("old_pwd"),
                                                             UserStatus.PASSWORD_CANNOT_BE_EMPTY);
        String newPassword = (String) Objects.requireNonNull(params.get("new_pwd"),
                                                             UserStatus.PASSWORD_CANNOT_BE_EMPTY);

        if (! StringUtils.hasLength(repeatPassword) ||
            ! StringUtils.hasLength(oldPassword) ||
            ! StringUtils.hasLength(newPassword))
        {
            return Result.fail(UserStatus.PASSWORD_CANNOT_BE_EMPTY, null);
        }

        return Optional.ofNullable(this.userService.updateUserPassword(newPassword, oldPassword, repeatPassword))
                       .map(Result::success)
                       .orElse(Result.fail(UserStatus.FAILED, null));
    }
}
