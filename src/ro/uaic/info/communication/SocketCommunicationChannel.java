package ro.uaic.info.communication;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocketCommunicationChannel extends CommunicationChannel {
    Socket socket;

    @Override
    public String readMessage() throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readFully(data, 0, length);

        for (DataTransformer inputTransformer : inputTransformers) {
//            APPLY WILL DECRYPT THE DATA
            data = inputTransformer.transform(data, length);
        }

        return new String(data);
    }

    @Override
    public void writeMessage(String message) throws IOException {
        byte[] data = message.getBytes();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        for (DataTransformer outputTransformer : outputTransformers) {
            data = outputTransformer.transform(data, message.length());
        }

        out.writeInt(data.length);
        out.write(data);

    }
}
