/**
 * InvalidValueException.java
 */
package exceptions;

/**
 * This is a Global exception that is used with the DePaulStockEx project.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class InvalidValueException extends Exception {

    /**
     * Invalid Value Exception to be used globally by all classes.
     *
     * @param msg
     * @see exceptions.ExceptionHandler
     */
    public InvalidValueException(String msg) {
        super(msg);
    }
}
//end of file
