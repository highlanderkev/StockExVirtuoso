/**
 * ProductAlreadyExistsException.java
 */
package book.utils;

/**
 * This is an exception that is used with the DePaulStockEx project.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class ProductAlreadyExistsException extends Exception {

    /**
     * Product Already Exists Exception.
     *
     * @param msg
     */
    public ProductAlreadyExistsException(String msg) {
        super(msg);
    }
}
//end of file