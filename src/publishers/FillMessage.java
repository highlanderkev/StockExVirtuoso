/*
 * FillMessage.java
 */
package publishers;

import constants.BookSide;
import price.Price;

/**
 * This class encapsulates data related to the fill (trade) of an order or
 * quote-side.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class FillMessage extends Messenger implements Comparable<FillMessage>, Message {

    /**
     * Public Construction method to create a new Fill Message.
     *
     * @param userName of user
     * @param productSymbol of stock
     * @param p price object
     * @param newVolume
     * @param details fill details
     * @param s side of book
     * @param newId of order/quote
     * @throws Exception
     */
    public FillMessage(String userName, String productSymbol, Price p, int newVolume, String details, BookSide s, String newId) throws Exception {
        super(userName, productSymbol, p, newVolume, details, s, newId);
    }

    /**
     * Public compare To method which compares the prices of two FillMessages.
     *
     * @param fm FillMessage passed in
     * @return integer from call to compareTo on price, 0 if equal, -1 if
     * exception or not equal
     */
    @Override
    public int compareTo(FillMessage fm) {
        try {
            Price currentPrice = getPrice();
            Price comparePrice = fm.getPrice();
            return currentPrice.compareTo(comparePrice);
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            return -1;
        }
    }

    /**
     * Public To string method which returns a string of the values of this
     * message.
     *
     * @return String of values of the FillMessage
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("User: %s, Product: %s, Fill Price: %s,", getUser(), getProduct(), getPriceString()));
        sb.append(String.format(" Fill Volume: %s, Details: %s, Side: %s", getVolume(), getDetails(), getSide()));
        return sb.toString();
    }
}
//end of file