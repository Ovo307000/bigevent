package com.ovo307000.bigevent.surety.encryptor;

import com.ovo307000.bigevent.global.security.encryptor.SHA256Encrypted;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

@SpringBootTest
class SHA256EncryptedTest
{
    private static final Logger log = LoggerFactory.getLogger(SHA256EncryptedTest.class);

    @Test
    void encrypt() throws NoSuchAlgorithmException
    {
        String encrypted = SHA256Encrypted.encrypt("test");

        log.info("encryptor: {}", encrypted);
    }
}