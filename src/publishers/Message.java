/*
 * Message.java
 */
package publishers;

import constants.BookSide;
import price.Price;

/**
 * This is a public interface for Messages being used by the DePaul Stock
 * Exchange.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public interface Message {

    /**
     * Interface method to get the user name.
     *
     * @return a string of the username
     */
    String getUser();

    /**
     * Interface method to get the product.
     *
     * @return the product symbol of the tradable
     */
    String getProduct();

    /**
     * Interface method to get price of tradable.
     *
     * @return returns the price of the tradable
     * @throws Exception
     */
    Price getPrice() throws Exception;

    /**
     * Interface method to set new volume of message.
     *
     * @param newVolume of message
     * @throws Exception
     */
    void setVolume(int newVolume) throws Exception;

    /**
     * Interface method to get volume of tradable.
     *
     * @return the volume of the tradable
     */
    int getVolume();

    /**
     * Interface method to set detail of message.
     *
     * @param details
     * @throws Exception
     */
    void setDetails(String details) throws Exception;

    /**
     * Interface method to get the details of message.
     *
     * @return the description of the cancellation
     */
    String getDetails();

    /**
     * Interface method to get book side.
     *
     * @return the Book side of the tradable
     */
    BookSide getSide();

    /**
     * Interface method to get id.
     *
     * @return returns the Id of the tradable
     */
    String getId();
}
//end of file