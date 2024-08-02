package com.ovo307000.bigevent.service;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.properties.ResignedProperties;
import com.ovo307000.bigevent.repository.UserRepository;
import com.ovo307000.bigevent.surety.encrypted.SHA256Encrypted;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("userService")
public class UserService
{
    private static final Logger             log = LoggerFactory.getLogger(UserService.class);
    private final        UserRepository     userRepository;
    private final        ResignedProperties resignedProperties;

    @Autowired
    public UserService(UserRepository userRepository, ResignedProperties resignedProperties)
    {
        this.userRepository     = userRepository;
        this.resignedProperties = resignedProperties;
    }

    public void register(@NotNull User user) throws NoSuchAlgorithmException
    {
        if (this.isUserExists(user))
        {
            throw new IllegalArgumentException("User already exists");
        }
        if (user.getUsername()
                .length() < this.resignedProperties.getPasswordLength())
        {
            throw new IllegalArgumentException("Username is too short");
        }
        if (user.getPassword()
                .length() < this.resignedProperties.getPasswordLength())
        {
            throw new IllegalArgumentException("Password is too short");
        }
        else
        {
            User newUser = new User();

            newUser.setCreateTime(LocalDateTime.now());
            newUser.setUpdateTime(LocalDateTime.now());
            newUser.setUsername(user.getUsername());
            newUser.setPassword(SHA256Encrypted.encrypt(user.getPassword()));

            this.userRepository.save(newUser);
        }
    }

    public boolean isUserExists(@NotNull User user)
    {
        return Optional.ofNullable(this.userRepository.findUsersByUsername(user.getUsername()))
                       .isPresent();
    }

    public void login(@NotNull User user)
    {
        Optional.ofNullable(this.userRepository.findUsersByUsername(user.getUsername()))
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
            String encryptedPassword = SHA256Encrypted.encrypt(user.getPassword());
            String userInDatabasePassword = this.userRepository.findUsersByUsername(user.getUsername())
                                                               .getPassword();

            log.debug("Equaling passwords: {} and {}", userInDatabasePassword, encryptedPassword);

            return Objects.equals(userInDatabasePassword, encryptedPassword);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<User> findUserByNickname(String nickname)
    {
        return this.userRepository.findUsersByNickname(nickname);
    }

    public User findUserByUsername(String username)
    {
        return this.userRepository.findUsersByUsername(username);
    }

    public void updateUser(User user)
    {
        if (! this.isUserExists(user))
        {
            throw new IllegalArgumentException("User does not exist");
        }
        if (user.getPassword()
                .length() < this.resignedProperties.getPasswordLength())
        {
            throw new IllegalArgumentException("Password is too short");
        }
        if (user.getNickname()
                .length() < this.resignedProperties.getNicknameLength())
        {
            throw new IllegalArgumentException("Nickname is too short");
        }

        user.setUpdateTime(LocalDateTime.now());

        int updatedCount = this.userRepository.updateUserByUsername(user.getUsername(),
                                                                    user.getUsername(),
                                                                    user.getNickname(),
                                                                    user.getPassword(),
                                                                    user.getEmail(),
                                                                    user.getUserPic(),
                                                                    LocalDateTime.now());

        log.info("Updated {} users", updatedCount);
    }
}