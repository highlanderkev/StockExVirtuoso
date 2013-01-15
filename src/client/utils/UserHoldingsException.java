/**
 * UserHoldingsException.java
 */
package client.utils;

/**
 * This is an exception that is used with the DePaulStockEx project.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class UserHoldingsException extends Exception {

    /**
     * User Holding Exception.
     *
     * @param msg
     */
    public UserHoldingsException(String msg) {
        super(msg);
    }
}
//end of file