package com.ovo307000.bigevent.controller;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.result.Result;
import com.ovo307000.bigevent.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<?> register(String nickname, String password)
    {
        this.userService.register(new User(nickname, password));

        return Result.success();
    }
}
