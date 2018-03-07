package ro.uaic.info.crypto;

import java.security.PublicKey;
import java.util.Date;

public class ClientCertificateRepresentation {
    private String B;
    private String U;
    private String IPu;
    private String Kb;
    private String Ku;
    private Date exp;

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

    public String getKb() {
        return Kb;
    }

    public void setKb(String kb) {
        Kb = kb;
    }

    public String getKu() {
        return Ku;
    }

    public void setKu(String ku) {
        Ku = ku;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private String info;

}
