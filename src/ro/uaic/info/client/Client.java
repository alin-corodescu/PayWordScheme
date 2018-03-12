package ro.uaic.info.client;

import ro.uaic.info.DTO.ClientCertificateDTO;
import ro.uaic.info.DTO.ClientInformationDTO;
import ro.uaic.info.DTO.CommitmentDTO;
import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.*;
import ro.uaic.info.json.JsonMapper;

import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;
import java.time.Instant;


/**
 * Created by alin on 3/7/18.
 */
public class Client {
    private CommunicationChannel communicationChannel;
    private ClientCertificate clientCertificate;
    private KeyPair keyPair;
    private String identity;
    private static final int BrokerPort = 4321;
    private static final int VendorPort = 4322;
//    This map stores the commitments for each VENDOR
    private Map<String, CommitmentDTO> commitments = new HashMap<>();
//    This map stores the HashChains for each commmitment
    private Map<CommitmentDTO, List<HashChain>> hashChains = new HashMap<>();

    public static void main(String[] args) {
        // in functie de parametri
        try {
            new Client().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run(String[] args) throws Exception {
        this.InitializeClientData();

        System.out.println("Client is registering with the broker!");
        registerWithBroker(BrokerPort);

        CommunicationChannel channel = connectWithVendor(VendorPort);
//        Logica de a cumpara ceva de la vendor
//        int productNumber = Integer.valueOf(args[1]);
//        System.out.println("Clients intends to buy product number " + args[1]);
        for (int i = 1; i <= 2; i++) {
            channel.writeMessage(String.valueOf(i));
            // now lets generate a commitment
            int price = Integer.valueOf(channel.readMessage());
            boolean alreadNegotiated = commitments.containsKey("vendor");
            if (alreadNegotiated) {
                System.out.println("Already negotiated with this vendor");
                channel.writeMessage("existing commitment");
                channel.writeMessage(clientCertificate.getRepresentation().getU());
            }

            CommitmentDTO commitmentDTO = getCommitmentForVendor("vendor");
            if (!alreadNegotiated) {
                String json = JsonMapper.generateJsonFromDTO(commitmentDTO);
                channel.writeMessage(json);
                channel.writeMessage(CryptoUtils.sign(json, keyPair.getPrivate()));
            }


            List<HashChain> chains = hashChains.get(commitmentDTO);

            HashChain chain = chains.get(2);
            int paywordCount = price / 10;
            price -= 10 * paywordCount;
            String payword = chain.getPaywordForSteps(paywordCount);
//            Send paywordCount payments of 10
            if (paywordCount != 0) {
                System.out.println("Sending out " + paywordCount + " payments of 10");
                channel.writeMessage(payword);
                channel.writeMessage("2");
                channel.writeMessage(String.valueOf(paywordCount));
            }

            chain = chains.get(1);
            paywordCount = price / 5;

            price -= 5 * paywordCount;
            payword = chain.getPaywordForSteps(paywordCount);
            if (paywordCount != 0) {
                System.out.println("Sending out " + paywordCount + " payments of 5");
//            Send paywordCount payments of 5
                channel.writeMessage(payword);
                channel.writeMessage("1");
                channel.writeMessage(String.valueOf(paywordCount));
            }

            chain = chains.get(0);
            paywordCount = price;

            price = 0;
            payword = chain.getPaywordForSteps(paywordCount);
            if (paywordCount != 0) {
                System.out.println("Sending out " + paywordCount + " payments of 1");
//            Send paywordCount payments of 1
                channel.writeMessage(payword);
                channel.writeMessage("0");
                channel.writeMessage(String.valueOf(paywordCount));
            }
        }
        Thread.sleep(600000);
    }

    private void InitializeClientData() throws Exception {
        this.keyPair = CryptoUtils.generateKeyPair();
        this.identity = "Client";
    }

    private void registerWithBroker(int port) throws Exception {
        Socket socket = new Socket("localhost",port);
        this.communicationChannel = new SocketCommunicationChannel(socket);

//        U sends information to B to inform him that he is a user
        this.communicationChannel.writeMessage("client");

//        U sends personal information to B
        System.out.println("Client is sending personal information to broker!");
//        ClientInformationDTO clientInformationDTO = new ClientInformationDTO(this.keyPair.getPublic().toString(), this.identity);
        ClientInformationDTO clientInformationDTO = new ClientInformationDTO(CryptoUtils.getBase64FromKey(keyPair.getPublic()), this.identity);
        String message = JsonMapper.generateJsonFromDTO(clientInformationDTO);
        this.communicationChannel.writeMessage(message);

//        B sends to U certificate C(U) for U to present it to the vendor when making a purchase
        System.out.println("Client is receiving certificate from broker!");
        message = this.communicationChannel.readMessage();
        ClientCertificateDTO clientCertificateDTO = (ClientCertificateDTO)JsonMapper.generateObjectFromJSON(message, ClientCertificateDTO.class);


        System.out.println("Checking the bank's identity");
        String signature = this.communicationChannel.readMessage();

        this.clientCertificate = new ClientCertificate(message, signature);
        clientCertificate.setRepresentation(clientCertificateDTO);

        if (CryptoUtils.verify(message, signature, CryptoUtils.getKeyFromBase64(clientCertificateDTO.getKb()))
                && clientCertificateDTO.getKu().equals(CryptoUtils.getBase64FromKey(keyPair.getPublic())))
            System.out.println("The bank's identity is valid");
        else {
            System.out.println("The bank cannot be trusted");
            System.exit(13);
        }

    }

    public CommunicationChannel connectWithVendor(int vendorPort) throws IOException {
        Socket socket = new Socket("localhost", vendorPort);
        return new SocketCommunicationChannel(socket);
    }


    public CommitmentDTO getCommitmentForVendor(String vendor) throws NoSuchAlgorithmException {
    // todo check if the commitment is still valid
    // todo if the date is invalid, remove the element from the map
        if (commitments.containsKey(vendor)) {
            return commitments.get(vendor);
        }

        CommitmentDTO commitmentDTO = new CommitmentDTO();
        commitmentDTO.setClientCertificateString(clientCertificate.getMessage());
        commitmentDTO.setClientCertSignature(clientCertificate.getSignature());

        Date currentDate = new java.util.Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MINUTE, 2);
        currentDate = c.getTime();

        commitmentDTO.setD(currentDate);
        commitmentDTO.setV(vendor);

        List<String> chainRoots = new ArrayList<>();
        List<Integer> chainValues = new ArrayList<>();
        List<Integer> chainLengths = new ArrayList<>();

        List<HashChain> hcs = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            HashChain hc = new HashChain(10);
            hcs.add(hc);
            chainRoots.add(hc.getC0());
            chainLengths.add(10);
        }
        chainValues.add(1);
        chainValues.add(5);
        chainValues.add(10);

        commitmentDTO.setChainRoots(chainRoots);
        commitmentDTO.setChainLengths(chainLengths);
        commitmentDTO.setChainValues(chainValues);

        commitments.put(vendor, commitmentDTO);
        hashChains.put(commitmentDTO, hcs);
        return commitmentDTO;

    }

}
