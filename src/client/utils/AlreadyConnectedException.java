/**
 * AlreadyConnectedException.java
 */
package client.utils;

/**
 * This is an exception that is used with Price objects and classes.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class AlreadyConnectedException extends Exception {

    /**
     * Already Connected Exception.
     *
     * @param msg
     */
    public AlreadyConnectedException(String msg) {
        super(msg);
    }
}
//end of file