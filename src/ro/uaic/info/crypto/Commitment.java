package ro.uaic.info.crypto;

import ro.uaic.info.DTO.CommitmentDTO;
import ro.uaic.info.client.Client;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alin on 3/7/18.
 */
public class Commitment extends SignedMessage {

    private CommitmentDTO representation;

    // todo these 2 should be maps, duh, Map<Integer, String>
    private List<String> lastPaywords;

//    How many paywords have been used by each chain
    private List<Integer> usedPaywords = new ArrayList<>();

    public Commitment(String message, String signature) {
        super(message, signature);
    }

    public static Commitment generateCommitment(String message, String signature) {
        return new Commitment(message, signature);
    }

    public Commitment setRepresentation(CommitmentDTO representation) {
        this.representation = representation;
        for (int i = 0; i< representation.getChainLengths().size(); i++)
            usedPaywords.add(1);
        return this;
    }

    public CommitmentDTO getRepresentation() {
        return representation;
    }

    public List<String> getLastPaywords() {
        return lastPaywords;
    }

    public List<Integer> getUsedPaywords() {
        return usedPaywords;
    }

    public void setChainRoots(List<String> chainRoots) {
        this.lastPaywords = new ArrayList<>(chainRoots);
    }


    public boolean processPayword(String payword, int index, int steps) throws NoSuchAlgorithmException {
        String paywordCopy = payword;
        if (usedPaywords.get(index) + steps > representation.getChainLengths().get(index))
            return false;
        String paywordToCheckAgainst = lastPaywords.get(index);
        for (int i = 0; i < steps; i++) {
//            apply hash function to the payword
            payword = CryptoUtils.generateHash(payword);
        }
        if (paywordToCheckAgainst.equals(payword)) {
            lastPaywords.set(index, paywordCopy);
            usedPaywords.set(index, usedPaywords.get(index) + steps);
            return true;
        }
        return false;
    }
}
