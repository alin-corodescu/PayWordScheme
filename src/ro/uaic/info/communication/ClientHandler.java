package ro.uaic.info.communication;

import java.net.Socket;

/**
 * Created by alin on 3/7/18.
 */
public interface ClientHandler {

    /**
     * Will handle the client connecting through the socket.
     * THIS METHOD HAS TO RETURN IMMEDIATLY - START ANOTHER THREAD
     * @param socket
     * https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
     */
    void handleClient(Socket socket);
}
