/*
 * Messenger.java
 */
package publishers;

import constants.BookSide;
import exceptions.ExceptionHandler;
import price.Price;
import price.PriceFactory;

/**
 * This is a parent class for any Message class which requires the same
 * implementation and functionality.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class Messenger implements Message {

    /**
     * String of UserName of user, cant be null or empty.
     */
    private String user;
    /**
     * String of Stock symbol, cant be null or empty.
     */
    private String product;
    /**
     * The Price, cannot be null.
     */
    private Price price;
    /**
     * The Volume, cant be negative.
     */
    private int volume;
    /**
     * Description of the cancellation, cant be null.
     */
    private String details;
    /**
     * The Side (BUY/SELL), must be valid side.
     */
    private BookSide side;
    /**
     * String System Id, cant be null.
     */
    public String id;

    /**
     * Protected Construction method to create a Messenger message object.
     *
     * @param userName name of user
     * @param productSymbol
     * @param p price
     * @param newVolume
     * @param details of message
     * @param s book-side BUY/SELL
     * @param newId string identifier
     * @throws Exception
     */
    protected Messenger(String userName, String productSymbol, Price p, int newVolume, String details, BookSide s, String newId) throws Exception {
        setUser(userName);
        setProduct(productSymbol);
        setPrice(p);
        setVolume(newVolume);
        setDetails(details);
        setSide(s);
        setId(newId);
    }

    /**
     * Private set method to set user name of message.
     *
     * @param userName of user
     * @throws Exception
     */
    private void setUser(String userName) throws Exception {
        if (ExceptionHandler.checkString(userName, "publishers.Messenger#setUser.")) {
            user = userName;
        }
    }

    /**
     * Public get method to get string of user.
     *
     * @return user name as string
     */
    @Override
    public String getUser() {
        return user;
    }

    /**
     * Private set method to set product symbol of related message.
     *
     * @param productSymbol
     * @throws Exception
     */
    private void setProduct(String productSymbol) throws Exception {
        if (ExceptionHandler.checkString(productSymbol, "publishers.Messenger#setProduct.")) {
            product = productSymbol;
        }
    }

    /**
     * Public get method to get product symbol.
     *
     * @return string of product symbol
     */
    @Override
    public String getProduct() {
        return product;
    }

    /**
     * Private set method to set price of related message.
     *
     * @param p price object
     * @throws Exception
     */
    private void setPrice(Price p) throws Exception {
        if (ExceptionHandler.checkObject(p, "publishers.Messenger#setPrice.")) {
            price = p;
        }
    }

    /**
     * Public get method to get price related to message.
     *
     * @return Price object
     * @throws Exception
     */
    @Override
    public Price getPrice() throws Exception {
        if (price.isMarket() == true) {
            Price currentPrice = PriceFactory.makeMarketPrice();
            return currentPrice;
        } else {
            Price currentPrice = PriceFactory.makeLimitPrice(price.toString());
            return currentPrice;
        }
    }

    /**
     * Protected method to string value of price value.
     *
     * @return string of price value
     */
    protected String getPriceString() {
        String priceValue = price.toString();
        return priceValue;
    }

    /**
     * Public set method to set volume related to message.
     *
     * @param newVolume
     * @throws Exception
     */
    @Override
    public void setVolume(int newVolume) throws Exception {
        if (ExceptionHandler.checkIntNegative(newVolume, "publishers.Messenger#setVolume.")) {
            volume = newVolume;
        }
    }

    /**
     * Public get method to get Volume related to message.
     *
     * @return volume integer
     */
    @Override
    public int getVolume() {
        return volume;
    }

    /**
     * Public set method to set details about message.
     *
     * @param details of message
     * @throws Exception
     */
    @Override
    public void setDetails(String details) throws Exception {
        if (ExceptionHandler.checkString(details, "publishers.Messenger#setDetails.")) {
            this.details = details;
        }
    }

    /**
     * Public get method to get details of message.
     *
     * @return String of details about message
     */
    @Override
    public String getDetails() {
        return details;
    }

    /**
     * Private Set method which sets the side to either BUY or SELL.
     *
     * @param book side of book (BUY/SELL)
     * @throws Exception
     */
    private void setSide(BookSide bookSide) throws Exception {
        if (ExceptionHandler.checkObject(bookSide, "publishers.Messenger#setSide.")) {
            side = bookSide;
        }
    }

    /**
     * Public Get method which returns the current book side (BUY/SELL).
     *
     * @return BookSide of current order object
     */
    @Override
    public BookSide getSide() {
        return side;
    }

    /**
     * Private Set method for system ID.
     *
     * @param systemId
     * @throws Exception
     */
    private void setId(String systemId) throws Exception {
        if (ExceptionHandler.checkString(systemId, "publishers.Messenger#setId.")) {
            id = systemId;
        }
    }

    /**
     * Public get method returns system id to caller.
     *
     * @return String of system ID
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Public to string method to return string of message details.
     *
     * @return string of message details
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("User: %s, Product: %s, Price: %s,", user, product, price));
        sb.append(String.format(" Volume: %s, Details: %s, Side: %s, Id: %s", volume, details, side, id));
        return sb.toString();
    }
}
//end of file