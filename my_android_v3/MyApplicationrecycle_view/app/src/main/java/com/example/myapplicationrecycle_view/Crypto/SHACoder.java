package com.example.myapplicationrecycle_view.Crypto;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.Security;

public class SHACoder {

    public static byte[] encodeSHA(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA");
        return md.digest(data);
    }

    public static byte[] encodeSHA(String data) throws Exception {
        return DigestUtils.sha(data);
    }

    public static String encodeSHAHex(String data) throws Exception {
        return DigestUtils.shaHex(data);
    }

    public static byte[] encodeSHA256(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(data);
    }

    public static byte[] encodeSHA256(String data) throws Exception {
        return DigestUtils.sha256(data);
    }

    public static String encodeSHA256Hex(String data) throws Exception {
        return DigestUtils.sha256Hex(data);
    }

    public static byte[] encodeSHA384(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        return md.digest(data);
    }

    public static byte[] encodeSHA384(String data) throws Exception {
        return DigestUtils.sha384(data);
    }

    public static String encodeSHA384Hex(String data) throws Exception {
        return DigestUtils.sha384Hex(data);
    }

    public static byte[] encodeSHA512(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        return md.digest(data);
    }

    public static byte[] encodeSHA512(String data) throws Exception {
        return DigestUtils.sha512(data);
    }

    public static String encodeSHA512Hex(String data) throws Exception {
        return DigestUtils.sha512Hex(data);
    }

    public static byte[] encodeSHA224(byte[] data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("SHA-224");
        return md.digest(data);
    }

    public static String encodeSHA224Hex(byte[] data) throws Exception {
        byte[] b = encodeSHA224(data);
        return new String(Hex.encode(b));
    }
}
