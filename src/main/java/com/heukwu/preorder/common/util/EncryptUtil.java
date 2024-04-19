package com.heukwu.preorder.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class EncryptUtil {

    private byte[] key;
    private SecretKeySpec secretKeySpec;

    public EncryptUtil(@Value("${encrypt.secretKey}") String secretKey) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = secretKey.getBytes(StandardCharsets.UTF_8);
            key = sha.digest(key);
            key = Arrays.copyOf(key, 24);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            return encode(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error(e.getMessage());

            return null;
        }
    }

    public String decrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            return new String(cipher.doFinal(decode(value)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());

            return null;
        }
    }

    private String encode(byte[] source) {
        return Base64.getEncoder().encodeToString(source);
    }

    private byte[] decode(String value) {
        return Base64.getDecoder().decode(value);
    }
}
