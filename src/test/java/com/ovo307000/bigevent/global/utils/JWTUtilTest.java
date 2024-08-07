package com.ovo307000.bigevent.global.utils;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

class JWTUtilTest
{
    private static final Logger log = LoggerFactory.getLogger(JWTUtilTest.class);

    @Test
    void generateToken()
    {
        SecretKey key = Jwts.SIG.HS256.key()
                                      .build();

        String compacted = Jwts.builder()
                               .header()
                               .add("alg", "HS256")
                               .and()
                               .signWith(key)
                               .expiration(Date.from(Instant.ofEpochSecond(System.currentTimeMillis() + 123456)))
                               .compact();

        log.info(compacted);
    }

    @Test
    void parseToken()
    {
    }
}