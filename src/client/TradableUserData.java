/**
 * TradableUserData.java
 */
package client;

import constants.BookSide;
import exceptions.InvalidValueException;

/**
 * This class will hold selected data elements related to the Tradable objects a
 * user has submitted to the system.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class TradableUserData {

    /**
     * User name.
     */
    private String user;
    /**
     * Product symbol.
     */
    private String product;
    /**
     * Side of book.
     */
    private BookSide side;
    /**
     * Id of tradable.
     */
    private String id;

    /**
     * This is a public constructor method for the tradable user data.
     *
     * @param userName
     * @param productSymbol
     * @param sidein
     * @param orderId
     * @throws InvalidValueException
     */
    public TradableUserData(String userName, String productSymbol, BookSide sidein, String orderId) throws InvalidValueException {
        setUser(userName);
        setProduct(productSymbol);
        setSide(sidein);
        setId(orderId);
    }

    /**
     * Private Set method which sets user name.
     *
     * @param newUser name
     * @throws InvalidValueException
     */
    private void setUser(String newUser) throws InvalidValueException {
        if (newUser == null || newUser.isEmpty()) {
            throw new InvalidValueException(("InvalidValueException:") + (newUser == null ? "NULL" : "Empty") + " String passed into TradableUserData.setUser(String)");
        }
        user = newUser;
    }

    /**
     * Public Get method which returns the current user name.
     *
     * @return string of user name
     */
    public String getUser() {
        return user;
    }

    /**
     * Private Set method which sets the product symbol.
     *
     * @param productSymbol
     * @throws InvalidValueException
     */
    private void setProduct(String productSymbol) throws InvalidValueException {
        if (productSymbol == null || productSymbol.isEmpty()) {
            throw new InvalidValueException(("InvalidValueException:") + (productSymbol == null ? "NULL" : "Empty") + " String passed into TraableUserData.setProduct(String)");
        }
        product = productSymbol;
    }

    /**
     * Public Get Method which returns the current product symbol.
     *
     * @return String of product symbol
     */
    public String getProduct() {
        return product;
    }

    /**
     * Private Set method which sets the side to either BUY or SELL.
     *
     * @param book side (BUY/SELL)
     * @throws InvalidValueException
     */
    private void setSide(BookSide book) throws InvalidValueException {
        if (book.getBookSide() == null) {
            throw new InvalidValueException("InvalidValueException: NULL String passed into TradableUserData.setSide(String)");
        }
        side = book;
    }

    /**
     * Public Get method which returns the current book side (BUY/SELL).
     *
     * @return BookSide
     */
    public BookSide getSide() {
        return side;
    }

    /**
     * Private Set method for System ID.
     *
     * @param newId
     * @throws InvalidValueException
     */
    private void setId(String newId) throws InvalidValueException {
        if (newId == null || newId.isEmpty()) {
            throw new InvalidValueException(("InvalidValueException:") + (newId == null ? "NULL" : "Empty") + " String passed into TraableUserData.setId(String)");
        }
        id = newId;
    }

    /**
     * Public Get method returns current system id.
     *
     * @return String of System ID
     */
    public String getId() {
        return id;
    }

    /**
     * Public to String method.
     *
     * @return String of tradable user data
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[User %s, %s %s (%s)", user, side, product, id));
        return sb.toString();
    }
}