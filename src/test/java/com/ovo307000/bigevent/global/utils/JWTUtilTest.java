package com.ovo307000.bigevent.global.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
class JWTUtilTest
{
    private static final Logger  log = LoggerFactory.getLogger(JWTUtilTest.class);
    @Autowired
    private              JWTUtil jwtUtil;

    @Test
    void generateToken()
    {
        String subject = "test";

        Optional.ofNullable(this.jwtUtil.generateToken(subject))
                .ifPresentOrElse(log::info, () -> Assertions.fail("Token is null"));
    }

    @Test
    void generateTokenWithClaims()
    {
        Map<String, Object> claims = Map.of("id", 1, "name", "test");

        Optional.ofNullable(this.jwtUtil.generateToken(claims))
                .map(this.jwtUtil::verifyAndParseToken)
                .ifPresent((Claims extracts) -> log.info(extracts.toString()));
    }

    @Test
    void generateTokenWithNullClaims()
    {
        String token = this.jwtUtil.generateToken((String) null);

        Optional.ofNullable(this.jwtUtil.verifyAndParseToken(token))
                .ifPresent((Claims claims) -> log.info(claims.toString()));
    }

    @Test
    void verifyAndParseToken()
    {
        String token = this.jwtUtil.generateToken(Map.of("id", 1, "name", "test"));

        Optional.ofNullable(this.jwtUtil.verifyAndParseToken(token))
                .ifPresent((Claims claims) -> log.info(claims.get("id")
                                                             .toString()));
    }
}