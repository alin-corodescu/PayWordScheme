package ro.uaic.info.crypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.uaic.info.DTO.ClientCertificateDTO;

/**
 * C(U) - class that represents the bank certificate, stating the user can pay via his account from the bank
 */
public class ClientCertificate extends SignedMessage{

//    Constructor pentru cazul B -> U : C(U). Broker-side
    public static ClientCertificate generateCertificateFromRepresentation(ClientCertificateDTO representation) {
        ClientCertificate certificate = new ClientCertificate();

        ObjectMapper mapper = new ObjectMapper();
        try {
            certificate.message = mapper.writeValueAsString(representation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        certificate.signature = certificate.message;

        return certificate;
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


}
