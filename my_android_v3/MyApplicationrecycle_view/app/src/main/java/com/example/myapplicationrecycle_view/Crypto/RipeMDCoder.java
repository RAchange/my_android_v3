package com.example.myapplicationrecycle_view.Crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.Security;

public abstract class RipeMDCoder {

    public static byte[] encodeRipeMD128(byte[] data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("RipeMD128");
        return md.digest(data);
    }

    public static String encodeRipeMD128Hex(byte[] data) throws Exception {
        byte[] b = encodeRipeMD128(data);
        return new String(Hex.encode(b));
    }

    public static byte[] encodeRipeMD160(byte[] data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("RipeMD160");
        return md.digest(data);
    }

    public static String encodeRipeMD160Hex(byte[] data) throws Exception {
        byte[] b = encodeRipeMD160(data);
        return new String(Hex.encode(b));
    }

    public static byte[] encodeRipeMD256(byte[] data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("RipeMD256");
        return md.digest(data);
    }

    public static String encodeRipeMD256Hex(byte[] data) throws Exception {
        byte[] b = encodeRipeMD256(data);
        return new String(Hex.encode(b));
    }

    public static byte[] encodeRipeMD320(byte[] data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("RipeMD320");
        return md.digest(data);
    }

    public static String encodeRipeMD320Hex(byte[] data) throws Exception {
        byte[] b = encodeRipeMD320(data);
        return new String(Hex.encode(b));
    }
}
