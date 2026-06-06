package com.weiwei.wang.common.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public final class RsaUtil {

    private static final String RSA = "RSA";
    private static final String RSA_CIPHER = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String RSA_SIGNATURE = "SHA256withRSA";

    private RsaUtil() {
    }

    public static RsaKeyPair generateKeyPair() {
        return generateKeyPair(2048);
    }

    public static RsaKeyPair generateKeyPair(int keySize) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
            generator.initialize(keySize);
            KeyPair keyPair = generator.generateKeyPair();
            return new RsaKeyPair(
                    Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()),
                    Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())
            );
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Generate RSA key pair failed", exception);
        }
    }

    public static String encryptByPublicKey(String plainText, String base64PublicKey) {
        return doCipher(plainText.getBytes(StandardCharsets.UTF_8), base64PublicKey, Cipher.ENCRYPT_MODE, true);
    }

    public static String decryptByPrivateKey(String base64CipherText, String base64PrivateKey) {
        byte[] plainBytes = doCipherBytes(Base64.getDecoder().decode(base64CipherText), base64PrivateKey, Cipher.DECRYPT_MODE, false);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }

    public static String sign(String content, String base64PrivateKey) {
        try {
            Signature signature = Signature.getInstance(RSA_SIGNATURE);
            signature.initSign(loadPrivateKey(base64PrivateKey));
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("RSA sign failed", exception);
        }
    }

    public static boolean verify(String content, String base64Signature, String base64PublicKey) {
        try {
            Signature signature = Signature.getInstance(RSA_SIGNATURE);
            signature.initVerify(loadPublicKey(base64PublicKey));
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(base64Signature));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("RSA verify failed", exception);
        }
    }

    public static PublicKey loadPublicKey(String base64PublicKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
            return KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (GeneralSecurityException exception) {
            throw new IllegalArgumentException("Invalid RSA public key", exception);
        }
    }

    public static PrivateKey loadPrivateKey(String base64PrivateKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);
            return KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (GeneralSecurityException exception) {
            throw new IllegalArgumentException("Invalid RSA private key", exception);
        }
    }

    private static String doCipher(byte[] content, String base64Key, int mode, boolean publicKey) {
        return Base64.getEncoder().encodeToString(doCipherBytes(content, base64Key, mode, publicKey));
    }

    private static byte[] doCipherBytes(byte[] content, String base64Key, int mode, boolean publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_CIPHER);
            cipher.init(mode, publicKey ? loadPublicKey(base64Key) : loadPrivateKey(base64Key));
            return cipher.doFinal(content);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("RSA cipher failed", exception);
        }
    }

    public record RsaKeyPair(String publicKey, String privateKey) {
    }
}
