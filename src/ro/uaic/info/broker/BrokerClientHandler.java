package ro.uaic.info.broker;

import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.CommunicationChannel;

import java.net.Socket;

/**
 * Created by alin on 3/7/18.
 */
public class BrokerClientHandler implements ClientHandler {
    private final AccountDatabase db;

    public BrokerClientHandler(AccountDatabase database) {
        this.db = database;
    }

    @Override
    public void handleClient(Socket socket) {
//
        Thread t = new Thread(() -> {
//                socket...
            CommunicationChannel channel = null;
            channel.withInputTransformer(s -> encrypt(s));
            String message = channel.readMessage();
//            ASTEPT IDENTITATE CLIENT
//            TRIMIT RASPUNS CU CERTIFICATUL
            if (message.startsWith("identitate"))
        }).start();


    }
}
