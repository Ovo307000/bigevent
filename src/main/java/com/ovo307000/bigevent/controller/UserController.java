package com.ovo307000.bigevent.controller;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.result.Result;
import com.ovo307000.bigevent.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RequestMapping("/user")
@RestController("userController")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<?> register(String nickname, String password) throws NoSuchAlgorithmException
    {
        this.userService.register(new User(nickname, password));

        return Result.success();
    }


    /*
     * 采用用户名和密码登录，因为在应用层出错后，会抛出异常，所以在这采用try-catch捕获异常
     * */
    @PostMapping("/login")
    public Result<?> login(String nickname, String password)
    {
        try
        {
            this.userService.login(new User(nickname, password));
        }
        catch (IllegalArgumentException e)
        {
            return Result.fail(e.getMessage());
        }

        return Result.success();
    }
}
