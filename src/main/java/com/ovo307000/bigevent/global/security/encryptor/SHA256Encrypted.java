package com.ovo307000.bigevent.global.security.encryptor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Encrypted
{
    /**
     * 接受一个字符串，返回一个SHA-256加密后的字符串
     *
     * @param input 输入字符串
     *
     * @return SHA-256加密后的字符串
     *
     * @throws NoSuchAlgorithmException 无法找到SHA-256加密算法
     */
    public static String encrypt(String input) throws NoSuchAlgorithmException
    {
        // 获取SHA-256加密算法的实例
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        // 更新输入字符串
        messageDigest.update(input.getBytes());

        // 计算哈希值
        byte[]        bytes         = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();

        // 将哈希值转换为十六进制字符串
        for (byte b : bytes)
        {
            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16)
                                        .substring(1));
        }

        return stringBuilder.toString();
    }
}
