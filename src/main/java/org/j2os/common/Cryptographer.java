package org.j2os.common;

import lombok.experimental.UtilityClass;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

/**
 * @author Amirsam Bahador, J2OS (Java2OpenSource) Organization
 * @version 2.0
 * @since 2026-02-15
 */
@UtilityClass
public class Cryptographer {
    /* ================= AES PROPERTIES ================= */
    private final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final int GCM_IV_LENGTH = 12;
    private final int GCM_TAG_LENGTH = 128;

    /* ================= BCRYPT ================= */
    public static String hashBCrypt(String password, int costFactor) {
        return BCrypt.hashpw(password, BCrypt.gensalt(costFactor));
    }

    public static boolean verifyBCrypt(String plainPassword, String hashPassword) {
        return BCrypt.checkpw(plainPassword, hashPassword);
    }

    /* ================= SHA512 ================= */
    @Deprecated
    public static String hashSHA512(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(plainText.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest);
    }

    /* ================= MD5 ================= */
    @Deprecated
    public static String hashMD5(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plainText.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest);
    }

    /* ================= BASE64 ================= */
    public static String encryptBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decryptBase64(String encrypted) {
        return Base64.getDecoder().decode(encrypted);
    }

    /* ================= AES ================= */
    private static SecretKey deriveKey(String secret, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static String encryptAES(String plainText, String secret, String salt) throws Exception {

        byte[] iv = new byte[GCM_IV_LENGTH];
        SECURE_RANDOM.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, deriveKey(secret, salt), new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        byte[] result = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(cipherText, 0, result, iv.length, cipherText.length);

        return encryptBase64(result);
    }

    public static String decryptAES(String encrypted, String secret, String salt) throws Exception {

        byte[] decoded = decryptBase64(encrypted);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] cipherText = new byte[decoded.length - GCM_IV_LENGTH];

        System.arraycopy(decoded, 0, iv, 0, iv.length);
        System.arraycopy(decoded, iv.length, cipherText, 0, cipherText.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, deriveKey(secret, salt), new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }

    /* ================= RSA ================= */

    public static KeyPair getRSAKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        return gen.generateKeyPair();
    }

    public static String encryptRSA(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return encryptBase64(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decryptRSA(String encrypted, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(decryptBase64(encrypted)), StandardCharsets.UTF_8);
    }

    /* ================= UUID ================= */

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}