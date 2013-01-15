/*
 * User.java
 */
package client;

import constants.BookSide;
import java.util.ArrayList;
import price.Price;
import publishers.CancelMessage;
import publishers.FillMessage;
import tradable.TradableDTO;

/**
 * This is a "User" interface which is to be implemented in any class that
 * wishes to be a "User" of the DePaul Stock Exchange Trading System.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public interface User {

    /**
     * Interface method to get users name.
     *
     * @return the String userName of this user
     */
    String getUserName();

    /**
     * Interface method to accept a stock symbol, a price object holding value
     * of last sale of stock, and volume(v) of last sale.
     *
     * @param Product stock symbol
     * @param p price object holding value of last sale of stock
     * @param v volume(v) of last sale
     */
    void acceptLastSale(String Product, Price p, int v);

    /**
     * Interface method to accept a fill message object which contains info on
     * an order or quote, like a receipt sent to user with details of a trade.
     *
     * @param fm fill message object
     */
    void acceptMessage(FillMessage fm);

    /**
     * Interface method to accept a cancel message object.
     *
     * @param cm cancel message object
     */
    void acceptMessage(CancelMessage cm);

    /**
     * Interface method to accept a string which contains market information
     * related to a stock symbol.
     *
     * @param message which contains market information related to a stock
     * symbol
     */
    void acceptMarketMessage(String message);

    /**
     * Interface method to accept a stock symbol (IBM, GOOG), a price object of
     * last trade of stock, and a character indicator whether it is a increase
     * or decrease in stock price.
     *
     * @param product stock symbol (IBM, GOOG)
     * @param p price object
     * @param direction character indicator whether it is a increase or decrease
     * in stock price
     */
    void acceptTicker(String product, Price p, char direction);

    /**
     * Interface method to accept a string stock symbol, a price object for BUY
     * side, an Integer of BUY volume, a price object for SELL side, and an
     * Integer of SELL volume, this info is used by "users" to update their
     * market display screen so that they are always looking at the most current
     * market data.
     *
     * @param product price object for BUY side
     * @param bp price object for BUY side
     * @param bv Integer of BUY volume
     * @param sp price object for SELL side
     * @param sv Integer of SELL volume
     */
    void acceptCurrentMarket(String product, Price bp, int bv, Price sp, int sv);

    /**
     * Instructs a User object to connect to the trading system.
     *
     * @throws Exception
     */
    void connect() throws Exception;

    /**
     * Instructs a User object to disconnect from the trading system.
     *
     * @throws Exception
     */
    void disConnect() throws Exception;

    /**
     * Requests the opening of the market display if the user is connected.
     *
     * @throws Exception
     */
    void showMarketDisplay() throws Exception;

    /**
     * Allows the User object to submit a new Order request.
     *
     * @param product symbol of order
     * @param price of order
     * @param volume of order
     * @param side of order
     * @return string
     * @throws Exception
     */
    String submitOrder(String product, Price price, int volume, BookSide side) throws Exception;

    /**
     * Allows the User object to submit a new Order Cancel request
     *
     * @param product
     * @param side
     * @param orderId
     * @throws Exception
     */
    void submitOrderCancel(String product, BookSide side, String orderId) throws Exception;

    /**
     * Allows the User object to submit a new Quote request
     *
     * @param product
     * @param buyPrice
     * @param buyVolume
     * @param sellPrice
     * @param sellVolume
     * @throws Exception
     */
    void submitQuote(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws Exception;

    /**
     * Allows the User object to submit a new Quote Cancel request
     *
     * @param product symbol
     * @throws Exception
     */
    void submitQuoteCancel(String product) throws Exception;

    /**
     * Allows the User object to subscribe for Current Market for the specified
     * Stock.
     *
     * @param product symbol
     * @throws Exception
     */
    void subscribeCurrentMarket(String product) throws Exception;

    /**
     * Allows the User object to subscribe for Last Sale for the specified
     * Stock.
     *
     * @param product symbol
     * @throws Exception
     */
    void subscribeLastSale(String product) throws Exception;

    /**
     * Allows the User object to subscribe for Messages for the specified Stock.
     *
     * @param product symbol
     * @throws Exception
     */
    void subscribeMessages(String product) throws Exception;

    /**
     * Allows the User object to subscribe for Ticker for the specified Stock.
     *
     * @param product symbol
     * @throws Exception
     */
    void subscribeTicker(String product) throws Exception;

    /**
     * Returns the value of the all Sock the User owns (has bought but not
     * sold).
     *
     * @return price
     * @throws Exception
     */
    Price getAllStockValue() throws Exception;

    /**
     * Returns the difference between cost of all stock purchases and stock
     * sales
     *
     * @return price
     * @throws Exception
     */
    Price getAccountCosts() throws Exception;

    /**
     * Returns the difference between current value of all stocks owned and the
     * account costs.
     *
     * @return price of current value
     * @throws Exception
     */
    Price getNetAccountValue() throws Exception;

    /**
     * Allows the User object to submit a Book Depth request for the specified
     * stock.
     *
     * @param product symbol
     * @return string array of book depth
     * @throws Exception
     */
    String[][] getBookDepth(String product) throws Exception;

    /**
     * Allows the User object to query the market state (OPEN, PREOPEN, CLOSED).
     *
     * @return string of market state.
     * @throws Exception
     */
    String getMarketState() throws Exception;

    /**
     * Returns a list of order id’s for the orders this user has submitted.
     *
     * @return ArrayList of Tradable User data
     */
    ArrayList<TradableUserData> getOrderIds();

    /**
     * Returns a list of the stock products available in the trading system.
     *
     * @return ArrayList of stock products
     */
    ArrayList<String> getProductList();

    /**
     * Returns the value of the specified stock that this user owns.
     *
     * @param sym product symbol
     * @return Price/value of specified stock
     * @throws Exception
     */
    Price getStockPositionValue(String sym) throws Exception;

    /**
     * Returns the volume of the specified stock that this user owns.
     *
     * @param product
     * @return volume of stock
     * @throws Exception
     */
    int getStockPositionVolume(String product) throws Exception;

    /**
     * Returns a list of all the Stocks the user owns.
     *
     * @return ArrayList of strings of stocks
     */
    ArrayList<String> getHoldings();

    /**
     * Gets a list of DTO’s containing information on all Orders for this user
     * for the specified product with remaining volume.
     *
     * @param product symbol
     * @return ArrayList of Tradable DTO's
     * @throws Exception
     */
    ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws Exception;
}
//end of file