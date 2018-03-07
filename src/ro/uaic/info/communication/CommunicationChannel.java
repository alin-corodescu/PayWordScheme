package ro.uaic.info.communication;

public interface CommunicationChannel {
    String readMessage();
    void writeMessage(String message);
}
