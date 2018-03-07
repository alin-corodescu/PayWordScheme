package ro.uaic.info.communication;

import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommunicationChannel {

    List<DataTransformer> inputTransformers = new ArrayList<>();
    protected List<DataTransformer> outputTransformers = new ArrayList<>();

    abstract String readMessage() throws IOException;
    abstract void writeMessage(String message) throws IOException;

    public void withInputTransformer(DataTransformer transformer) {
        inputTransformers.add(transformer);
    }
    public void withOutputTransformer(DataTransformer transformer) {
        outputTransformers.add(transformer);
    }

}
