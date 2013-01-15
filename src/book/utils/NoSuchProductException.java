/**
 * NoSuchProductException.java
 */
package book.utils;

/**
 * This is an exception that is used with Price objects and classes.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class NoSuchProductException extends Exception {

    /**
     * No such product Exception.
     *
     * @param msg
     */
    public NoSuchProductException(String msg) {
        super(msg);
    }
}
//end of file