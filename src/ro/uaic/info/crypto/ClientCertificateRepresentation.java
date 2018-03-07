package ro.uaic.info.crypto;

import java.security.PublicKey;
import java.util.Date;

public class ClientCertificateRepresentation {
    private String B;
    private String U;
    private String IPu;
    private PublicKey Kb;
    private String info;

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getU() {
        return U;
    }

    public void setU(String u) {
        U = u;
    }

    public String getIPu() {
        return IPu;
    }

    public void setIPu(String IPu) {
        this.IPu = IPu;
    }

    public PublicKey getKb() {
        return Kb;
    }

    public void setKb(PublicKey kb) {
        Kb = kb;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
