/**
 * OrderNotFoundException.java
 */
package book.utils;

/**
 * This is an exception that is used with the DePaulStockEx project.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class OrderNotFoundException extends Exception {

    /**
     * Order Not Found Exception.
     *
     * @param msg
     */
    public OrderNotFoundException(String msg) {
        super(msg);
    }
}
//end of file
