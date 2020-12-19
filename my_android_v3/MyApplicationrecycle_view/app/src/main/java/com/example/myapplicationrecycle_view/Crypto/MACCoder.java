package com.example.myapplicationrecycle_view.Crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.Security;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class MACCoder {
    public static byte[] initHmacMD5Key() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacMD5(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "HmacMD5");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static byte[] initHmacSHAKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacSHA(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static byte[] initHmacSHA256Key() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacSHA256(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static byte[] initHmacSHA384Key() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA384");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacSHA384(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA384");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static byte[] initHmacSHA512Key() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA512");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacSHA512(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA512");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static byte[] initHmacMD2Key() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD2");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacMD2(byte[] data, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey secretKey = new SecretKeySpec(key, "HmacMD2");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static String encodeHmacMD2Hex(byte[] data, byte[] key) throws Exception {
        byte[] b = encodeHmacMD2(data, key);
        return new String(Hex.encode(b));
    }

    public static byte[] initHmacMD4Key() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD4");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacMD4(byte[] data, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey secretKey = new SecretKeySpec(key, "HmacMD4");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static String encodeHmacMD4Hex(byte[] data, byte[] key) throws Exception {
        byte[] b = encodeHmacMD4(data, key);
        return new String(Hex.encode(b));
    }

    public static byte[] initHmacSHA224Key() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA224");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacSHA224(byte[] data, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA224");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static String encodeHmacSHA224Hex(byte[] data, byte[] key) throws Exception {
        byte[] b = encodeHmacSHA224(data, key);
        return new String(Hex.encode(b));
    }
}
