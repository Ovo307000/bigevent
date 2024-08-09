package com.ovo307000.bigevent.global.utils;

import com.ovo307000.bigevent.global.properties.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component("jwtUtil")
public class JWTUtil
{
    private static final Logger        log = LoggerFactory.getLogger(JWTUtil.class);
    private final        JWTProperties jwtProperties;
    private final        SecretKey     key;

    public JWTUtil(@Qualifier("jwtProperties") JWTProperties jwtProperties)
    {
        this.jwtProperties = jwtProperties;

        Objects.requireNonNull(this.jwtProperties.getSecret(), "The secret property is required.");

        this.key = this.getSignatureAlgorithm(this.jwtProperties.getAlgorithmNameUpperCase()
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

    // 生成JWT Token
    public String generateToken()
    {
        Map<String, Object> claims = new HashMap<>();

        LocalDateTime expirationDateTime = LocalDateTime.now()
                                                        .plusMinutes(this.jwtProperties.getExpirationOfMinutes());

        return Jwts.builder()
                   .claims(claims)
                   .signWith(this.key)
                   .notBefore(Date.from(Instant.now()))
                   .issuedAt(Date.from(Instant.now()))
                   .expiration(Date.from(expirationDateTime.atZone(ZoneId.systemDefault())
                                                           .toInstant()))
                   .id(UUID.randomUUID()
                           .toString())
                   .compact();
    }

    // 验证和解析JWT Token
    public Claims verifyAndParseToken(String token)
    {
        return Jwts.parser()
                   .verifyWith(this.key)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
}
