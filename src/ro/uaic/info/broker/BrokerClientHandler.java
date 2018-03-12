package ro.uaic.info.broker;

import ro.uaic.info.DTO.ClientCertificateDTO;
import ro.uaic.info.DTO.ClientInformationDTO;
import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.CryptoUtils;
import ro.uaic.info.json.JsonMapper;

import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alin on 3/7/18.
 */
public class BrokerClientHandler implements ClientHandler {
    private AccountDatabase brokerDatabase;
    private PublicKey brokerPublicKey;
    private PrivateKey brokerPrivateKey;
    private String brokerIdentity;
    public BrokerClientHandler(AccountDatabase brokerDatabase, PrivateKey brokerPrivateKey, PublicKey brokerPublicKey, String brokerIdentity) {
        this.brokerDatabase = brokerDatabase;
        this.brokerPublicKey = brokerPublicKey;
        this.brokerIdentity = brokerIdentity;
        this.brokerPrivateKey = brokerPrivateKey;
    }

    @Override
    public void handleClient(Socket socket) throws Exception {
        try {
            System.out.println("Broker Client Handler got a request from an unknown source!");
            CommunicationChannel channel = new SocketCommunicationChannel(socket);
            String message = channel.readMessage();

//            Use case: client
            if(message.equals("client")) {
                System.out.println("Broker Client Handler received a message from a client!");
                message = channel.readMessage();
                ClientInformationDTO clientInformationDTO =
                        (ClientInformationDTO) JsonMapper.generateObjectFromJSON(message, ClientInformationDTO.class);

                System.out.println("Broker Client Handler sending certificate to client!");
                ClientCertificateDTO clientCertificateDTO = this.clientRegistrationResponse(clientInformationDTO);
                message = JsonMapper.generateJsonFromDTO(clientCertificateDTO);
                String signature = CryptoUtils.sign(message, brokerPrivateKey);
                channel.writeMessage(message);
                channel.writeMessage(signature);
            }

            if(message.equals("vendor")) {
                System.out.println("Broker Client Handler received a message from a vendor!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ClientCertificateDTO clientRegistrationResponse(ClientInformationDTO clientInformationDTO){
        ClientCertificateDTO clientCertificateDTO = new ClientCertificateDTO();

        clientCertificateDTO.setB(this.brokerIdentity);
        clientCertificateDTO.setKb(CryptoUtils.getBase64FromKey(this.brokerPublicKey));
        clientCertificateDTO.setExp(getExpirationDate());

        clientCertificateDTO.setU(clientInformationDTO.getClientIdentity());
        clientCertificateDTO.setKu(clientInformationDTO.getClientKey());
        clientCertificateDTO.setIPu("ip");
        clientCertificateDTO.setInfo("info");
        return clientCertificateDTO;
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
