package com.ovo307000.bigevent.core.security.generater;

import com.ovo307000.bigevent.core.security.encryptor.SHA256Encrypted;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@Component("defaultValueGenerator")
public class DefaultValueGenerator
{
    private final SecureRandom secureRandom = new SecureRandom();

    // 随机密码生成器
    public String generateRandomString(Integer length)
    {
        return this.secureRandom.ints(length)
                                .mapToObj((int i) ->
                                          {
                                              try
                                              {
                                                  return SHA256Encrypted.encrypt(String.valueOf(i));
                                              }
                                              catch (NoSuchAlgorithmException e)
                                              {
                                                  throw new RuntimeException(e);
                                              }
                                          })
                                .collect(Collectors.joining());
    }
}
