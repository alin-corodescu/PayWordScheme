package ro.uaic.info.communication;

import java.net.Socket;
import java.util.List;

public class SocketCommunicationChannel implements CommunicationChannel {
    Socket socket;
    List<String> inputTransformers;
    @Override
    public String readMessage() {
        int length = socket.readInt();
        String data = socket.readString(length);

//        IF WE HAVE ENCRYPTION ENABLED
        if (inputTransformers.isEmpty())
            return data;

        for (String inputTransformer : inputTransformers) {
//            APPLY WILL DECRYPT THE DATA
            data = inputTransformer.transform(data);
        }

        return data;
    }

    @Override
    public void writeMessage(String message) {

    }
}
