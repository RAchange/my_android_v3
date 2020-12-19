package com.example.myapplicationrecycle_view.Crypto;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public abstract class ECDSACoder {

    private static final String KEY_ALGORITHM = "ECDSA";

    private static final String SIGNATURE_ALGORITHM = "SHA512withECDSA";
    private static final String PUBLIC_KEY = "ECDSAPublicKey";
    private static final String PRIVATE_KEY = "ECDSAPrivateKey";

    public static Map<String, Object> initKey() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        BigInteger p = new BigInteger("883423532389192164791648750360308885314476597252960362792450860609699839");
        ECFieldFp ecFieldFp = new ECFieldFp(p);
        BigInteger a = new BigInteger("7fffffffffffffffffffffff7fffffffffff8000000000007ffffffffffc", 16);
        BigInteger b = new BigInteger("6b016c3bdcf18941d0d654921475ca71a9db2fb27d1d37796185c2942c0a", 16);
        EllipticCurve ellipticCurve = new EllipticCurve(ecFieldFp, a, b);
        BigInteger x = new BigInteger("110282003749548856476348533541186204577905061504881242240149511594420911");
        BigInteger y = new BigInteger("869078407435509378747351873793058868500210384946040694651368759217025454");
        ECPoint g = new ECPoint(x, y);
        BigInteger n = new BigInteger("883423532389192164791648750360308884807550341691627752275345424702807307");
        ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g, n, 1);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        kpg.initialize(ecParameterSpec, new SecureRandom());
        KeyPair keypair = kpg.generateKeyPair();
        ECPublicKey publicKey = (ECPublicKey) keypair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keypair.getPrivate();
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, privateKey);
        return map;
    }

    public static byte[] getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(sign);
    }
}

