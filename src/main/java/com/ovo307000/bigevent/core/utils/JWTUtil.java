package com.ovo307000.bigevent.core.utils;

import com.ovo307000.bigevent.config.properties.JWTProperties;
import com.ovo307000.bigevent.core.security.encryptor.SHA256Encrypted;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component("jwtUtil")
public class JWTUtil
{
    private final JWTProperties jwtProperties;
    private final SecretKey     key;

    public JWTUtil(@Qualifier("jwtProperties") JWTProperties jwtProperties)
    {
        this.jwtProperties = jwtProperties;

        this.key = this.getSignatureAlgorithm(Objects.requireNonNull(this.jwtProperties.getAlgorithmNameUpperCase(),
                                                                     "Algorithm name is null")
                                                     .trim()
                                                     .toUpperCase());
    }

    private SecretKey getSignatureAlgorithm(String keyAlgorithmUpperCase)
    {
        return switch (keyAlgorithmUpperCase)
        {
            case "HS256" -> Jwts.SIG.HS256.key()
                                          .build();
            case "HS384" -> Jwts.SIG.HS384.key()
                                          .build();
            case "HS512" -> Jwts.SIG.HS512.key()
                                          .build();

            default -> throw new IllegalArgumentException("Unsupported algorithm: " + keyAlgorithmUpperCase);
        };
    }

    // TODO:
    //  2024年8月10日 18点52分
    //      生成加密的JWT Token，待实现

        /**
     * 验证和解析JWT Token
     * <p>
     * 通过使用SHA算法验证JWT Token，并解析出其中的声明（Claims）
     * 这个方法主要用于确保JWT Token的有效性，并获取Token中包含的用户信息或其他数据
     *
     * @param token 待验证和解析的JWT Token字符串
     *
     * @return 解析后的JWT Payload部分，以Claims对象形式返回
     */
    public Claims verifyAndParseToken(String token)
    {
        // 创建JWT解析器
        return Jwts.parser()
                   // 指定用于验证JWT的密钥
                   .verifyWith(this.key)
                   // 构建解析器
                   .build()
                   // 解析并验证JWT Token
                   .parseSignedClaims(token)
                   // 获取解析后的Payload部分
                   .getPayload();
    }



    /**
     * 根据用户名和密码生成令牌
     * <p>
     * 本方法通过SHA算法对用户密码进行加密，并将加密后的密码与用户名一起作为载荷生成令牌
     * 使用场景包括但不限于用户身份验证和授权过程中，需要生成唯一标识令牌以便后续的身份验证和授权操作
     *
     * @param username 用户名，用于标识用户身份
     * @param password 用户密码，用于验证用户身份，将被加密处理
     *
     * @return 生成的令牌字符串
     *
     * @throws NoSuchAlgorithmException 如果SHA-256加密算法不可用，则抛出此异常
     */
    public String generateTokenByUsernameAndPassword(String username, String password) throws NoSuchAlgorithmException
    {
        // 对用户密码进行SHA-256加密，以确保密码在生成令牌时不会以明文形式出现
        String encryptedPassword = SHA256Encrypted.encrypt(password);

        // 调用内部方法generateToken，传入包含加密密码和用户名的Map作为参数，生成令牌
        return this.generateToken(Map.of("username", username, "password", encryptedPassword));
    }


    /**
     * 根据用户信息生成JWT令牌
     * <p>
     * 本函数构建一个JWT令牌，其中包括提供的用户信息（claims）、当前时间戳、
     * 以及基于配置的过期时间。使用随机生成的唯一ID和私密密钥对令牌进行签名。
     *
     * @param claims 用户信息的集合，将作为JWT的载荷部分
     *
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(Map<String, Object> claims)
    {
        // 构建JWT令牌，设置载荷为提供的用户信息
        Instant instant = Instant.now();

        // 生成随机唯一ID，用于区分不同的JWT令牌
        String uuid = UUID.randomUUID()
                          .toString();

        // 使用Jwts.builder()构建JWT令牌
        return Jwts.builder()
                   .claims(claims)
                   // 使用私密密钥对令牌进行签名，确保令牌的完整性和防篡改
                   .signWith(this.key)
                   // 设置令牌的签发时间为当前时间
                   .issuedAt(Date.from(instant))
                   // 设置令牌的过期时间为当前时间加上配置的过期时长
                   .expiration(Date.from(instant.plusSeconds(this.jwtProperties.getExpirationOfSeconds())))
                   // 为令牌生成一个随机的唯一ID
                   .id(uuid)
                   // 将所有部分压缩成一个紧凑的JWT字符串
                   .compact();
    }

}
