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
    private final JWTProperties           jwtProperties;
    private final SecretKey               key;
    private final ThreadLocalUtil<Claims> threadLocalUtil;

    public JWTUtil(@Qualifier("jwtProperties") JWTProperties jwtProperties, ThreadLocalUtil<Claims> threadLocalUtil)
    {
        this.jwtProperties   = jwtProperties;
        this.threadLocalUtil = threadLocalUtil;

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

    // 验证和解析JWT Token
    public Claims verifyAndParseToken(String token)
    {
        return Jwts.parser()
                   .verifyWith(this.key)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    public String generateTokenByUsernameAndPasswordFromThreadLocal() throws NoSuchAlgorithmException
    {
        Claims claims = Objects.requireNonNull(this.threadLocalUtil.get(), "Failed to get claims from thread local");

        String username = Objects.requireNonNull(claims.get("username", String.class),
                                                 "Failed to get username from claims");
        String password = SHA256Encrypted.encrypt(Objects.requireNonNull(claims.get("password", String.class),
                                                                         "Failed to get password from claims"));

        return this.generateToken(Map.of("username", username, "password", password));
    }

    public String generateToken(Map<String, Object> claims)
    {
        return Jwts.builder()
                   .claims(claims)
                   .signWith(this.key)
                   .issuedAt(Date.from(Instant.now()))
                   .expiration(Date.from(Instant.now()
                                                .plusSeconds(this.jwtProperties.getExpirationOfSeconds())))
                   .id(UUID.randomUUID()
                           .toString())
                   .compact();
    }
}
