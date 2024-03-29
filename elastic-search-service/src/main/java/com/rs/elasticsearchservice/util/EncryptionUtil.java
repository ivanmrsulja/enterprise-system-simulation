package com.rs.elasticsearchservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptionUtil {

    private final SecretKey secretKey;

    private final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(new byte[]{15, 93, 67, 92, 93, 64, 28, 80, 6, 88, 38, 98, 26, 12, 15, 59});
    private final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

    @Autowired
    public EncryptionUtil(SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
    }

    public String encrypt(String plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV_PARAMETER_SPEC);
        return Base64Utils.encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public String decrypt(String cipherText) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IV_PARAMETER_SPEC);
        return new String(cipher.doFinal(Base64Utils.decodeFromString(cipherText)));
    }
}
