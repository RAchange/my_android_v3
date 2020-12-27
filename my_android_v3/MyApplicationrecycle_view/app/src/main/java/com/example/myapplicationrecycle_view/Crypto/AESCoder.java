package com.example.myapplicationrecycle_view.Crypto;


import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public abstract class AESCoder {

    public static final String KEY_ALGORITHM = "AES";

    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(MDCoder.encodeMD5(key), KEY_ALGORITHM);
    }

    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    public static byte[] initKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(128);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    public static String initKeyString() throws Exception {
        return Base64.getEncoder().encodeToString(initKey());
    }

    public static byte[] getKey(String key) throws Exception {
        return Base64.getDecoder().decode(key);
    }

    public static String decrypt(String data, byte[] key) throws Exception {
        return new String(decrypt(Base64.getDecoder().decode(data.getBytes("ISO-8859-1")), key), "UTF-8");
    }

    public static String encrypt(String data, byte[] key) throws Exception {
        return new String(Base64.getEncoder().encode(encrypt(data.getBytes("UTF-8"), key)), "ISO-8859-1");
    }

    public static byte[] decrypt(byte[] data, String key) throws Exception {
        return decrypt(data, getKey(key));
    }

    public static byte[] encrypt(byte[] data, String key) throws Exception {
        return encrypt(data, getKey(key));
    }

    public static String shaHex(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public static boolean validate(byte[] data, String messageDigest) {
        return messageDigest.equals(shaHex(data));
    }
}
