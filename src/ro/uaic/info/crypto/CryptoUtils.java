package ro.uaic.info.crypto;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptoUtils {
    public String getBase64FromKey(PublicKey key) {
        return new String(Base64.getEncoder().encode(key.getEncoded()));
    }

    public PublicKey getKeyFromBase64(String key) {
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
}
