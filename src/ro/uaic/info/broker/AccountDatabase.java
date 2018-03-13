package ro.uaic.info.broker;

/**
 * Created by alin on 3/7/18
 * TODO implementations of this interface MUST allow concurrent modifications
 */
public interface AccountDatabase {

    /**
     *
     * @param accountId - identifier of the account to get balance for
     * @return the balance of the account associated with the account id specified as parameter
     */
    Long getBalance(String accountId);


    /**
     * adjusts the balance of the specified account by a value
     * @param accountId identifier of the account to adjust balance for
     * @param value value to adjust the balance with, can be either positive or negative
     */
    void adjustBalance(String accountId, Long value);
}
