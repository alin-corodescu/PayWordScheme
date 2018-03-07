package ro.uaic.info.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by alin on 3/7/18.
 */
public class Server {
    private ClientHandler handler;
    private int port;
    public Server(ClientHandler handler, int port) {
        this.handler = handler;
        this.port = port;
    }

    /**
     * Starts listening for incoming connections
     */
    public void start() {
//    When a new connection comes, handlerFactory.create();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handler.handleClient(clientSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
