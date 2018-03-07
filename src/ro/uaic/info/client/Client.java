package ro.uaic.info.client;

import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.ClientCertificate;
import ro.uaic.info.crypto.Commitment;
import ro.uaic.info.crypto.HashChain;

import java.rmi.server.UID;
import java.sql.Time;
import java.time.Instant;
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
//    This map stores the commitments for each VENDOR
    private Map<Integer, Commitment> commitments;
//    This map stores the HashChains for each commmitment
    private Map<Commitment, List<HashChain>> hashChains;
    /**
     * Connects to the localhost:port broker and registers itself with it
     * @param port
     */
    private ClientCertificate registerWithBroker(int port) {
        return new ClientCertificate();
    }

    private void generateHashChain(){

    }

    public CommunicationChannel connectWithVendor(int vendorPort){
        return new SocketCommunicationChannel();
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
        new ProductConsumer(1, "1",  l);
    }

    public static void main(String[] args) {
        // in functie de parametri
        new Client().run(args);
    }

    private void run(String[] args) {
        ClientCertificate certificate = registerWithBroker(4321);

        CommunicationChannel channel = connectWithVendor(4322);

//        Logica de a cumpara ceva de la vendor

    }
    private Client() {

    }
}
