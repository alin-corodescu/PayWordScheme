package ro.uaic.info.vendor;

import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.Server;

import java.util.Timer;

/**
 * Created by alin on 3/7/18.
 */
public class Vendor {

    private static int SERVER_PORT = 4322;
    private static int BROKER_PORT = 4321;
    private Server server;

    public static void main(String[] args) throws Exception {
//        Wait for a connection
        new Vendor().run();
    }

    private void run() throws Exception {
        ClientHandler clientHandler = new VendorClientHandler();
        RedeemPayWordTask redeemPayWordTask = new RedeemPayWordTask(clientHandler, BROKER_PORT);
        new Timer().scheduleAtFixedRate(redeemPayWordTask,15000, 15000);
        server = new Server(clientHandler, SERVER_PORT);
        server.start();
    }

}
