package ro.uaic.info.communication;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;


public class SocketCommunicationChannel extends CommunicationChannel {
    Socket socket;

    public SocketCommunicationChannel(Socket socket){
       this.socket = socket;
    }

    public SocketCommunicationChannel(){
    }

    @Override
    public String readMessage() throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        int length = in.readInt();
        int offset = Integer.toString(length).length();
        byte[] data = new byte[length];
        in.readFully(data, 0, length);

        for (DataTransformer inputTransformer : inputTransformers) {
//            APPLY WILL DECRYPT THE DATA
            data = inputTransformer.transform(data, length);
        }
//        removing the length of the message from the actual message
        String response = new String(data);
        response = response.substring(offset);
        return response;
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
