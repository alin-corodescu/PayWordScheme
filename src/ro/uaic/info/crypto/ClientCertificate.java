package ro.uaic.info.crypto;

import java.security.PublicKey;
import java.util.Date;

/**
 * C(U) - class that represents the bank certificate, stating the user can pay via his account from the bank
 */
public class ClientCertificate extends SignedMessage{
//    Constructor pentru cazul B -> U : C(U). Broker-side
    public ClientCertificate(String B, String U, String IPU, PublicKey KB, PublicKey KU, Date exp, String info) {
        this.message = null; //concatenare de toate astea
        this.signature = sign(message, KB);
    }

//    Cand U primeste C(U), el primeste defapt un json cu doua field-uri: message, signature
    public String toJson() {

    }

    public ClientCertificate(String json) {

    }
}
