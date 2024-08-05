package com.ovo307000.bigevent.service;

import com.ovo307000.bigevent.entity.User;
import com.ovo307000.bigevent.global.enumeration.status.LoginStatus;
import com.ovo307000.bigevent.global.enumeration.status.RegisterStatus;
import com.ovo307000.bigevent.global.enumeration.status.Status;
import com.ovo307000.bigevent.global.enumeration.status.UpdateStatus;
import com.ovo307000.bigevent.global.surety.encryptor.SHA256Encrypted;
import com.ovo307000.bigevent.repository.UserRepository;
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

    public Status register(@NotNull User user) throws NoSuchAlgorithmException
    {
        if (this.isUserExists(user))
        {
            return RegisterStatus.USER_ALREADY_EXISTS;
        }
        else
        {
            User newUser = new User();

            newUser.setCreateTime(LocalDateTime.now());
            newUser.setUpdateTime(LocalDateTime.now());
            newUser.setUsername(user.getUsername());
            newUser.setPassword(SHA256Encrypted.encrypt(user.getPassword()));

            this.userRepository.save(newUser);

            return RegisterStatus.SUCCESS;
        }
    }

    public boolean isUserExists(@NotNull User user)
    {
        return Optional.ofNullable(this.userRepository.findUsersByUsername(user.getUsername()))
                       .isPresent();
    }

    public Status login(@NotNull User user)
    {
        return Optional.ofNullable(this.userRepository.findUsersByUsername(user.getUsername()))
                       .map((User userInDatabase) ->
                            {
                                if (this.isPasswordCorrect(user))
                                {
                                    return LoginStatus.SUCCESS;
                                }
                                else
                                {
                                    return LoginStatus.PASSWORD_INCORRECT;
                                }
                            })
                       .orElse(LoginStatus.USER_NOT_EXISTS);
    }

    public boolean isPasswordCorrect(@NotNull User user)
    {
        try
        {
            String encryptedPassword = SHA256Encrypted.encrypt(user.getPassword());
            String userInDatabasePassword = this.userRepository.findUsersByUsername(user.getUsername())
                                                               .getPassword();

            log.debug("Equaling passwords [User input: {}, Database: {}]", encryptedPassword, userInDatabasePassword);

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

    public Status updateUser(User user)
    {
        if (! this.isUserExists(user))
        {
            return UpdateStatus.USER_NOT_EXISTS;
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

        return updatedCount == 1 ? UpdateStatus.SUCCESS : UpdateStatus.FAILED;
    }

    public List<User> findUserByUsernameLikeIgnoreCase(String username)
    {
        return this.userRepository.findUsersByNicknameLikeIgnoreCase(username);
    }
}