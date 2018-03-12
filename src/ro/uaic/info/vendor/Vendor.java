package ro.uaic.info.vendor;

import ro.uaic.info.broker.AccountDatabase;
import ro.uaic.info.broker.Broker;
import ro.uaic.info.broker.BrokerClientHandler;
import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.Server;

/**
 * Created by alin on 3/7/18.
 */
public class Vendor {

    private static int SERVER_PORT = 4322;

    private Server server;

    public static void main(String[] args) throws Exception {
//        Wait for a connection
        new Vendor().run();
    }

    private void run() throws Exception {
        ClientHandler clientHandler = new VendorClientHandler();
        server = new Server(clientHandler, SERVER_PORT);
        server.start();
    }

}
