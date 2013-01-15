/**
 * Tradable.java
 */
package tradable;

import constants.BookSide;
import price.Price;

/**
 * This is an interface for common generic type of entities representing a BUY
 * or SELL request that is traded in the DePaul Stock Exchange System.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public interface Tradable {

    /**
     * Get method returns the product symbol (IBM, GOOG).
     *
     * @return product symbol
     */
    String getProduct();

    /**
     * Get method returns the price of the tradable.
     *
     * @return price object
     * @throws Exception
     */
    Price getPrice() throws Exception;

    /**
     * Get method returns the original volume (quantity) of the tradable.
     *
     * @return volume of tradable
     */
    int getOriginalVolume();

    /**
     * Get method returns the remaining volume of the tradable.
     *
     * @return remaining volume
     */
    int getRemainingVolume();

    /**
     * Get method returns the canceled volume of the tradable.
     *
     * @return canceled volume
     */
    int getCancelledVolume();

    /**
     * Set method sets the tradable canceled quantity to the value passed in.
     *
     * @param newCancelledVolume
     * @throws Exception
     */
    void setCancelledVolume(int newCancelledVolume) throws Exception;

    /**
     * Set method sets the tradable remaining quantity to the value passed in.
     *
     * @param newRemainingVolume
     * @throws Exception
     */
    void setRemainingVolume(int newRemainingVolume) throws Exception;

    /**
     * Get method returns the user id associated with the tradable.
     *
     * @return user id
     */
    String getUser();

    /**
     * Get method returns the "side" (BUY/SELL) of the tradable.
     *
     * @return side of book
     */
    BookSide getSide();

    /**
     * Is quote method returns true if the tradable is part of a quote, returns
     * false if not.
     *
     * @return boolean if quote
     */
    boolean isQuote();

    /**
     * Get method returns the tradable "id" or system id.
     *
     * @return id of tradable
     */
    String getId();
}
//end of file