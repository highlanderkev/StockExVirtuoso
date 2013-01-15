/**
 * BookSide.java
 */
package constants;

import constants.utils.RuntimeInputException;

/**
 * This is an enumerated class which defines whether it is a BUY side or SELL
 * side.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public enum BookSide {

    /**
     * BUY side of market.
     */
    BUY("BUY"),
    /**
     * SELL side of market.
     */
    SELL("SELL");
    private final String bookside;  //side of book        

    /**
     * Construction method for enumeration of Book side.
     *
     * @param side
     * @throws RuntimeInputException
     */
    private BookSide(String side) throws RuntimeInputException {
        if (side.equals("BUY") || side.equals("SELL")) {
            bookside = side;
        } else {
            throw new RuntimeInputException("RuntimeInputException: MarketState can only be OPEN, CLOSED or PREOPEN.");
        }
    }

    /**
     * Get Method for value of BookSide.
     *
     * @return String value of book side
     */
    public String getBookSide() {
        return bookside;
    }
}
//end of file
