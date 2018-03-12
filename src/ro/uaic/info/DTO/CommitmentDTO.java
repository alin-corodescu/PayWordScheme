package ro.uaic.info.DTO;

import java.util.Date;
import java.util.List;

public class CommitmentDTO {
    private String V;
    private String clientCertificateString;
    private String clientCertSignature;
    private List<String> chainRoots;
    private List<Integer> chainValues;
    private List<Integer> chainLengths;
    private Date d;

    public String getV() {
        return V;
    }

    public void setV(String v) {
        V = v;
    }

    public String getClientCertSignature() {
        return clientCertSignature;
    }

    public void setClientCertSignature(String clientCertSignature) {
        this.clientCertSignature = clientCertSignature;
    }

    public List<String> getChainRoots() {
        return chainRoots;
    }

    public void setChainRoots(List<String> chainRoots) {
        this.chainRoots = chainRoots;
    }

    public List<Integer> getChainValues() {
        return chainValues;
    }

    public void setChainValues(List<Integer> chainValues) {
        this.chainValues = chainValues;
    }

    public List<Integer> getChainLengths() {
        return chainLengths;
    }

    public void setChainLengths(List<Integer> chainLengths) {
        this.chainLengths = chainLengths;
    }

    public Date getD() {
        return d;
    }

    public void setD(Date d) {
        this.d = d;
    }

    public String getClientCertificateString() {
        return clientCertificateString;
    }

    public void setClientCertificateString(String clientCertificateString) {
        this.clientCertificateString = clientCertificateString;
    }
}
