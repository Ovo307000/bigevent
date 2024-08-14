package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.Status;
import com.ovo307000.bigevent.core.constants.enumeration.status.UserStatus;
import com.ovo307000.bigevent.core.security.encryptor.SHA256Encrypted;
import com.ovo307000.bigevent.core.security.generater.DefaultValueGenerator;
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
    private final DefaultValueGenerator   defaultValueGenerator;

    @Autowired
    public UserService(@Qualifier("userUserRepository") UserRepository userRepository,
                       ThreadLocalUtil<Claims> threadLocalUtil,
                       JWTUtil jwtUtil,
                       DefaultValueGenerator defaultValueGenerator)
    {
        this.userRepository        = userRepository;
        this.threadLocalUtil       = threadLocalUtil;
        this.jwtUtil               = jwtUtil;
        this.defaultValueGenerator = defaultValueGenerator;
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

        UserDTO newUser        = new UserDTO();
        String  randomPassword = this.defaultValueGenerator.generateRandomPassword();

        BeanUtils.copyProperties(user, newUser);

        log.debug("Updating user: {}", newUser);

        newUser.setUpdateTime(LocalDateTime.now());
        newUser.setCreateTime(Optional.ofNullable(newUser.getCreateTime())
                                      .orElse(LocalDateTime.now()));
        newUser.setPassword(SHA256Encrypted.encrypt(Optional.ofNullable(user.getPassword())
                                                            .orElse(randomPassword)));

        return this.userRepository.save(newUser);
    }

    public UserDTO updateAvatar(String avatarUrl)
    {
        UserDTO newUser = Objects.requireNonNull(this.findUserByThreadLocal(), "User not found");

        newUser.setUserPicture(avatarUrl);
        newUser.setUpdateTime(LocalDateTime.now());

        return this.userRepository.save(newUser);
    }

    public UserDTO findUserByThreadLocal()
    {
        Long id = Objects.requireNonNull(this.threadLocalUtil.getAndRemove()
                                                             .get("id", Long.class),
                                         "Failed to get user id from ThreadLocal");

        return this.userRepository.findUsersById(id);
    }

    /**
     * 更新用户密码
     *
     * @param newPassword    新密码
     * @param oldPassword    旧密码
     * @param repeatPassword 重复的新密码
     *
     * @return 更新后的用户信息
     *
     * @throws NoSuchAlgorithmException 加密算法异常
     *                                  <p>
     *                                  该方法用于更新当前用户的密码。首先验证新密码和重复密码是否一致，
     *                                  然后从线程本地获取当前用户信息，并对旧密码进行加密后与数据库中存储的密码比对。
     *                                  如果旧密码正确，则对新密码进行加密后更新至用户信息，并记录更新时间。
     *                                  最后保存更新后的用户信息至数据库，并返回更新后的用户信息对象。
     */
    public UserDTO updateUserPassword(String newPassword, String oldPassword, String repeatPassword)
            throws NoSuchAlgorithmException
    {
        // 检查新密码和重复密码是否一致
        if (! Objects.equals(newPassword, repeatPassword))
        {
            throw new IllegalArgumentException("The new password and repeat password are not equal");
        }

        // 从线程本地获取当前用户信息
        UserDTO userByThreadLocal = this.findUserByThreadLocal();
        // 对旧密码进行加密
        String encryptedOldPassword = SHA256Encrypted.encrypt(oldPassword);

        // 验证加密后的旧密码是否与数据库中存储的密码一致
        if (! Objects.equals(encryptedOldPassword, userByThreadLocal.getPassword()))
        {
            throw new IllegalArgumentException("The old password is incorrect");
        }

        // 对新密码进行加密
        String encryptedNewPassword = SHA256Encrypted.encrypt(newPassword);

        // 更新用户密码和更新时间
        userByThreadLocal.setPassword(encryptedNewPassword);
        userByThreadLocal.setUpdateTime(LocalDateTime.now());

        // 保存更新后的用户信息至数据库，并返回
        return this.userRepository.save(userByThreadLocal);
    }
}
