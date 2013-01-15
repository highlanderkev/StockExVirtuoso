/*
 * TradeProcessorFactory.java
 */
package book;

import exceptions.ExceptionHandler;

/**
 * This is a public class which implements the factory design pattern and
 * creates TradeProcessors for the trading system.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class TradeProcessorFactory {

    /**
     * Public method to make and return a trade processor for the product book.
     *
     * @param productBook reference
     * @return TradeProcessor
     * @throws Exception
     */
    public static TradeProcessor makeTradeProcessor(ProductBookSide productBook) throws Exception {
        if (ExceptionHandler.checkObject(productBook, "book.TradeProcessorFactory#makeTradeProcessor.")) {
            TradeProcessor trader = new TradeProcessorPriceTimeImpl(productBook);
            return trader;
        } else {
            return null;
        }
    }
}
//end of file