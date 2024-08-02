package com.ovo307000.bigevent.service;

import com.ovo307000.bigevent.constant.ResultConstant;
import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.excaption.PasswordNotMatchException;
import com.ovo307000.bigevent.excaption.UserAlreadyExistsException;
import com.ovo307000.bigevent.excaption.UserNotExistsException;
import com.ovo307000.bigevent.repository.UserRepository;
import com.ovo307000.bigevent.surety.encrypted.SHA256Encrypted;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("userService")
public class UserService
{
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public void register(@NotNull User user) throws NoSuchAlgorithmException, UserAlreadyExistsException
    {
        if (this.isUserExists(user))
        {
            throw new UserAlreadyExistsException(ResultConstant.USER_ALREADY_EXISTS.getMessage());
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
                                         throw new PasswordNotMatchException(ResultConstant.PASSWORD_INCORRECT.getMessage());
                                     }
                                 }, () ->
                                 {
                                     throw new UserNotExistsException(ResultConstant.USER_NOT_EXISTS.getMessage());
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
            log.error("Encryption algorithm not found: {}", e.getMessage());

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
            throw new UserNotExistsException(ResultConstant.USER_NOT_EXISTS.getMessage());
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