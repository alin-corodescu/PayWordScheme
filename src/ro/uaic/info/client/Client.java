package ro.uaic.info.client;

import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.*;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
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

        ClientCertificate certificate = registerWithBroker(BrokerPort);

        CommunicationChannel channel = connectWithVendor(VendorPort);
//        Logica de a cumpara ceva de la vendor

    }

    private void InitializeClientData() throws Exception {
        this.keyPair = CryptoUtils.generateKeyPair();
        this.identity = "Client";
    }

    /**
     * Connects to the localhost:port broker and registers itself with it
     * @param port
     */
    private ClientCertificate registerWithBroker(int port) throws IOException {
        Socket socket = new Socket("localhost",port);
        this.communicationChannel = new SocketCommunicationChannel(socket);

//        U sends personal information to B
//        TODO JSON
//        String message = JSON(this.identity, this.keyPair.getPublic())
        String message = "Hello";
        this.communicationChannel.writeMessage(message);

//        B sends to U certificate C(U) for U to present it to the vendor when making a purchase
        message = this.communicationChannel.readMessage();
//        TODO JSON
//        this.clientCertificate = JSON(message)

//        U checks C(U) by checking B's signature using it's public key
//        bPublicKey = JSON_object['Kb']
//        bSignature = JSON_object['signature']
//        bSignedMessage = JSON_object['signedMessage']
//        if CryptoUtils.verify(signedMessage, bSignature, bPublicKey) == False
//          throw new SignatureException("The certificate provided by broker is not authentic!");
        return new ClientCertificate();
    }

    public CommunicationChannel connectWithVendor(int vendorPort){
        return new SocketCommunicationChannel();
    }

    private void generateHashChain(){

    }

    public Commitment hasValidCommitment(int vendorPort) {
// todo check if the commitment is still valid
//        todo if the date is invalid, remove the element from the map
        return commitments.getOrDefault(vendorPort, null);
    }

    Commitment negotiateNewCommitment(int vendorPort) {
        List<HashChain> chains = new ArrayList<>();
        HashChain chain = new HashChain(100);
        chains.add(chain);
        Commitment commit = Commitment.generateCommitment(vendorPort, clientCertificate, chain.getC0(),
                new Date(), " ");
        hashChains.put(commit, chains);
        commitments.put(vendorPort, commit);
        return commit;
    }

    void orderProduct() {
        Commitment c = hasValidCommitment(1);
        List<HashChain> l;
        if (c != null) {
            l = hashChains.get(c);
        }
        else {
            l = hashChains.get(negotiateNewCommitment(1));
        }
        new ProductConsumer(communicationChannel, "1",  l);
    }
}
