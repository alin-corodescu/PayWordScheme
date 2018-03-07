package ro.uaic.info.communication;

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

    }
}
