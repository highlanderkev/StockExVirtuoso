/*
 * TradeProcessor.java
 */
package book;

import java.util.HashMap;
import publishers.FillMessage;
import tradable.Tradable;

/**
 * This is a public interface which governs the behavior of any TradeProcessor.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public interface TradeProcessor {

    /**
     * This method will be called when it has been determined that a tradable
     * can trade.
     *
     * @param trd the tradable object
     * @return a HashMap of Fill messages
     * @throws Exception
     */
    public HashMap<String, FillMessage> doTrade(Tradable trd) throws Exception;
}
