package ro.uaic.info.crypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.PublicKey;
import java.util.Date;

/**
 * C(U) - class that represents the bank certificate, stating the user can pay via his account from the bank
 */
public class ClientCertificate extends SignedMessage{

//    Constructor pentru cazul B -> U : C(U). Broker-side
    public static ClientCertificate generateCertificateFromRepresentation(ClientCertificateRepresentation representation) {
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


}
