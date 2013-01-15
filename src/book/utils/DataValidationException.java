/**
 * DataValidationException.java
 */
package book.utils;

/**
 * This is an exception that is used with Price objects and classes.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class DataValidationException extends Exception {

    /**
     * Data Validation Exception.
     *
     * @param msg
     */
    public DataValidationException(String msg) {
        super(msg);
    }
}
//end of file