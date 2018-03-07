package ro.uaic.info.client;

import ro.uaic.info.crypto.Commitment;
import ro.uaic.info.crypto.HashChain;

import java.util.List;

/**
 * Holds the logic of buying a product from a vendor
 */
public class ProductConsumer {

    /**
     *  This class assumes that there are enough Ci's in the hashchain to complete the
     *  transaction
     * @param vendorPort
     * @param productID
     */
    public ProductConsumer(int vendorPort, String productID, List<HashChain> hashChain){
        
    }
}
