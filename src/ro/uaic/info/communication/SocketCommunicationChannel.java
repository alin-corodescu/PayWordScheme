package ro.uaic.info.communication;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class SocketCommunicationChannel extends CommunicationChannel {
    Socket socket;

    public SocketCommunicationChannel(Socket socket){
       this.socket = socket;
    }

    public SocketCommunicationChannel(){
    }

    @Override
    public String readMessage() throws IOException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readFully(data, 0, length);

        for (DataTransformer outputTransformer : outputTransformers) {
//            APPLY WILL DECRYPT THE DATA
            data = outputTransformer.transform(data, length);
        }
        String response = new String(data);
        return response;
    }

    @Override
    public void writeMessage(String message) throws IOException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        byte[] data = message.getBytes();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        for (DataTransformer inputTransformer : inputTransformers) {
            data = inputTransformer.transform(data, message.length());
        }

        out.writeInt(data.length);
        out.write(data);
    }
}
