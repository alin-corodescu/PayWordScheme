package ro.uaic.info.crypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.PublicKey;
import java.util.Date;

/**
 * C(U) - class that represents the bank certificate, stating the user can pay via his account from the bank
 */
public class ClientCertificate extends SignedMessage{
    ObjectMapper mapper;
//    Constructor pentru cazul B -> U : C(U). Broker-side
    public ClientCertificate(ClientCertificateRepresentation representation) {
        mapper = new ObjectMapper();
        try {
            this.message = mapper.writeValueAsString(representation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.signature = message;
    }


    public ClientCertificate(String json) {

    }
}
