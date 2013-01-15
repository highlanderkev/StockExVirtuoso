/**
 * InvalidMarketStateException.java
 */
package book.utils;

/**
 * This is an exception that is used with Price objects and classes.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class InvalidMarketStateException extends Exception {

    /**
     * Invalid Market State Exception.
     *
     * @param msg
     */
    public InvalidMarketStateException(String msg) {
        super(msg);
    }
}
//end of file