package com.example.myapplicationrecycle_view.Crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.Security;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class HmacRipeMDCoder {

    public static byte[] initHmacRipeMD128Key() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacRipeMD128");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacRipeMD128(byte[] data, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey secretKey = new SecretKeySpec(key, "HmacRipeMD128");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static String encodeHmacRipeMD128Hex(byte[] data, byte[] key) throws Exception {
        byte[] b = encodeHmacRipeMD128(data, key);
        return new String(Hex.encode(b));
    }

    public static byte[] initHmacRipeMD160Key() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacRipeMD160");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacRipeMD160(byte[] data, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey secretKey = new SecretKeySpec(key, "HmacRipeMD160");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static String encodeHmacRipeMD160Hex(byte[] data, byte[] key) throws Exception {
        byte[] b = encodeHmacRipeMD160(data, key);
        return new String(Hex.encode(b));
    }
}
