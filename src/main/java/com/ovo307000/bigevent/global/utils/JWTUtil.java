package com.ovo307000.bigevent.global.utils;

import com.ovo307000.bigevent.global.properties.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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
    public String generateToken(String subject)
    {
        return Jwts.builder()
                   .subject(subject)
                   .signWith(this.key)
                   .notBefore(Date.from(Instant.now()))
                   .issuedAt(Date.from(Instant.now()))
                   .expiration(Date.from(Instant.now()
                                                .plusSeconds(this.jwtProperties.getExpirationOfSeconds())))
                   .id(UUID.randomUUID()
                           .toString())
                   .compact();
    }

    // TODO:
    //  2024年8月10日 18点52分
    //      生成加密的JWT Token，待实现

    public String generateToken(Map<String, Object> claims)
    {
        return Jwts.builder()
                   .claims(claims)
                   .signWith(this.key)
                   .notBefore(Date.from(Instant.now()))
                   .issuedAt(Date.from(Instant.now()))
                   .expiration(Date.from(Instant.now()
                                                .plusSeconds(this.jwtProperties.getExpirationOfSeconds())))
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
