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



        System.out.println("A new client is connecting to the vendor");
        CommunicationChannel channel = new SocketCommunicationChannel(socket);
//        Sell 2 products in total
        for (int i = 0; i < 2; i++) {
            String message = channel.readMessage();
            int productNumber = Integer.valueOf(message);
            switch (productNumber) {
                case 1: {
                    handleProduct(10, channel);
                    break;
                }
                case 2 :{
                    handleProduct(17, channel);
                }
            }
        }
    }

    private void handleProduct(int price, CommunicationChannel channel) throws Exception {
        int sum = 0;
        Commitment commitment;
        System.out.println("Sending client the price for product " + price);
        channel.writeMessage(String.valueOf(price));
        String message = channel.readMessage();
        if (message.equals("existing commitment")) {
            String identity = channel.readMessage();
            commitment = getCommitmentFor(identity);
        } else {
            commitment = checkCommitment(message, channel);
            ClientCertificateDTO clientCertificateDTO = (ClientCertificateDTO) JsonMapper.generateObjectFromJSON(commitment.getRepresentation().getClientCertificateString(), ClientCertificateDTO.class);
            commitment.setChainRoots(commitment.getRepresentation().getChainRoots());
            commitmentMap.put(clientCertificateDTO.getU(), commitment);
        }

        while (sum < price) {
//                Read the payword sent by the buyer
            String payword = channel.readMessage();
//                From which chain is it?
            int index = Integer.valueOf(channel.readMessage());
//                How many steps did we skip? 1 = the next payword
            int steps = Integer.valueOf(channel.readMessage());

            if (commitment.processPayword(payword, index, steps)) {
                System.out.println("Got the payment from chain " + index + " with " + steps + " steps");
                sum += commitment.getRepresentation().getChainValues().get(index) * steps;
                System.out.println("Balance is now " + (price - sum));
            }
        }
    }

    private Commitment checkCommitment(String message, CommunicationChannel channel) throws Exception {
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

        return new Commitment(message,signature).setRepresentation(commitmentDTO);
    }

    private Commitment getCommitmentFor(String identity) {
        return commitmentMap.get(identity);
    }
}
