package com.ovo307000.bigevent.service;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
public class UserService
{
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public void register(@NotNull User user)
    {
        Optional.ofNullable(this.userRepository.findUsersByNickname(user.getNickname()))
                .ifPresentOrElse((User u) ->
                                 {
                                     throw new IllegalArgumentException("User already exists");
                                 }, () -> this.userRepository.save(user));
    }
}
