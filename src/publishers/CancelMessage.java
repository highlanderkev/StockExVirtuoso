/*
 * CancelMessage.java
 */
package publishers;

import constants.BookSide;
import price.Price;

/**
 * This class encapsulates data related to the cancellation of an order or
 * quote-side by a user, or by the trading system.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class CancelMessage extends Messenger implements Comparable<CancelMessage>, Message {

    /**
     * Public Construction method to create a new Cancel Message for users.
     *
     * @param userName associated with cancel message
     * @param productSymbol
     * @param p price object
     * @param newVolume
     * @param cancelDetails details of cancel message
     * @param s side of book BUY/SELL
     * @param newId id of cancel order/quote
     * @throws Exception
     */
    public CancelMessage(String userName, String productSymbol, Price p, int newVolume, String cancelDetails, BookSide s, String newId) throws Exception {
        super(userName, productSymbol, p, newVolume, cancelDetails, s, newId);
    }

    /**
     * Public compare To method which compares the prices of two CancelMessages.
     *
     * @param cm CancelMessage passed in
     * @return integer from call to compareTo on price, 0 for success, -1 for
     * exception or failure
     */
    @Override
    public int compareTo(CancelMessage cm) {
        try {
            Price currentPrice = getPrice();
            Price comparePrice = cm.getPrice();
            return currentPrice.compareTo(comparePrice);
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            return -1;
        }
    }
}
//end of file