package ro.uaic.info.crypto;

import ro.uaic.info.client.Client;

import java.util.Date;

/**
 * Created by alin on 3/7/18.
 */
public class Commitment extends SignedMessage {

    public static Commitment generateCommitment(int vendorId, ClientCertificate certificate,
                                                String c0, Date d, String info) {
        return new Commitment();
    }

}
