package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.config.properties.RegisterProperties;
import com.ovo307000.bigevent.core.constants.enumeration.status.UserStatus;
import com.ovo307000.bigevent.core.security.encryptor.SHA256Encrypted;
import com.ovo307000.bigevent.core.security.generater.DefaultValueGenerator;
import com.ovo307000.bigevent.core.utils.ThreadLocalUtil;
import com.ovo307000.bigevent.entity.dto.UserDTO;
import com.ovo307000.bigevent.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
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
    private final DefaultValueGenerator   defaultValueGenerator;
    private final RegisterProperties      registerProperties;

    @Autowired
    public UserService(@Qualifier("userUserRepository") UserRepository userRepository,
                       ThreadLocalUtil<Claims> threadLocalUtil,
                       DefaultValueGenerator defaultValueGenerator,
                       RegisterProperties registerProperties)
    {
        this.userRepository        = userRepository;
        this.threadLocalUtil       = threadLocalUtil;
        this.defaultValueGenerator = defaultValueGenerator;
        this.registerProperties    = registerProperties;
    }

    public String register(@NotNull UserDTO user) throws NoSuchAlgorithmException
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

    /**
     * 用户登录方法
     *
     * @param user 需要登录的用户信息，不能为空
     *
     * @return 返回登录状态，可能的状态包括成功、密码错误和用户已存在
     * <p>
     * 说明：
     * 1. 本方法首先尝试根据用户名查找数据库中的用户信息
     * 2. 如果找到用户信息，则检查传入的用户密码是否正确
     * 3. 如果密码正确，返回登录成功状态；否则返回密码错误状态
     * 4. 如果没有找到用户信息，则返回用户已存在状态
     */
    public String login(@NotNull UserDTO user)
    {
        // 尝试查找数据库中与提供的用户名匹配的用户信息
        return Optional.ofNullable(this.userRepository.findUsersByUsername(user.getUsername()))
                       // 使用 Map 做映射，将结果映射到 UserStatus 中并返回
                       .map((UserDTO userInDatabase) -> this.isPasswordCorrect(user, userInDatabase)
                                                        ? UserStatus.SUCCESS
                                                        : UserStatus.PASSWORD_MISMATCH)
                       // 如果没有找到用户信息，则返回用户不存在状态
                       .orElse(UserStatus.USER_NOT_EXISTS);
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

    /**
     * 更新用户信息
     *
     * @param user 待更新的用户信息
     *
     * @return 更新后的用户信息，如果用户不存在，则返回null
     *
     * @throws NoSuchAlgorithmException 如果生成随机密码时发生错误
     */
    public UserDTO update(UserDTO user) throws NoSuchAlgorithmException
    {
        // 检查用户是否存在，如果不存在则返回null
        if (!this.isUserExists(user))
        {
            return null;
        }

        // 创建一个新的UserDTO对象，以避免直接修改传入的对象
        UserDTO newUser = new UserDTO();
        // 生成一个随机密码，用于备用
        String randomPassword = this.defaultValueGenerator.generateRandomString(12);

        // 复制传入的user对象的属性到新创建的newUser对象
        BeanUtils.copyProperties(user, newUser);

        // 记录debug级别的日志，指示正在更新的用户信息
        log.debug("Updating user: {}", newUser);

        // 设置用户的更新时间为当前时间
        newUser.setUpdateTime(LocalDateTime.now());
        // 如果用户对象中没有创建时间，则设置为当前时间
        newUser.setCreateTime(Optional.ofNullable(newUser.getCreateTime())
                                      .orElse(LocalDateTime.now()));
        // 加密用户的密码，如果密码为空，则使用随机生成的密码
        newUser.setPassword(SHA256Encrypted.encrypt(Optional.ofNullable(user.getPassword())
                                                            .orElse(randomPassword)));

        // 保存更新后的用户信息到数据库，并返回更新后的用户信息
        return this.userRepository.save(newUser);
    }


    public UserDTO updateAvatar(String avatarUrl)
    {
        // 验证头像URL格式是否正确
        if (! this.isValidUrl(avatarUrl))
        {
            throw new IllegalArgumentException("The avatar url is not valid");
        }

        // 检查文件类型是否支持
        String fileType = this.getFileTypeFromUrl(avatarUrl);
        if (! this.registerProperties.getEmailFormats()
                                     .contains(fileType))
        {
            throw new IllegalArgumentException("The file type is not supported");
        }

        // 确保当前用户存在
        UserDTO newUser = Objects.requireNonNull(this.findUserByThreadLocal(), "User not found");

        // 更新用户的头像URL和最后更新时间
        newUser.setUserPicture(avatarUrl);
        newUser.setUpdateTime(LocalDateTime.now());

        // 保存更改到数据库
        return this.userRepository.save(newUser);
    }

    /**
     * 验证字符串是否为合法的URL
     *
     * @param url 待验证的URL字符串
     *
     * @return 如果URL合法则返回true，否则返回false
     * 此方法使用正则表达式来验证URL，支持http和https协议
     * 它检查URL是否以http或https开头，后面跟着一个或多个由点号连接的字母数字组合
     * URL的路径部分是可选的，如果存在，将进行一定程度的格式检查
     * <p>
     * 注意：这个正则表达式可能无法覆盖所有合法的URL情况，但它在大多数常见情况下有效
     */
    private boolean isValidUrl(String url)
    {
        // 更严格的URL验证正则表达式
        String regex = "^(http|https)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+(/[a-zA-Z0-9.,@?^=%&amp;:~+#-]*[a-zA-Z0-9@?^=%&amp;~+#-])?$";

        return url.matches(regex);
    }


    /**
     * 从URL中获取文件类型
     *
     * @param url 文件的URL地址
     *
     * @return 文件类型字符串
     * <p>
     * 该方法通过分析URL的最后一点（'.'）来确定文件类型，即获取URL中最后一点（'.'）之后的所有字符作为文件类型
     * 如果URL格式不正确，即不包含'.'或以'.'结尾，则抛出IllegalArgumentException异常
     */
    private String getFileTypeFromUrl(String url)
    {
        // 查找URL中最后一点（'.'）的位置
        int lastDotIndex = url.lastIndexOf('.');

        // 如果URL中没有'.'或以'.'结尾，则视为无效URL格式，抛出异常
        if (lastDotIndex < 0 || lastDotIndex == url.length() - 1)
        {
            throw new IllegalArgumentException("Invalid URL format for determining file type");
        }

        // 返回URL中最后一点（'.'）之后的所有字符作为文件类型
        return url.substring(lastDotIndex + 1);
    }

    /**
     * 根据ThreadLocal中的信息查找用户
     * <p>
     * 此方法通过检查ThreadLocal存储的Claims对象来获取用户名，
     * 然后根据这个用户名从用户仓库中查找对应的用户数据
     *
     * @return UserDTO 根据ThreadLocal中用户名查找到的用户数据，如果未找到返回null
     */
    public @Nullable UserDTO findUserByThreadLocal()
    {
        // 从ThreadLocal中获取Claims对象，该对象包含用户信息
        Claims claims = this.threadLocalUtil.get();

        // 从Claims对象中提取用户名，如果不存在则抛出异常
        String username = Objects.requireNonNull(claims.get("username", String.class),
                                                 "Failed to get username from claims");

        // 根据提取的用户名查询用户数据
        return this.userRepository.findUsersByUsername(username);
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
            throw new IllegalArgumentException("The new passwordInDatabase and repeat passwordInDatabase are not equal");
        }

        // 从线程本地获取当前用户信息
        UserDTO user = Objects.requireNonNull(this.findUserByThreadLocal(), "Failed to get user from ThreadLocal");
        String passwordInDatabase = Objects.requireNonNull(user.getPassword(),
                                                           "Failed to get user passwordInDatabase from ThreadLocal");

        // 对旧密码进行加密
        String encryptedOldPassword = SHA256Encrypted.encrypt(oldPassword);

        // 验证加密后的旧密码是否与数据库中存储的密码一致
        if (! Objects.equals(encryptedOldPassword, passwordInDatabase))
        {
            throw new IllegalArgumentException("The old passwordInDatabase is incorrect");
        }

        // 对新密码进行加密
        String encryptedNewPassword = SHA256Encrypted.encrypt(newPassword);

        // 更新用户密码和更新时间
        user.setPassword(encryptedNewPassword);
        user.setUpdateTime(LocalDateTime.now());

        // 保存更新后的用户信息至数据库，并返回
        return this.userRepository.save(user);
    }
}
