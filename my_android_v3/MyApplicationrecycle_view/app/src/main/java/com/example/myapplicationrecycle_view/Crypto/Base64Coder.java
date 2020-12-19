package com.example.myapplicationrecycle_view.Crypto;


import org.bouncycastle.util.encoders.Base64;

public abstract class Base64Coder {

    public final static String ENCODING = "UTF-8";

    public static String encode(String data) throws Exception {

        byte[] b =  Base64.encode(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    public static String decode(String data) throws Exception {

        byte[] b = Base64.decode(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    public static byte[] decodeHex(String data) throws Exception {

        byte[] b = Base64.decode(data.getBytes(ENCODING));
        return b;
    }
}
