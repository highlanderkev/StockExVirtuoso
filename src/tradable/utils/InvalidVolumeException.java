/**
 * InvalidVolumeException.java
 */
package tradable.utils;

/**
 * This is a tradable exception that is used with the DePaulStockEx project.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class InvalidVolumeException extends Exception {

    /**
     * Invalid Volume Exception.
     *
     * @param msg
     */
    public InvalidVolumeException(String msg) {
        super(msg);
    }
}
//end of file