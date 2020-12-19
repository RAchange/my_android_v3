package com.example.myapplicationrecycle_view.Crypto;

import org.apache.commons.codec.binary.Hex;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class Security {
    public static final String ALGORITHM = "AES";
    private static final String KEY = "1486c5dc751a54ce3a58701ba537ecc8e257bf66127837e9401acdaceb6023f8";

    private static Key getKey() throws Exception {
        byte[] key = Hex.decodeHex(KEY.toCharArray());
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        return secretKey;
    }

    public static byte[] decrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            return cipher.doFinal(data);
        } catch (Exception e) {
            return data;
        }
    }

    public static byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            return cipher.doFinal(data);
        } catch (Exception e) {
            return data;
        }
    }

    public static byte[] initKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
        kg.init(256);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    public static String initKeyHex() throws Exception {
        return Hex.encodeHexString(initKey());
    }
}
