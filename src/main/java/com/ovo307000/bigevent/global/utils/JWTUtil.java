package com.ovo307000.bigevent.global.utils;

import com.ovo307000.bigevent.global.properties.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Component("jwtUtil")
public class JWTUtil
{
    private final JWTProperties jwtProperties;

    public JWTUtil(JWTProperties jwtProperties)
    {
        this.jwtProperties = jwtProperties;
    }

    // 生成JWT Token
    public String generateToken()
    {
        HashMap<String, Objects> claims = new HashMap<>();

        SecretKey key = Jwts.SIG.HS256.key()
                                      .build();

        LocalDateTime outDateTime = LocalDateTime.now()
                                                 .plusMinutes(this.jwtProperties.getExpirationOfMinutes());

        return Jwts.builder()
                   .claims(claims)
                   .signWith(key)
                   .notBefore(Date.from(Instant.now()))
                   .issuedAt(Date.from(Instant.now()))
                   .expiration(Date.from(outDateTime.atZone(ZoneId.systemDefault())
                                                    .toInstant()))
                   .id(UUID.randomUUID()
                           .toString())
                   .compact();
    }

    // 解析JWT Token
    public Claims parseToken(String token)
    {
        return null;
    }
}