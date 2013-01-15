/**
 * MarketState.java
 */
package constants;

import constants.utils.RuntimeInputException;

/**
 * This is an enumerated class which defines whether the market is OPEN, CLOSED
 * or PREOPEN.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public enum MarketState {

    /**
     * CLOSED state of market.
     */
    CLOSED("CLOSED"),
    /**
     * OPEN state of market.
     */
    OPEN("OPEN"),
    /**
     * PREOPEN state of market.
     */
    PREOPEN("PREOPEN");
    private final String marketstate;  //string holder of state of market        

    /**
     * Construction method for enumeration of Market State.
     *
     * @param state of market
     * @throws RuntimeInputException
     */
    private MarketState(String state) throws RuntimeInputException {
        if (state.equals("OPEN") || state.equals("CLOSED") || state.equals("PREOPEN")) {
            marketstate = state;
        } else {
            throw new RuntimeInputException("RuntimeInputException: MarketState can only be OPEN, CLOSED or PREOPEN.");
        }
    }

    /**
     * Get Method for value of MarketState.
     *
     * @return String value of market state
     */
    public String getMarketState() {
        return marketstate;
    }
}
//end of file