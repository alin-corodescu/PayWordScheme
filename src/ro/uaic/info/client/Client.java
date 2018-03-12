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
import java.security.SignatureException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    private Map<Integer, Commitment> commitments;
//    This map stores the HashChains for each commmitment
    private Map<Commitment, List<HashChain>> hashChains;

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
        channel.writeMessage("1");
        // now lets generate a commitment
        String price = channel.readMessage();
        CommitmentDTO commitmentDTO = new CommitmentDTO();
        commitmentDTO.setClientCertificateString(clientCertificate.getMessage());
        commitmentDTO.setClientCertSignature(clientCertificate.getSignature());
        commitmentDTO.setD(Date.from(Instant.now()));
        commitmentDTO.setV("vendor");
        List<String> chainRoots = new ArrayList<>();
        chainRoots.add("hello");
        List<Integer> chainValues = new ArrayList<>();
        chainValues.add(1);

        List<Integer> chainLengths = new ArrayList<>();
        chainValues.add(1);

        commitmentDTO.setChainRoots(chainRoots);
        commitmentDTO.setChainLengths(chainLengths);
        commitmentDTO.setChainValues(chainValues);


        String json = JsonMapper.generateJsonFromDTO(commitmentDTO);
        channel.writeMessage(json);
        channel.writeMessage(CryptoUtils.sign(json, keyPair.getPrivate()));

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

    private void generateHashChain(){

    }

    public Commitment hasValidCommitment(int vendorPort) {
// todo check if the commitment is still valid
//        todo if the date is invalid, remove the element from the map
        return commitments.getOrDefault(vendorPort, null);
    }

//    Commitment negotiateNewCommitment(int vendorPort) {
//        List<HashChain> chains = new ArrayList<>();
//        HashChain chain = new HashChain(100);
//        chains.add(chain);
//        Commitment commit = Commitment.generateCommitment(vendorPort, this.clientCertificate, chain.getC0(),
//                new Date(), " ");
//        hashChains.put(commit, chains);
//        commitments.put(vendorPort, commit);
//        return commit;
//    }

//    void orderProduct() {
//        Commitment c = hasValidCommitment(1);
//        List<HashChain> l;
//        if (c != null) {
//            l = hashChains.get(c);
//        }
//        else {
//            l = hashChains.get(negotiateNewCommitment(1));
//        }
//        new ProductConsumer(communicationChannel, "1",  l);
//    }
}
