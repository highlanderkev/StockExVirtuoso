/**
 * UserNotConnectedException.java
 */
package client.utils;

/**
 * This is an exception that is used with the DePaulStockEx project.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class UserNotConnectedException extends Exception {

    /**
     * User Not Connected Exception.
     *
     * @param msg
     */
    public UserNotConnectedException(String msg) {
        super(msg);
    }
}
//end of file