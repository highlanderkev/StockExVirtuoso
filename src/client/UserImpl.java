/**
 * UserImpl.java
 */
package client;

import client.utils.UserNotConnectedException;
import constants.BookSide;
import exceptions.ExceptionHandler;
import gui.UserDisplayManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import price.Price;
import publishers.CancelMessage;
import publishers.FillMessage;
import tradable.TradableDTO;

/**
 * This class represents a real user in the trading system.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version1.0
 */
public class UserImpl implements User {

    /**
     * String of User name.
     */
    private String user;
    /**
     * Connection Id provided by system upon connection.
     */
    private long connectionId;
    /**
     * List of currently available stocks.
     */
    private ArrayList<String> stocks = new ArrayList();
    /**
     * List containing all orders for this user.
     */
    private ArrayList<TradableUserData> allOrders = new ArrayList();
    /**
     * Position Object which holds values of users stocks, costs, etc.
     */
    private Position userPosition;
    /**
     * Reference to the facade for the user interaction.
     */
    private UserDisplayManager myManager;

    /**
     * Public Constructor method.
     *
     * @param userName
     * @throws Exception
     */
    public UserImpl(String userName) throws Exception {
        setUserName(userName);
        userPosition = new Position();
    }

    /**
     * Private set method to set the user name.
     *
     * @param userName
     * @throws Exception
     */
    private void setUserName(String userName) throws Exception {
        if (ExceptionHandler.checkString(userName, "client.UserImpl#setUserName.")) {
            user = userName;
        }
    }

    /**
     * Public get method to return user name.
     *
     * @return user name
     */
    @Override
    public String getUserName() {
        return user;
    }

    /**
     * This method will call the user's display manager and position to update
     * the last sale.
     *
     * @param Product
     * @param p
     * @param v
     */
    @Override
    public void acceptLastSale(String Product, Price p, int v) {
        try {
            myManager.updateLastSale(Product, p, v);
            userPosition.updateLastSale(Product, p);
        } catch (Exception ex) {
            System.out.println("Caught error: " + ex.getMessage());
        }
    }

