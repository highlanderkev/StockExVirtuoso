/*
 * RuntimeInputException.java
 */
package constants.utils;

/**
 * This is a Runtime Exception that is used with tradable objects and
 * classes.<br> This is an UNCHECKED exception so handle with care.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class RuntimeInputException extends RuntimeException {

    /**
     * Run time Input Exception.<br> HANDLE WITH CARE.
     *
     * @param msg
     */
    public RuntimeInputException(String msg) {
        super(msg);
    }
}
//end of file