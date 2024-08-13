package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.Status;
import com.ovo307000.bigevent.core.constants.enumeration.status.UserStatus;
import com.ovo307000.bigevent.core.security.encryptor.SHA256Encrypted;
import com.ovo307000.bigevent.core.utils.JWTUtil;
import com.ovo307000.bigevent.core.utils.ThreadLocalUtil;
import com.ovo307000.bigevent.entity.dto.UserDTO;
import com.ovo307000.bigevent.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("userUserService")
public class UserService
{
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository          userRepository;
    private final ThreadLocalUtil<Claims> threadLocalUtil;
    private final JWTUtil                 jwtUtil;

    @Autowired
    public UserService(@Qualifier("userUserRepository") UserRepository userRepository,
                       ThreadLocalUtil<Claims> threadLocalUtil,
                       JWTUtil jwtUtil)
    {
        this.userRepository  = userRepository;
        this.threadLocalUtil = threadLocalUtil;
        this.jwtUtil         = jwtUtil;
    }

    public Status register(@NotNull UserDTO user) throws NoSuchAlgorithmException
    {
        if (this.isUserExists(user))
        {
            return UserStatus.USER_ALREADY_EXISTS;
        }
        else
        {
            UserDTO newUser = new UserDTO();

            newUser.setCreateTime(LocalDateTime.now());
            newUser.setUpdateTime(LocalDateTime.now());
            newUser.setUsername(user.getUsername());
            newUser.setPassword(SHA256Encrypted.encrypt(user.getPassword()));

            this.userRepository.save(newUser);

            return UserStatus.SUCCESS;
        }
    }

    public boolean isUserExists(@NotNull UserDTO user)
    {
        return Optional.ofNullable(this.userRepository.findUsersById(user.getId()))
                       .isPresent();
    }

    public Status login(@NotNull UserDTO user)
    {
        return Optional.ofNullable(this.userRepository.findUsersByUsername(user.getUsername()))
                       // 使用 Map 做映射，将结果映射到 UserStatus 中并返回
                       .map((UserDTO userInDatabase) ->
                            {
                                if (this.isPasswordCorrect(user, userInDatabase))
                                {
                                    return UserStatus.SUCCESS;
                                }
                                else
                                {
                                    return UserStatus.PASSWORD_CORRECT;
                                }
                            })
                       .orElse(UserStatus.USER_ALREADY_EXISTS);
    }

    public boolean isPasswordCorrect(@NotNull UserDTO user, @NotNull UserDTO userInDatabase)
    {
        try
        {
            String encryptedPassword      = SHA256Encrypted.encrypt(user.getPassword());
            String userInDatabasePassword = userInDatabase.getPassword();

            log.debug("Equaling passwords [UserDTO input: {}, Database: {}]",
                      encryptedPassword,
                      userInDatabasePassword);

            return Objects.equals(userInDatabasePassword, encryptedPassword);
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("Encryption algorithm not found: {}", e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public List<UserDTO> findUserByNickname(String nickname)
    {
        return this.userRepository.findUsersByNickname(nickname);
    }

    public UserDTO findUserByUsername(String username)
    {
        return this.userRepository.findUsersByUsername(username);
    }

    public List<UserDTO> findUserByUsernameLikeIgnoreCase(String username)
    {
        return this.userRepository.findUsersByUsernameLikeIgnoreCase(username);
    }

    public List<UserDTO> findUserByNicknameLikeIgnoreCase(String nickname)
    {
        return this.userRepository.findUsersByNicknameLikeIgnoreCase(nickname);
    }

    public UserDTO queryCurrentUserInfo()
    {
        Claims claims = this.threadLocalUtil.getAndRemove();

        String username = claims.get("username", String.class);
        String password = claims.get("password", String.class);

        return this.userRepository.findUsersByUsernameAndPassword(username, password)
                                  .getFirst();
    }

    public UserDTO queryCurrentUserInfo(String token)
    {
        Claims claims;

        try
        {
            claims = this.jwtUtil.verifyAndParseToken(token);

            String username = claims.get("username", String.class);
            String password = claims.get("password", String.class);

            return this.userRepository.findUsersByUsernameAndPassword(username, password)
                                      .getFirst();
        }
        catch (JwtException jwtException)
        {
            throw new JwtException("Token verification failed: " + jwtException.getMessage());
        }
    }

    public UserDTO update(UserDTO user) throws NoSuchAlgorithmException
    {
        if (! this.isUserExists(user))
        {
            return null;
        }

        UserDTO newUser = new UserDTO();

        BeanUtils.copyProperties(user, newUser);

        newUser.setUpdateTime(LocalDateTime.now());
        newUser.setCreateTime(Optional.ofNullable(newUser.getCreateTime())
                                      .orElse(LocalDateTime.now()));
        newUser.setPassword(SHA256Encrypted.encrypt(Optional.ofNullable(user.getPassword())
                                                            .orElse("123456")));

        return this.userRepository.save(newUser);
    }
}