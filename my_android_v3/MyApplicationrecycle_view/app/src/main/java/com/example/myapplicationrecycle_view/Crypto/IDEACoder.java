package com.example.myapplicationrecycle_view.Crypto;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class IDEACoder {

    public static final String KEY_ALGORITHM = "IDEA";

    public static final String CIPHER_ALGORITHM = "IDEA/ECB/ISO10126Padding";

    private static Key toKey(byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    public static byte[] initKey() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(256);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }
}