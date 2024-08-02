package com.ovo307000.bigevent.controller;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.result.Result;
import com.ovo307000.bigevent.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequestMapping("/user")
@RestController("userController")
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<?> register(String username, String password) throws NoSuchAlgorithmException
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
        catch (IllegalArgumentException e)
        {
            log.error("Login failed: {}", e.getMessage());

            return Result.fail(e.getMessage());
        }

        log.info("Login successful: {}", username);
        return Result.success();
    }

    @PutMapping("/updateUser")
    public Result<?> updateUser(String username, String password, String nickname, String email, String userPic)
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
