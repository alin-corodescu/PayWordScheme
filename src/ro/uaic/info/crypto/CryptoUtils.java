package ro.uaic.info.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CryptoUtils {
    public static String getBase64FromKey(PublicKey key) {
        return new String(Base64.getEncoder().encode(key.getEncoded()));
    }

    public static PublicKey getKeyFromBase64(String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec pk = new X509EncodedKeySpec(keyBytes);

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(pk);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }


    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String symmetricEncrypt(String plainText, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptCipher = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(key, "AES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, k);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String symmetricDecrypt(String encryptedText, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] bytes = Base64.getDecoder().decode(encryptedText);
        Cipher encryptCipher = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(key, "AES");
        encryptCipher.init(Cipher.DECRYPT_MODE, k);
        byte[] plainText = encryptCipher.doFinal(bytes);
        return new String(plainText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), UTF_8);
    }

    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

    public static String generateHash(String payword) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(payword.getBytes());
        return new String(Base64.getEncoder().encode(md.digest()));
    }

    public static boolean checkHash(String rootHash, String currentHash, int steps) throws NoSuchAlgorithmException {
        for (int i = 0; i < steps; i++) {
            rootHash = CryptoUtils.generateHash(rootHash);
        }
        return rootHash.equals(currentHash);
    }

    public static Key generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        return generator.generateKey();
    }
}
