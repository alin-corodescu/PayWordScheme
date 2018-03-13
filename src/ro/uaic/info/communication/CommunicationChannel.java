package ro.uaic.info.communication;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommunicationChannel {

    List<DataTransformer> inputTransformers = new ArrayList<>();
    protected List<DataTransformer> outputTransformers = new ArrayList<>();

    public abstract String readMessage() throws IOException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException;
    public abstract void writeMessage(String message) throws IOException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException;

    public void withInputTransformer(DataTransformer transformer) {
        inputTransformers.add(transformer);
    }
    public void withOutputTransformer(DataTransformer transformer) {
        outputTransformers.add(transformer);
    }

}
