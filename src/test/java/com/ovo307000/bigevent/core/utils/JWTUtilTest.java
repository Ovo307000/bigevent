package com.ovo307000.bigevent.core.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
class JWTUtilTest
{
    private static final Logger log = LoggerFactory.getLogger(JWTUtilTest.class);

    private final JWTUtil jwtUtil;

    JWTUtilTest(JWTUtil jwtUtil)
    {
        this.jwtUtil = jwtUtil;
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
    void verifyAndParseToken()
    {
        String token = this.jwtUtil.generateToken(Map.of("id", 1, "name", "test"));

        Optional.ofNullable(this.jwtUtil.verifyAndParseToken(token))
                .ifPresent((Claims claims) -> log.info(claims.get("id")
                                                             .toString()));
    }
}