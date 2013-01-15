/**
 * InvalidSubscriptionException.java
 */
package publishers.utils;

/**
 * This is an exception that is used with the publishers in the DePaulStockEx
 * project.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class InvalidSubscriptionException extends Exception {

    /**
     * Invalid Subscription Exception.
     *
     * @param msg
     */
    public InvalidSubscriptionException(String msg) {
        super(msg);
    }
}
//end of file
