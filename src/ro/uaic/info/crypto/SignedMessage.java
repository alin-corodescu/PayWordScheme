package ro.uaic.info.crypto;

/**
 * Created by alin on 3/7/18.
 */
public class SignedMessage {
    protected String message;

    public SignedMessage(String message, String signature) {
        this.message = message;
        this.signature = signature;
    }

    protected String signature;
}
