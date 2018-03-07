package ro.uaic.info.broker;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the entire account database in memory
 */
public class MemoryAccountDatabase implements AccountDatabase {
    Map<String, Long> balances = new HashMap<>();


    public MemoryAccountDatabase() {
        balances.put("1", 100L);
        balances.put("2", 100L);
    }
    @Override
    public Long getBalance(String accountId) {
        return balances.getOrDefault(accountId, (long) 0);
    }

    @Override
    public void adjustBalance(String accountId, Long value) {
//        Add the value to the account
        balances.put(accountId, balances.get(accountId) + value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> entry : balances.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue());
        }
        return sb.toString();
    }
}
