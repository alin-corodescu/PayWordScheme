package ro.uaic.info.DTO;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CommitmentDTO {
    private String V;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommitmentDTO that = (CommitmentDTO) o;
        return Objects.equals(V, that.V) &&
                Objects.equals(clientCertificateString, that.clientCertificateString) &&
                Objects.equals(clientCertSignature, that.clientCertSignature) &&
                Objects.equals(chainRoots, that.chainRoots) &&
                Objects.equals(chainValues, that.chainValues) &&
                Objects.equals(chainLengths, that.chainLengths) &&
                Objects.equals(d, that.d);
    }

    @Override
    public int hashCode() {

        return Objects.hash(V, clientCertificateString, clientCertSignature, chainRoots, chainValues, chainLengths, d);
    }

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
