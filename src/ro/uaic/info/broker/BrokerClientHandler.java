package ro.uaic.info.broker;

import ro.uaic.info.DTO.ClientCertificateDTO;
import ro.uaic.info.DTO.ClientInformationDTO;
import ro.uaic.info.DTO.CommitmentDTO;
import ro.uaic.info.DTO.CurrentPaymentsDTO;
import ro.uaic.info.client.Client;
import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.ClientCertificate;
import ro.uaic.info.crypto.CryptoUtils;
import ro.uaic.info.json.JsonMapper;

import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            System.out.println("\nBroker Client Handler got a request from an unknown source!");
            CommunicationChannel channel = new SocketCommunicationChannel(socket);
            String message = channel.readMessage();

//            Use case: client
            if (message.equals("client")) {
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

            if (message.equals("vendor")) {
                System.out.println("Broker Client Handler received a message from a vendor!");
                Boolean response = true;
                message = channel.readMessage();
                CommitmentDTO commitmentDTO =
                        (CommitmentDTO) JsonMapper.generateObjectFromJSON(message, CommitmentDTO.class);

                ClientCertificateDTO clientCertificateDTO = (ClientCertificateDTO) JsonMapper.generateObjectFromJSON(commitmentDTO.getClientCertificateString(), ClientCertificateDTO.class);

//                TODO get client signature without requesting it from a channel
                String signature = commitmentDTO.getClientCertSignature();
                if (CryptoUtils.verify(message, signature, CryptoUtils.getKeyFromBase64(clientCertificateDTO.getKu())))
                    System.out.println("Customer signature is valid");
                else {
                    System.out.println("Customer signature is invalid");
                }

                if (CryptoUtils.verify(commitmentDTO.getClientCertificateString(), commitmentDTO.getClientCertSignature(), this.brokerPublicKey))
                    System.out.println("Customer certificate is valid");
                else {
                    System.out.println("Customer certificate is invalid");
                    System.exit(13);
                }

                message = channel.readMessage();
                CurrentPaymentsDTO currentPaymentsDTO = new CurrentPaymentsDTO();
                currentPaymentsDTO = (CurrentPaymentsDTO) JsonMapper.generateObjectFromJSON(message, currentPaymentsDTO.getClass());
                List<String> lastPayWords = currentPaymentsDTO.getLastPaywords();
                List<Integer> usedPayWords = currentPaymentsDTO.getUsedPaywords();
                List<String> chainRoots = commitmentDTO.getChainRoots();
                System.out.println(lastPayWords);
                System.out.println(usedPayWords);
                System.out.println(chainRoots);
                for (int i = 0; i < lastPayWords.size(); i++) {
                    String rootHash = chainRoots.get(i);
                    if (CryptoUtils.checkHash(rootHash, lastPayWords.get(i), usedPayWords.get(i))) {
                        System.out.println("Hash " + i + "  matched!");
                    } else {
                        System.out.println("Hash " + i + " did NOT matched!");
                        response = false;
                    }
                }
                channel.writeMessage(response.toString());
                if (response) {
                    System.out.println("Sending money to vendor!");
                }
                else{
                    System.out.println("Vendor request was denied!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ClientCertificateDTO clientRegistrationResponse(ClientInformationDTO clientInformationDTO) {
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

    private Date getExpirationDate() {
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MINUTE, 2);
        currentDate = c.getTime();
        return currentDate;
    }
}
