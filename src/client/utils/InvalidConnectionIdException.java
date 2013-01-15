/**
 * InvalidConnectionIdException.java
 */
package client.utils;

/**
 * This is an exception that is used with the DePaulStockEx project.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class InvalidConnectionIdException extends Exception {

    /**
     * Invalid Connection Id Exception.
     *
     * @param msg
     */
    public InvalidConnectionIdException(String msg) {
        super(msg);
    }
}
//end of file