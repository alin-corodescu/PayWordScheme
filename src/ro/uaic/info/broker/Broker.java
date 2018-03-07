package ro.uaic.info.broker;

import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.Server;


/**
 * Created by alin on 3/7/18.
 */
public class Broker {

    private static int SERVER_PORT = 4321;

    private AccountDatabase database;

    private Server server;

    public static void main(String[] args) {
//        Wait for a connection
        new Broker().run();
    }

    private void run() {
        ClientHandler clientHandler = new BrokerClientHandler(database);
        server = new Server(clientHandler, SERVER_PORT);
        server.start();
    }
}
