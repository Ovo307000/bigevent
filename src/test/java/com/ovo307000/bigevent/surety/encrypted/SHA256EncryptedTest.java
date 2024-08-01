package com.ovo307000.bigevent.surety.encrypted;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SHA256EncryptedTest
{
    private static final Logger log = LoggerFactory.getLogger(SHA256EncryptedTest.class);

    @Test
    void encrypt() throws NoSuchAlgorithmException
    {
        String encrypted = SHA256Encrypted.encrypt("test");

        log.info("encrypted: {}", encrypted);
    }
}