    /**
     * This method will display the Fill Message in the market display and will
     * forward the data to the Position object.
     *
     * @param fm fill message
     */
    @Override
    public void acceptMessage(FillMessage fm) {
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("{%s} Fill Message: %s %s %s at %s", time.toString(), fm.getSide(), fm.getVolume(), fm.getProduct(), fm.getPrice().toString()));
            sb.append(String.format(" %s [Tradable Id: %s]", fm.getDetails(), fm.getId()));
            String fillSummary = sb.toString();
            myManager.updateMarketActivity(fillSummary);
            userPosition.updatePosition(fm.getProduct(), fm.getPrice(), fm.getSide(), fm.getVolume());
        } catch (Exception ex) {
            System.out.println("Caught error: " + ex.getMessage());
        }
    }

    /**
     * This method will display the cancel message in the market display.
     *
     * @param cm cancel message
     */
    @Override
    public void acceptMessage(CancelMessage cm) {
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("{%s} Cancel Message: %s %s %s at %s", time.toString(), cm.getSide(), cm.getVolume(), cm.getProduct(), cm.getPrice().toString()));
            sb.append(String.format(" %s [Tradable Id: %s]", cm.getDetails(), cm.getId()));
            String cancelSummary = sb.toString();
            myManager.updateMarketActivity(cancelSummary);
        } catch (Exception ex) {
            System.out.println("Caught error: " + ex.getMessage());
        }
    }

    /**
     * This method will display the market message in the market display.
     *
     * @param message
     */
    @Override
    public void acceptMarketMessage(String message) {
        try {
            myManager.updateMarketState(message);
        } catch (Exception ex) {
            System.out.println("Caught error: " + ex.getMessage());
        }
    }

    /**
     * This method will display the ticker data in the market display.
     *
     * @param product symbol
     * @param p price
     * @param direction of movement
     */
    @Override
    public void acceptTicker(String product, Price p, char direction) {
        try {
            myManager.updateTicker(product, p, direction);
        } catch (Exception ex) {
            System.out.println("Caught error: " + ex.getMessage());
        }
    }

    /**
     * This method will display the current market data in the market display.
     *
     * @param product symbol
     * @param bp buy price
     * @param bv buy volume
     * @param sp sell price
     * @param sv sell volume
     */
    @Override
    public void acceptCurrentMarket(String product, Price bp, int bv, Price sp, int sv) {
        try {
            myManager.updateMarketData(product, bp, bv, sp, sv);
        } catch (Exception ex) {
            System.out.println("Caught error: " + ex.getMessage());
        }
    }

    /**
     * This method will connect the user to the trading system.
     */
    @Override
    public void connect() throws Exception {
        connectionId = UserCommandService.getInstance().connect(this);
        stocks = UserCommandService.getInstance().getProducts(user, connectionId);
    }

    /**
     * This method will disconnect a user from the trading system.
     */
    @Override
    public void disConnect() throws Exception {
        UserCommandService.getInstance().disConnect(user, connectionId);
    }

    /**
     * This method will setup the market display manager.
     *
     * @throws UserNotConnectedException
     */
    @Override
    @SuppressWarnings("empty-statement")
    public void showMarketDisplay() throws UserNotConnectedException {
        if (stocks == null) {
            throw new UserNotConnectedException("UserNotConnectedException: This user is not connected.");
        } else {
            if (myManager == null) {
                myManager = new UserDisplayManager(this);
            }
            myManager.showMarketDisplay();;
        }
    }

    /**
     * This method will submit an order to the system.
     *
     * @param product
     * @param price
     * @param volume
     * @param side
     * @return string of order id
     * @throws Exception
     */
    @Override
    public String submitOrder(String product, Price price, int volume, BookSide side) throws Exception {
        String thisClass = "client.UserImpl#submitOrder.";
        if (ExceptionHandler.checkString(product, thisClass) && ExceptionHandler.checkObject(price, thisClass)
                && ExceptionHandler.checkIntNegative(volume, thisClass) && ExceptionHandler.checkIntZero(volume, thisClass)
                && ExceptionHandler.checkObject(side, thisClass)) {
            String orderId = UserCommandService.getInstance().submitOrder(user, connectionId, product, price, volume, side);
            TradableUserData userTrade = new TradableUserData(user, product, side, orderId);
            allOrders.add(userTrade);
            return orderId;
        } else {
            return ("Invalid Order");
        }
    }

    /**
     * This method will submit a cancel order to the system.
     *
     * @param product
     * @param side
     * @param orderId
     * @throws Exception
     */
    @Override
    public void submitOrderCancel(String product, BookSide side, String orderId) throws Exception {
        String thisClass = "client.UserImpl#submitOrderCancel.";
        if (ExceptionHandler.checkString(product, thisClass) && ExceptionHandler.checkObject(side, thisClass)
                && ExceptionHandler.checkString(orderId, thisClass)) {
            UserCommandService.getInstance().submitOrderCancel(user, connectionId, product, side, orderId);
        }
    }

    /**
     * This method will a submit a quote to the system.
     *
     * @param product
     * @param buyPrice
     * @param buyVolume
     * @param sellPrice
     * @param sellVolume
     * @throws Exception
     */
    @Override
    public void submitQuote(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws Exception {
        String thisClass = "client.UserImpl#submitQuote.";
        if (ExceptionHandler.checkString(product, thisClass) && ExceptionHandler.checkObject(buyPrice, thisClass)
                && ExceptionHandler.checkIntNegative(buyVolume, thisClass) && ExceptionHandler.checkIntNegative(sellVolume, thisClass)
                && ExceptionHandler.checkObject(sellPrice, thisClass)) {
            UserCommandService.getInstance().submitQuote(user, connectionId, product, buyPrice, buyVolume, sellPrice, sellVolume);
        }
    }

    /**
     * This method will submit a cancel quote to the system.
     *
     * @param product
     * @throws Exception
     */
    @Override
    public void submitQuoteCancel(String product) throws Exception {
        if (ExceptionHandler.checkString(product, "client.UserImpl#submitQuoteCancel.")) {
            UserCommandService.getInstance().submitQuoteCancel(user, connectionId, product);
        }
    }

    /**
     * This method will subscribe to the current market.
     *
     * @param product
     * @throws Exception
     */
    @Override
    public void subscribeCurrentMarket(String product) throws Exception {
        if (ExceptionHandler.checkString(product, "client.UserImpl#subscribeCurrentMarket.")) {
            UserCommandService.getInstance().subscribeCurrentMarket(user, connectionId, product);
        }
    }

    /**
     * This method will subscribe to the last sale.
     *
     * @param product
     * @throws Exception
     */
    @Override
    public void subscribeLastSale(String product) throws Exception {
        if (ExceptionHandler.checkString(product, "client.UserImpl#subscribeLastSale.")) {
            UserCommandService.getInstance().subscribeLastSale(user, connectionId, product);
        }
    }

    /**
     * This method will subscribe to messages.
     *
     * @param product
     * @throws Exception
     */
    @Override
    public void subscribeMessages(String product) throws Exception {
        if (ExceptionHandler.checkString(product, "client.UserImpl#subscribeMessages.")) {
            UserCommandService.getInstance().subscribeMessages(user, connectionId, product);
        }
    }

    /**
     * This method will subscribe to the ticker.
     *
     * @param product
     * @throws Exception
     */
    @Override
    public void subscribeTicker(String product) throws Exception {
        if (ExceptionHandler.checkString(product, "client.UserImpl#subscribeTicker.")) {
            UserCommandService.getInstance().subscribeTicker(user, connectionId, product);
        }
    }

    /**
     * This method will return a call to the user position getAllStockValue.
     *
     * @return Price object
     * @throws Exception
     */
    @Override
    public Price getAllStockValue() throws Exception {
        Price p = userPosition.getAllStockValue();
        return p;
    }

    /**
     * This method will return a call to the user position getAccountCosts.
     *
     * @return price object
     * @throws Exception
     */
    @Override
    public Price getAccountCosts() throws Exception {
        Price p = userPosition.getAccountCosts();
        return p;
    }

    /**
     * This method will return a call to user position getNetAccountValue.
     *
     * @return Price object
     * @throws Exception
     */
    @Override
    public Price getNetAccountValue() throws Exception {
        Price p = userPosition.getNetAccountValue();
        return p;
    }

    /**
     * This method will return the book depth.
     *
     * @param product
     * @return 2 dimensional string array of book depth
     * @throws Exception
     */
    @Override
    public String[][] getBookDepth(String product) throws Exception {
        if (ExceptionHandler.checkString(product, "client.UserImpl#getBookDepth.")) {
            String[][] bookDepth = UserCommandService.getInstance().getBookDepth(user, connectionId, product);
            return bookDepth;
        } else {
            return null;
        }
    }

    /**
     * This method will return the market state.
     *
     * @return market state
     * @throws Exception
     */
    @Override
    public String getMarketState() throws Exception {
        String marketState = UserCommandService.getInstance().getMarketState(user, connectionId);
        return marketState;
    }

    /**
     * This method will return an array list of all orders.
     *
     * @return array list of all orders
     */
    @Override
    public ArrayList<TradableUserData> getOrderIds() {
        ArrayList<TradableUserData> currentOrders = allOrders;
        return currentOrders;
    }

    /**
     * This method will return the stocks array list.
     *
     * @return stocks array list
     */
    @Override
    public ArrayList<String> getProductList() {
        ArrayList<String> currentStocks = stocks;
        return currentStocks;
    }

    /**
     * This method will return a call to user position getStockPositionValue.
     *
     * @param sym
     * @return Price of stock position value
     * @throws Exception
     */
    @Override
    public Price getStockPositionValue(String sym) throws Exception {
        if (ExceptionHandler.checkString(sym, "client.UserImpl#getStockPositionValue.")) {
            Price p = userPosition.getStockPositionValue(sym);
            return p;
        } else {
            return null;
        }
    }

    /**
     * This method will return a call to user position getStockPositionVolume.
     *
     * @param product
     * @return integer of stock position volume
     */
    @Override
    public int getStockPositionVolume(String product) throws Exception {
        if (ExceptionHandler.checkString(product, "client.UserImpl#getStockPositionVolume.")) {
            int volume = userPosition.getStockPositionVolume(product);
            return volume;
        } else {
            return 0;
        }
    }

    /**
     * This method will return a call to user position getHoldings.
     *
     * @return array list of holdings
     */
    @Override
    public ArrayList<String> getHoldings() {
        ArrayList<String> holdings = userPosition.getHoldings();
        return holdings;
    }

    /**
     * This method will return a call to the user command service
     * getOrdersWithRemainingQty.
     *
     * @param product
     * @return Array List of tradable DTO objects
     * @throws Exception
     */
    @Override
    public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws Exception {
        if (ExceptionHandler.checkString(product, "client.UserImpl#getOrdersWithRemainingQty.")) {
            ArrayList<TradableDTO> orders = UserCommandService.getInstance().getOrdersWithRemainingQty(user, connectionId, product);
            return orders;
        } else {
            return null;
        }
    }
}
//end of file