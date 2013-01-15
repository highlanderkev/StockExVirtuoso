/**
 * Order.java
 */
package tradable;

import constants.BookSide;
import price.Price;

/**
 * This class represents a request from a user to BUY or SELL.<br> It extends
 * the Trader class and implements the Tradable interface.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class Order extends Trader implements Tradable {

    /**
     * Construction method which creates an order object.
     *
     * @param userName name of user
     * @param productSymbol (IBM, GOOG)
     * @param orderPrice price of order
     * @param originalVolume size of starting volume
     * @param side BUY or SELL
     * @throws Exception
     */
    public Order(String userName, String productSymbol, Price orderPrice, int originalVolume, BookSide side) throws Exception {
        super(userName, productSymbol, orderPrice, originalVolume, side, false);
    }
}
//end of file