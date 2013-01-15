/*
 * MarketMessage.java
 */
package publishers;

import constants.MarketState;
import publishers.utils.InvalidMarketStateException;

/**
 * This class encapsulates data related to the "state" of the market, the
 * possible states are OPEN, CLOSED and PREOPEN.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class MarketMessage {

    /**
     * Current market state.
     */
    private MarketState state;

    /**
     * Public construction method to set the state of the market.
     *
     * @param state of market
     * @throws Exception
     */
    public MarketMessage(MarketState state) throws Exception {
        setState(state);
    }

    /**
     * Public get method to get the current state of the market.
     *
     * @return state of market
     */
    public MarketState getState() {
        return state;
    }

    /**
     * Private set method to set the state of the market.
     *
     * @param state of market
     * @throws InvalidMarketStateException if invalid market state
     */
    private void setState(MarketState state) throws InvalidMarketStateException {
        if (state.equals(MarketState.OPEN) || state.equals(MarketState.CLOSED) || state.equals(MarketState.PREOPEN)) {
            this.state = state;
        } else {
            throw new InvalidMarketStateException("InvalidMarketStateException: Market State can only be set to OPEN, CLOSED or PREOPEN.");
        }
    }

    /**
     * Public to string method to return string value of state of market.
     *
     * @return Market State to String
     */
    @Override
    public String toString() {
        switch (state.getMarketState()) {
            case "OPEN":
                return ("OPEN");
            case "PREOPEN":
                return ("PREOPEN");
            default:
                return ("CLOSED");
        }
    }
}
//end of file  