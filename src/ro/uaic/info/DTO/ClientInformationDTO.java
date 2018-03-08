package ro.uaic.info.DTO;

public class ClientInformationDTO {
    private String clientKey;
    private String clientIdentity;

    public ClientInformationDTO(String clientKey, String clientIdentity){
        this.clientKey = clientKey;
        this.clientIdentity = clientIdentity;
    }

    public ClientInformationDTO(){}

    public void setClientIdentity(String clientIdentity) {
        this.clientIdentity = clientIdentity;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getClientKey() {

        return this.clientKey;
    }

    public String getClientIdentity() {
        return this.clientIdentity;
    }
}
