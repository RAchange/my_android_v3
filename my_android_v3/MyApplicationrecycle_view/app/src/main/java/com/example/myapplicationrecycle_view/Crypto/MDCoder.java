package com.example.myapplicationrecycle_view.Crypto;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.Security;

public abstract class MDCoder {

    public static byte[] encodeMD2(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD2");
        return md.digest(data);
    }

    public static byte[] encodeMD5(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(data);
    }

    public static byte[] encodeMD4(byte[] data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("MD4");
        return md.digest(data);
    }

    public static String encodeMD4Hex(byte[] data) throws Exception {
        byte[] b = encodeMD4(data);
        return new String(Hex.encode(b));
    }

    public static byte[] encodeMD5(String data) throws Exception {
        return DigestUtils.md5(data);
    }

    public static String encodeMD5Hex(String data) throws Exception {
        return DigestUtils.md5Hex(data);
    }
}
