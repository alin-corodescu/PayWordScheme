package ro.uaic.info.DTO;

import java.util.List;

public class CurrentPaymentsDTO {
    private List<String> lastPaywords;
    private List<Integer> usedPaywords;

    public CurrentPaymentsDTO(List<String> lastPaywords,List<Integer> usedPaywords){
        this.lastPaywords = lastPaywords;
        this.usedPaywords = usedPaywords;
    }
    public CurrentPaymentsDTO(){}

    public List<String> getLastPaywords() {
        return lastPaywords;
    }

    public List<Integer> getUsedPaywords() {
        return usedPaywords;
    }

    public void setLastPaywords(List<String> lastPaywords) {
        this.lastPaywords = lastPaywords;
    }

    public void setUsedPaywords(List<Integer> usedPaywords) {
        this.usedPaywords = usedPaywords;
    }
}
