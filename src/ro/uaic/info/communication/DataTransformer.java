package ro.uaic.info.communication;

public interface DataTransformer {

    byte[] transform(byte[] input, int length);
}
