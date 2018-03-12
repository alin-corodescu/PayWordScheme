package ro.uaic.info.vendor;

import ro.uaic.info.DTO.ClientCertificateDTO;
import ro.uaic.info.DTO.CommitmentDTO;
import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.Commitment;
import ro.uaic.info.crypto.CryptoUtils;
import ro.uaic.info.json.JsonMapper;

import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alin on 3/7/18.
 */
public class VendorClientHandler implements ClientHandler {
    Map<String, Commitment> commitmentMap = new HashMap<>();
    @Override
    public void handleClient(Socket socket) throws Exception {

        Commitment commitment;

        System.out.println("A new client is connecting to the vendor");
        CommunicationChannel channel = new SocketCommunicationChannel(socket);
        String message = channel.readMessage();
        int productNumber = Integer.valueOf(message);

        System.out.println("Sending client the price for product " + productNumber);
        switch (productNumber) {
            case 1:
            {
                channel.writeMessage("10");
                message = channel.readMessage();
                if (message.equals("existing commitment")) {
                    String identity = channel.readMessage();
                    commitment = getCommitmentFor(identity);
                }
                else {
//                    message contains the commitment
                    CommitmentDTO commitmentDTO =
                            (CommitmentDTO) JsonMapper.generateObjectFromJSON(message, CommitmentDTO.class);

                    ClientCertificateDTO clientCertificateDTO = (ClientCertificateDTO) JsonMapper.generateObjectFromJSON(commitmentDTO.getClientCertificateString(), ClientCertificateDTO.class);

                    String signature = channel.readMessage();
                    if (CryptoUtils.verify(message, signature, CryptoUtils.getKeyFromBase64(clientCertificateDTO.getKu())))
                        System.out.println("Customer signature is valid");
                    else {
                        System.out.println("Customer signature is invalid");
                        System.exit(13);
                    }

                    PublicKey bankKey = CryptoUtils.getKeyFromBase64(clientCertificateDTO.getKb());

                    if (CryptoUtils.verify(commitmentDTO.getClientCertificateString(), commitmentDTO.getClientCertSignature(), bankKey))
                        System.out.println("Customer certificate is valid");
                    else {
                        System.out.println("Customer certificate is invalid");
                        System.exit(13);
                    }
                }
            }
        }
    }

    private Commitment getCommitmentFor(String identity) {
        return commitmentMap.get(identity);
    }
}
