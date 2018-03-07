package ro.uaic.info.client;

import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.ClientCertificate;

import java.rmi.server.UID;

/**
 * Created by alin on 3/7/18.
 */
public class Client {

    private CommunicationChannel communicationChannel;
    private ClientCertificate clientCertificate;
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


}
