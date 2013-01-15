/*
 * Publisher.java
 */
package publishers;

import client.User;

/**
 * This is a public interface for Publishers being used by the DePaul Stock
 * Exchange.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public interface Publisher {

    /**
     * Method used to subscribe to updates on stock movements.
     *
     * @param u
     * @param product
     * @throws Exception
     */
    void subscribe(User u, String product) throws Exception;

    /**
     * Method used to un-subscribe to updates on stock movements.
     *
     * @param u user
     * @param product symbol
     * @throws Exception
     */
    void unSubscribe(User u, String product) throws Exception;
}
//end of file