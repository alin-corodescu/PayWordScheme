package ro.uaic.info.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class HashChain {

    String[] paywords;
    private int nextIndex = 0;
    private String cn;
    private String c0;
    public HashChain(int chainLength) throws NoSuchAlgorithmException {
        paywords = new String[chainLength + 1];
        byte[] buffer = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(buffer);
        cn = CryptoUtils.generateHash(new String(buffer));
        paywords[chainLength] = cn;
        for (int  i = chainLength - 1; i >= 0; i--) {
            paywords[i] = CryptoUtils.generateHash(paywords[i+1]);
        }
    }

    public String getC0() {
        return paywords[0];
    }

    public String getAtIndex(int index) {
        nextIndex = index + 1;
        return paywords[index];
    }

    public String getPaywordForSteps(int paywordCount) {
        String result = paywords[nextIndex + paywordCount];
        nextIndex += paywordCount;
        return result;

    }
}
