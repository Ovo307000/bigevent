package com.ovo307000.bigevent.service;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.repository.UserRepository;
import com.ovo307000.bigevent.surety.encrypted.SHA256Encrypted;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
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

    public void register(@NotNull User user) throws NoSuchAlgorithmException
    {
        if (this.isUserExists(user))
        {
            throw new IllegalArgumentException("User already exists");
        }
        else
        {
            User newUser = new User(user.getNickname(), user.getPassword());

            user.setPassword(SHA256Encrypted.encrypt(user.getPassword()));
            this.userRepository.save(newUser);
        }
    }

    public boolean isUserExists(@NotNull User user)
    {
        return Optional.ofNullable(this.userRepository.findUsersByNickname(user.getNickname()))
                       .isPresent();
    }

    public void login(@NotNull User user)
    {
        Optional.ofNullable(this.userRepository.findUsersByNickname(user.getNickname()))
                .ifPresentOrElse((User userInDatabase) ->
                                 {
                                     if (! this.isPasswordCorrect(user))
                                     {
                                         throw new IllegalArgumentException("Password is incorrect");
                                     }
                                 }, () ->
                                 {
                                     throw new IllegalArgumentException("User does not exist");
                                 });
    }

    public boolean isPasswordCorrect(@NotNull User user)
    {
        try
        {
            return user.getPassword()
                       .equals(SHA256Encrypted.encrypt(user.getPassword()));
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
}