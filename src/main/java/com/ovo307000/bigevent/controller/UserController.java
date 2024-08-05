package com.ovo307000.bigevent.controller;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.global.enumeration.status.LoginStatus;
import com.ovo307000.bigevent.global.enumeration.status.RegisterStatus;
import com.ovo307000.bigevent.global.excaption.UserAlreadyExistsException;
import com.ovo307000.bigevent.global.result.Result;
import com.ovo307000.bigevent.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

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
    public Result<?> register(String username, String password) throws NoSuchAlgorithmException
    {
        log.info("Registering user: {}", username);

        return switch (this.userService.register(new User(username, password)))
        {
            case SUCCESS -> Result.success(RegisterStatus.SUCCESS.getMassage());
            case USER_ALREADY_EXISTS -> Result.fail(RegisterStatus.USER_ALREADY_EXISTS.getMassage());

            default -> Result.fail(RegisterStatus.FAILED.getMassage());
        };
    }


    /*
     * 采用用户名和密码登录，因为在应用层出错后，会抛出异常，所以在这采用try-catch捕获异常
     * */
    @PostMapping("/login")
    public Result<?> login(String username, String password)
    {
        log.info("Trying to login user: {}", username);

        return switch (this.userService.login(new User(username, password)))
        {
            // TODO 2024年8月6日 00点50分 应该返回一个JWT Token
            case SUCCESS -> Result.success(LoginStatus.SUCCESS.getMassage());

            case PASSWORD_NOT_MATCH -> Result.fail(LoginStatus.PASSWORD_NOT_MATCH.getMassage());
            case USER_NOT_EXISTS -> Result.fail(LoginStatus.USER_NOT_EXISTS.getMassage());
            default -> Result.fail(LoginStatus.FAILED.getMassage());
        };
    }

    @PutMapping("/updateUser")
    public Result<?> updateUser(String username, String password, String nickname, String email, String userPic)
    {
        log.info("Updating user: {}", username);

        return switch (this.userService.updateUser(new User(username, password, nickname, email, userPic)))
        {
            case RegisterStatus.SUCCESS -> Result.success("User updated successfully");
            case RegisterStatus.USER_ALREADY_EXISTS -> Result.fail(RegisterStatus.USER_ALREADY_EXISTS.getMassage());

            default -> Result.fail(RegisterStatus.FAILED.getMassage());
        };
    }

    @GetMapping("/findUserByNickname")
    public Result<?> findUserByNickname(String nickname)
    {
        log.info("Finding user by nickname: {}", nickname);

        List<User> users = this.userService.findUserByNickname(nickname);

        if (users.isEmpty())
        {
            return Result.fail("User not exists");
        }
        else
        {
            return Result.success(users);
        }
    }

    @GetMapping("/findUserByUsernameLikeIgnoreCase")
    public Result<?> findUserByUsernameLikeIgnoreCase(String username)
    {
        log.info("Finding user by username like: {}", username);

        List<User> users = this.userService.findUserByUsernameLikeIgnoreCase(username);

        if (users.isEmpty())
        {
            return Result.fail("User not exists");
        }
        else
        {
            return Result.success(users);
        }
    }

    @GetMapping("/findUserByUsername")
    public Result<?> findUserByUsername(String username)
    {
        log.info("Finding user by username: {}", username);

        User user = this.userService.findUserByUsername(username);

        if (user == null)
        {
            return Result.fail("User not exists");
        }
        else
        {
            return Result.success(user);
        }
    }
}
