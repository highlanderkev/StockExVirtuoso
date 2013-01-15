/*
 * Trader.java
 */
package tradable;

import constants.BookSide;
import exceptions.ExceptionHandler;
import exceptions.InvalidValueException;
import price.Price;
import price.PriceFactory;
import tradable.utils.InvalidVolumeException;

/**
 * This is a parent class for any class which implements Tradable and requires
 * the same functionality and behavior.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class Trader implements Tradable {

    /**
     * product symbol (IBM, GOOG)
     */
    private String product;
    /**
     * Price of tradable
     */
    private Price price;
    /**
     * original volume of tradable
     */
    private int originalVolume;
    /**
     * remaining volume of tradable
     */
    private int remainingVolume;
    /**
     * canceled volume of tradable
     */
    private int cancelledVolume;
    /**
     * userName of user
     */
    private String user;
    /**
     * the side (BUY/SELL)
     */
    private BookSide side;
    /**
     * true if tradable is a quote
     */
    private boolean isQuote;
    /**
     * System ID
     */
    private String id;

    /**
     * Protected Construction method which creates a Tradable object.
     *
     * @param userName name of user
     * @param productSymbol (IBM, GOOG)
     * @param orderPrice price of order
     * @param originalVolume size of starting volume
     * @param side BUY or SELL
     * @param isQT whether it is a quote-side tradable
     * @throws Exception
     */
    protected Trader(String userName, String productSymbol, Price orderPrice, int originalVolume, BookSide side, boolean isQT) throws Exception {
        setId(orderPrice, userName, productSymbol);
        setPrice(orderPrice);
        setUser(userName);
        setProduct(productSymbol);
        setOriginalVolume(originalVolume);
        setRemainingVolume(originalVolume);
        setCancelledVolume();
        setSide(side);
        setQuote(isQT);
    }

    /**
     * Private Set method which sets the product symbol of the tradable object.
     *
     * @param productSymbol
     * @throws Exception
     */
    private void setProduct(String productSymbol) throws Exception {
        if (ExceptionHandler.checkString(productSymbol, "tradable.Trader#setProduct.")) {
            product = productSymbol;
        }
    }

    /**
     * Public Get Method which returns the current product symbol.
     *
     * @return String of product symbol
     */
    @Override
    public String getProduct() {
        return product;
    }

    /**
     * Private Set Method which sets the price on construction of tradable
     * object.
     *
     * @param orderPrice
     * @throws Exception
     */
    private void setPrice(Price orderPrice) throws InvalidValueException {
        if (ExceptionHandler.checkObject(orderPrice, "tradable.Trader#setPrice.")) {
            price = orderPrice;
        }
    }

    /**
     * Public Get method which returns price of current tradable object.
     *
     * @return Price of current order object
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
     * Private set method to set original volume to the volume passed in.
     *
     * @param originalVol
     * @throws Exception
     */
    private void setOriginalVolume(int originalVol) throws Exception {
        if (ExceptionHandler.checkIntNegative(originalVol, "tradable.Trader#setOriginalVolume.")
                && ExceptionHandler.checkIntZero(originalVol, "tradable.Trader#setOriginalVolume.")) {
            originalVolume = originalVol;
        }
    }

    /**
     * Public get method to get the original volume of tradable.
     *
     * @return integer of original volume
     */
    @Override
    public int getOriginalVolume() {
        return originalVolume;
    }

    /**
     * Public Set method to be used to set a new remaining volume of tradable.
     *
     * @param newRemainingVolume of order
     * @throws InvalidVolumeException if remaining volume is greater than
     * original volume
     */
    @Override
    public void setRemainingVolume(int newRemainingVolume) throws Exception, InvalidVolumeException {
        if (ExceptionHandler.checkIntNegative(newRemainingVolume, "tradable.Trader#setRemainingVolume.")) {
            if (newRemainingVolume > originalVolume) {
                throw new InvalidVolumeException("InvalidVolumeException: Remaining Quantity is greater than original volume, passed to tradable.Trader#setRemainingVolume.");
            } else {
                remainingVolume = newRemainingVolume;
            }
        }
    }

    /**
     * Public Get method to return current remaining volume of tradable.
     *
     * @return integer of current remaining volume
     */
    @Override
    public int getRemainingVolume() {
        return remainingVolume;
    }

    /**
     * Overloaded Set method only to be used for construction of tradable
     * object.<br> This method is private and initializes the canceled volume to
     * 0.
     */
    private void setCancelledVolume() {
        cancelledVolume = 0;
    }

    /**
     * Public Set method to set a new canceled volume.
     *
     * @throws InvalidVolumeException canceled volume is greater than original
     * volume
     */
    @Override
    public void setCancelledVolume(int newCancelledVolume) throws InvalidVolumeException, Exception {
        if (ExceptionHandler.checkIntNegative(newCancelledVolume, "tradable.Trader#setCancelledVolume.")) {
            if (newCancelledVolume > originalVolume) {
                throw new InvalidVolumeException("InvalidVolumeException: Cancelled volume is greater than original volume, passed to tradable.Trader#setCancelledVolume.");
            } else {
                cancelledVolume = newCancelledVolume;
            }
        }
    }

    /**
     * Public Get method to get the current canceled volume.
     *
     * @return integer of canceled volume
     */
    @Override
    public int getCancelledVolume() {
        return cancelledVolume;
    }

    /**
     * Private Set method which sets user name of tradable.
     *
     * @param newUser
     * @throws Exception
     */
    private void setUser(String newUser) throws Exception {
        if (ExceptionHandler.checkString(newUser, "tradable.trader#setUser.")) {
            user = newUser;
        }
    }

    /**
     * Public Get method which returns the current user name.
     *
     * @return string of user name
     */
    @Override
    public String getUser() {
        return user;
    }

    /**
     * Private Set method which sets the side to either BUY or SELL.
     *
     * @param book side (BUY/SELL)
     * @throws Exception
     */
    private void setSide(BookSide book) throws Exception {
        if (ExceptionHandler.checkObject(book, "tradable.Trader#setSide.")) {
            side = book;
        }
    }

    /**
     * Public Get method which returns the current book side (BUY/SELL).
     *
     * @return BookSide
     */
    @Override
    public BookSide getSide() {
        return side;
    }

    /**
     * Private Set method to set (isQuote) boolean.
     *
     * @param setQT it is a Quote object
     */
    private void setQuote(boolean isQT) {
        isQuote = isQT;
    }

    /**
     * Public Get method returns whether the current object is a quote or not.
     *
     * @return boolean if it is a quote object
     */
    @Override
    public boolean isQuote() {
        return isQuote;
    }

    /**
     * Private Set method for System ID.
     *
     * @param orderPrice
     * @param userName
     * @param productSymbol
     * @throws Exception
     */
    private void setId(Price orderPrice, String userName, String productSymbol) throws Exception {
        long currentTime = System.nanoTime();
        String thisClass = "tradable.Trader#setID.";
        if (ExceptionHandler.checkString(userName, thisClass) && ExceptionHandler.checkString(productSymbol, thisClass)
                && ExceptionHandler.checkObject(orderPrice, thisClass)) {
            String orderValue = orderPrice.toString();
            String newId = (userName + productSymbol + orderValue + currentTime);
            id = newId;
        }
    }

    /**
     * Public Get method returns current system id of this tradable.
     *
     * @return String of System ID
     */
    @Override
    public String getId() {
        String thisId = id;
        return thisId;
    }

    /**
     * Public To String method to return a string representation of the Tradable
     * values.
     *
     * @return String of order values
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s order: %s %s %s at %s", user, side, remainingVolume, product, price.toString()));
        sb.append(String.format(" (Original Vol: %s, CXL'd Vol: %s), ID: %s", originalVolume, cancelledVolume, id));
        return sb.toString();
    }
}
//end of file