package ro.uaic.info.broker;

import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.Server;
import ro.uaic.info.crypto.CryptoUtils;

import java.security.KeyPair;
import java.util.Date;


/**
 * Created by alin on 3/7/18.
 */
public class Broker {

    private static int SERVER_PORT = 4321;
    private AccountDatabase database;
    private KeyPair keyPair;
    private String identity;
    private Server server;

    public static void main(String[] args) {
//        Wait for a connection
        new Broker().run();
    }

    private void run() {
        ClientHandler clientHandler = new BrokerClientHandler(database,keyPair.getPublic(),identity);
        server = new Server(clientHandler, SERVER_PORT);
        server.start();
    }

    private void Broker() throws Exception {
        this.database = new MemoryAccountDatabase();
        this.keyPair = CryptoUtils.generateKeyPair();
        this.identity = "broker";
    }
}
