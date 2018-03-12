package ro.uaic.info.crypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.uaic.info.DTO.ClientCertificateDTO;

/**
 * C(U) - class that represents the bank certificate, stating the user can pay via his account from the bank
 */
public class ClientCertificate extends SignedMessage{

//    Best method of storing individual fields - do not try this at home
    private ClientCertificateDTO representation = null;

    public ClientCertificate(String message, String signature) {
        super(message, signature);
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMessage() {

        return message;
    }

    public String getSignature() {
        return signature;
    }


    public ClientCertificateDTO getRepresentation() {
        return representation;
    }

    public void setRepresentation(ClientCertificateDTO representation) {
        this.representation = representation;
    }
}
