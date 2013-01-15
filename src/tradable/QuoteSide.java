/**
 * QuoteSide.java
 */
package tradable;

import constants.BookSide;
import price.Price;

/**
 * This class represents the price and volume of one side (BUY or SELL) of a
 * quote.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class QuoteSide extends Trader implements Tradable {

    /**
     * Public Construction method which creates an Quote-side object.
     *
     * @param userName name of user
     * @param productSymbol (IBM, GOOG)
     * @param sidePrice price of order
     * @param originalVolume size of starting volume
     * @param side BUY or SELL
     * @throws Exception
     */
    public QuoteSide(String userName, String productSymbol, Price sidePrice, int originalVolume, BookSide side) throws Exception {
        super(userName, productSymbol, sidePrice, originalVolume, side, true);
    }

    /**
     * Public Copy Construction method which copies the values of one quote side
     * object into a new quote side object.
     *
     * @param qs Quote side object to be copied
     * @throws Exception
     */
    public QuoteSide(QuoteSide qs) throws Exception {
        super(qs.getUser(), qs.getProduct(), qs.getPrice(), qs.getOriginalVolume(), qs.getSide(), qs.isQuote());
    }

    /**
     * Public To String method which returns a string of Quote values.
     *
     * @return String of quote side values
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s x %s", getPriceString(), getRemainingVolume()));
        sb.append(String.format(" (Original Vol: %s, CXL'd Vol: %s) [%s]", getOriginalVolume(), getCancelledVolume(), getId()));
        return sb.toString();
    }
}
//end of file