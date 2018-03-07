package ro.uaic.info.communication;

public interface CommunicationChannel {
    String readMessage();
    String writeMessage();
}
