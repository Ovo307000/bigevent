package com.ovo307000.bigevent.controller;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.excaption.PasswordNotMatchException;
import com.ovo307000.bigevent.excaption.UserAlreadyExistsException;
import com.ovo307000.bigevent.excaption.UserNotExistsException;
import com.ovo307000.bigevent.result.Result;
import com.ovo307000.bigevent.service.UserService;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Validated
@RequestMapping("/user")
@RestController("userController")
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(@Qualifier("userService") UserService userService)
    {
        this.userService = userService;
    }

    /**
     * 注册用户
     *
     * @param username 用户名，格式为3-20位的字母、数字、下划线
     * @param password 密码，格式为8-20位的字母、数字、特殊字符，必须包含大小写字母、数字和特殊字符
     *
     * @return 注册结果
     *
     * @throws NoSuchAlgorithmException   加密算法不存在
     * @throws UserAlreadyExistsException 用户已存在
     */
    @PostMapping("/register")
    public Result<?> register(
            @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "Invalid username format.") String username,

            @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
                     message = "Password must be 8-20 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
            String password) throws NoSuchAlgorithmException, UserAlreadyExistsException
    {
        log.info("Registering user: {}", username);

        this.userService.register(new User(username, password));

        return Result.success();
    }


    /*
     * 采用用户名和密码登录，因为在应用层出错后，会抛出异常，所以在这采用try-catch捕获异常
     * */
    @PostMapping("/login")
    public Result<?> login(String username, String password)
    {
        log.info("Trying to login user: {}", username);

        try
        {
            log.info("Logging in user: {}", username);

            this.userService.login(new User(username, password));
        }
        catch (PasswordNotMatchException passwordNotMatchException)
        {
            log.error("Login failed due to incorrect password: {}", passwordNotMatchException.getMessage());

            return Result.fail(passwordNotMatchException.getMessage());
        }
        catch (UserNotExistsException userNotExistsException)
        {
            log.error("Login failed due to user not exists: {}", userNotExistsException.getMessage());

            return Result.fail(userNotExistsException.getMessage());
        }

        log.info("Login successful: {}", username);
        return Result.success();
    }

    /**
     * 更新用户信息
     *
     * @param username 用户名，格式为3-20位的字母、数字、下划线
     * @param password 密码，格式为8-20位的字母、数字、特殊字符，必须包含大小写字母、数字和特殊字符
     * @param nickname 昵称，格式为3-20位的字母、数字、下划线、中文
     * @param email    邮箱，格式为邮箱格式
     * @param userPic  用户头像，格式为图片URL
     *
     * @return 更新结果
     */
    @PutMapping("/updateUser")
    public Result<?> updateUser(
            @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "Invalid username format.") String username,

            @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
                     message = "Password must be 8-20 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
            String password,

            @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5 ]{3,20}$", message = "Invalid nickname format.")
            String nickname,

            @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email format.") String email,

            @Pattern(regexp = "^(http(s?):)([/|.\\w\\s-])*\\.(?:jpg|gif|png)$", message = "Invalid image URL format.")
            String userPic)
    {
        log.info("Updating user: {}", username);

        this.userService.updateUser(new User(username, password, nickname, email, userPic));

        return Result.success();
    }

    @GetMapping("/findUserByNickname")
    public Result<List<User>> findUserByNickname(String nickname)
    {
        log.info("Finding user by nickname: {}", nickname);
        return Result.success(this.userService.findUserByNickname(nickname));
    }

    @GetMapping("/findUserByUsername")
    public Result<User> findUserByUsername(String username)
    {
        log.info("Finding user by username: {}", username);

        return Result.success(this.userService.findUserByUsername(username));
    }
}
