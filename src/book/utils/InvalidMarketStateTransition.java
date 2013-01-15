/**
 * InvalidMarketStateTransition.java
 */
package book.utils;

/**
 * This is an exception that is used with Price objects and classes.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class InvalidMarketStateTransition extends Exception {

    /**
     * Invalid Market State Transition Exception.
     *
     * @param msg
     */
    public InvalidMarketStateTransition(String msg) {
        super(msg);
    }
}
//end of file