package ro.uaic.info.broker;

import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.ClientCertificateRepresentation;

import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alin on 3/7/18.
 */
public class BrokerClientHandler implements ClientHandler {
    private AccountDatabase brokerDatabase;
    private Key brokerPublicKey;
    private String brokerIdentity;
    public BrokerClientHandler(AccountDatabase brokerDatabase, Key brokerPublicKey, String brokerIdentity) {
        this.brokerDatabase = brokerDatabase;
        this.brokerPublicKey = brokerPublicKey;
        this.brokerIdentity = brokerIdentity;
    }

    @Override
    public void handleClient(Socket socket) {
        try {
            CommunicationChannel channel = new SocketCommunicationChannel(socket);
            String message = channel.readMessage();

//            TODO JSON
//            JSON_obj = JSON(message)

//            Use case: client
//            if JSON_obj.identity == 'client':
//            message = this.clientRegistrationResponse(JSON_obj);
             this.clientRegistrationResponse(message);
            channel.writeMessage(message);

//            Use case: vendor
//            add logic here
            System.out.println(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    The parameter will be changed to a JSON_Object type in order to gather all information
//    We have the brokers identity and public key
//    We need to also get the identity and public key of the user
    private ClientCertificateRepresentation clientRegistrationResponse(String json_response){
        ClientCertificateRepresentation clientCertificateRepresentation = new ClientCertificateRepresentation();

        clientCertificateRepresentation.setB(this.brokerIdentity);
        clientCertificateRepresentation.setKb(this.brokerPublicKey.toString());
        clientCertificateRepresentation.setExp(getExpirationDate());


//        clientCertificateRepresentation.setU(json_response['U']);
//        clientCertificateRepresentation.setKu(json_response['Ku']);
//        clientCertificateRepresentation.setIPu(json_response['IPu']);
        return clientCertificateRepresentation;
    }

    private Date getExpirationDate(){
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MINUTE, 2);
        currentDate = c.getTime();
        return currentDate;
    }
}
