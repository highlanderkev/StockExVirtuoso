/**
 * InvalidPriceOperation.java
 */
package price.utils;

/**
 * This is an exception that is used with Price objects and classes.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class InvalidPriceOperation extends Exception {

    /**
     * Invalid Price operation exception.
     *
     * @param msg
     */
    public InvalidPriceOperation(String msg) {
        super(msg);
    }
}
//end of file