/**
 * UserCommandService.java
 */
package client;

import book.ProductService;
import client.utils.AlreadyConnectedException;
import client.utils.InvalidConnectionIdException;
import client.utils.UserNotConnectedException;
import constants.BookSide;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import price.Price;
import publishers.CurrentMarketPublisher;
import publishers.LastSalePublisher;
import publishers.MessagePublisher;
import publishers.TickerPublisher;
import tradable.Order;
import tradable.Quote;
import tradable.TradableDTO;

/**
 * This class acts as a facade between a user and the trading system.<br> It
 * implements the singleton design pattern and only one should ever be in
 * existence.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version1.0
 */
public class UserCommandService {

    /**
     * Map of "connected user id".
     */
    private static HashMap<String, Long> connectedUserIds = new HashMap<>();
    /**
     * Map of "connected users".
     */
    private static HashMap<String, User> connectedUsers = new HashMap<>();
    /**
     * Map of "connection time".
     */
    private static HashMap<String, Long> connectionTime = new HashMap<>();
    /**
     * Single instance of this class.
     */
    private static UserCommandService instance;

    /**
     * Private Constructor for single construction of UserCommandService.
     */
    private UserCommandService() {
    }

    /**
     * Public get instance of method to return single instance of the user
     * command service.
     *
     * @return instance of user command service
     */
    public synchronized static UserCommandService getInstance() {
        if (instance == null) {
            instance = new UserCommandService();
        }
        return instance;
    }

    /**
     * This is a utility method to be used by many methods in the class to
     * verify the integrity of the username and connection id.
     *
     * @param userName
     * @param cId connection id
     */
    private boolean verifyUser(String userName, long cId) throws UserNotConnectedException, InvalidConnectionIdException {
        if (connectedUserIds.get(userName) == null) {
            throw new UserNotConnectedException("UserNotConnectedException: User is not connected to the system.");
        } else {
            Long connectionId = connectedUserIds.get(userName);
            if (cId != connectionId) {
                throw new InvalidConnectionIdException("InvalidConnectionIdException: Connection Id is invalid.");
            } else {
                return true;
            }
        }
    }

    /**
     * This method will connect the user to the trading system.
     *
     * @param user
     * @return long of time
     * @throws AlreadyConnectedException
     */
    public synchronized long connect(User user) throws AlreadyConnectedException {
        if (connectedUserIds.get(user) != null) {
            throw new AlreadyConnectedException("AlreadyConnectedException: This user is already connected.");
        } else {
            Long cId = System.nanoTime();
            connectedUserIds.put(user.getUserName(), cId);
            connectedUsers.put(user.getUserName(), user);
            connectionTime.put(user.getUserName(), System.currentTimeMillis());
            return cId;
        }
    }

    /**
     * This method will disconnect the user from the trading system.
     *
     * @param userName
     * @param cId connection id
     * @throws Exception
     */
    public synchronized void disConnect(String userName, long cId) throws Exception {
        if (verifyUser(userName, cId)) {
            connectedUserIds.remove(userName);
            connectedUsers.remove(userName);
            connectionTime.remove(userName);
        }
    }

    /**
     * This method forwards this call to the call of getBookDepth to the product
     * service.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @return 2 dimensional string array
     * @throws Exception
     */
    public String[][] getBookDepth(String userName, long cId, String product) throws Exception {
        if (verifyUser(userName, cId)) {
            String[][] bookDepth = ProductService.getInstance().getBookDepth(product);
            return bookDepth;
        } else {
            return null;
        }
    }

    /**
     * This method forwards this call to the call of getMarketState to the
     * product service.
     *
     * @param userName
     * @param cId connection id
     * @return String of market state
     * @throws Exception
     */
    public String getMarketState(String userName, long cId) throws Exception {
        if (verifyUser(userName, cId)) {
            String marketState = ProductService.getInstance().getMarketState().toString();
            return marketState;
        } else {
            return ("Invalid User.");
        }
    }

    /**
     * This method will forward the call of getOrdersWithRemainingQty to the
     * product service.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @return Array List of tradable DTO objects
     * @throws Exception
     */
    public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName, long cId, String product) throws Exception {
        if (verifyUser(userName, cId)) {
            ArrayList<TradableDTO> tradables = ProductService.getInstance().getOrdersWithRemainingQty(userName, product);
            return tradables;
        } else {
            return null;
        }
    }

    /**
     * This method will return a sorted list of the available stocks on this
     * system, received from the productService.
     *
     * @param userName
     * @param cId connection id
     * @return Array List of strings
     * @throws Exception
     */
    public ArrayList<String> getProducts(String userName, long cId) throws Exception {
        if (verifyUser(userName, cId)) {
            ArrayList<String> products = ProductService.getInstance().getProductList();
            Collections.sort(products);
            return products;
        } else {
            return null;
        }
    }

    /**
     * This method will create an order object using the data passed in, and
     * will forward the order to the product services submitOrder method.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @param price
     * @param volume
     * @param side
     * @return string of order id
     * @throws Exception
     */
    public String submitOrder(String userName, long cId, String product, Price price, int volume, BookSide side) throws Exception {
        if (verifyUser(userName, cId)) {
            Order newOrder = new Order(userName, product, price, volume, side);
            String orderId = ProductService.getInstance().submitOrder(newOrder);
            return orderId;
        } else {
            return ("Invalid Order.");
        }
    }

    /**
     * This method will forward the provided information to the product service
     * submitOrderCancel method.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @param side
     * @param orderId
     * @throws Exception
     */
    public void submitOrderCancel(String userName, long cId, String product, BookSide side, String orderId) throws Exception {
        if (verifyUser(userName, cId)) {
            ProductService.getInstance().submitOrderCancel(product, side, orderId);
        }
    }

    /**
     * This method will create a quote object using the data passed in and will
     * forward the quote to the product services submitQuote method.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @param bPrice buy price
     * @param bVolume buy volume
     * @param sPrice sell price
     * @param sVolume sell volume
     * @throws Exception
     */
    public void submitQuote(String userName, long cId, String product, Price bPrice, int bVolume, Price sPrice, int sVolume) throws Exception {
        if (verifyUser(userName, cId)) {
            Quote newQuote = new Quote(userName, product, bPrice, bVolume, sPrice, sVolume);
            ProductService.getInstance().submitQuote(newQuote);
        }
    }

    /**
     * This method will forward the provided data to the product services
     * submitQuoteCancel method.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws Exception
     */
    public void submitQuoteCancel(String userName, long cId, String product) throws Exception {
        if (verifyUser(userName, cId)) {
            ProductService.getInstance().submitQuoteCancel(userName, product);
        }
    }

    /**
     * This method will forward the subscription request to the current market
     * publisher.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws Exception
     */
    public void subscribeCurrentMarket(String userName, long cId, String product) throws Exception {
        if (verifyUser(userName, cId)) {
            if (connectedUsers.get(userName) == null) {
                throw new UserNotConnectedException("UserNotConnectedException: User name passed to UserCommandService.subscribeCurrentMarket is not connected.");
            } else {
                User u = connectedUsers.get(userName);
                CurrentMarketPublisher.getInstance().subscribe(u, product);
            }
        }
    }

    /**
     * This method will forward the subscription request to the last sale
     * publisher.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws UserNotConnectedException
     * @throws Exception
     */
    public void subscribeLastSale(String userName, long cId, String product) throws UserNotConnectedException, Exception {
        if (verifyUser(userName, cId)) {
            if (connectedUsers.get(userName) == null) {
                throw new UserNotConnectedException("UserNotConnectedException: User name passed to UserCommandService.subscribeCurrentMarket is not connected.");
            } else {
                User u = connectedUsers.get(userName);
                LastSalePublisher.getInstance().subscribe(u, product);
            }
        }
    }

    /**
     * This method will forward the subscription request to the message
     * publisher.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws Exception
     * @throws UserNotConnectedException
     */
    public void subscribeMessages(String userName, long cId, String product) throws UserNotConnectedException, Exception {
        if (verifyUser(userName, cId)) {
            if (connectedUsers.get(userName) == null) {
                throw new UserNotConnectedException("UserNotConnectedException: User name passed to UserCommandService.subscribeCurrentMarket is not connected.");
            } else {
                User u = connectedUsers.get(userName);
                MessagePublisher.getInstance().subscribe(u, product);
            }
        }
    }

    /**
     * This method will forward the subscription request to the ticker
     * publisher.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws Exception
     * @throws UserNotConnectedException
     */
    public void subscribeTicker(String userName, long cId, String product) throws UserNotConnectedException, Exception {
        if (verifyUser(userName, cId)) {
            if (connectedUsers.get(userName) == null) {
                throw new UserNotConnectedException("UserNotConnectedException: User name passed to UserCommandService.subscribeCurrentMarket is not connected.");
            } else {
                User u = connectedUsers.get(userName);
                TickerPublisher.getInstance().subscribe(u, product);
            }
        }
    }

    /**
     * This method will forward the un-subscribe request to the current market
     * publisher.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws Exception
     * @throws UserNotConnectedException
     */
    public void unSubscribeCurrentMarket(String userName, long cId, String product) throws UserNotConnectedException, Exception {
        if (verifyUser(userName, cId)) {
            if (connectedUsers.get(userName) == null) {
                throw new UserNotConnectedException("UserNotConnectedException: User name passed to UserCommandService.subscribeCurrentMarket is not connected.");
            } else {
                User u = connectedUsers.get(userName);
                CurrentMarketPublisher.getInstance().subscribe(u, product);
            }
        }
    }

    /**
     * This method will forward the un-subscribe request to the last sale
     * publisher.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws UserNotConnectedException
     * @throws Exception
     */
    public void unSubscribeLastSale(String userName, long cId, String product) throws UserNotConnectedException, Exception {
        if (verifyUser(userName, cId)) {
            if (connectedUsers.get(userName) == null) {
                throw new UserNotConnectedException("UserNotConnectedException: User name passed to UserCommandService.subscribeCurrentMarket is not connected.");
            } else {
                User u = connectedUsers.get(userName);
                LastSalePublisher.getInstance().subscribe(u, product);
            }
        }
    }

    /**
     * This method will forward the un-subscribe request to the ticker
     * publisher.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws UserNotConnectedException
     * @throws Exception
     */
    public void unSubscribeTicker(String userName, long cId, String product) throws UserNotConnectedException, Exception {
        if (verifyUser(userName, cId)) {
            if (connectedUsers.get(userName) == null) {
                throw new UserNotConnectedException("UserNotConnectedException: User name passed to UserCommandService.subscribeCurrentMarket is not connected.");
            } else {
                User u = connectedUsers.get(userName);
                TickerPublisher.getInstance().subscribe(u, product);
            }
        }
    }

    /**
     * This method will forward the un-subscribe request to the message
     * publisher.
     *
     * @param userName
     * @param cId connection id
     * @param product
     * @throws Exception
     * @throws UserNotConnectedException
     */
    public void unSubscribeMessages(String userName, long cId, String product) throws UserNotConnectedException, Exception {
        if (verifyUser(userName, cId)) {
            if (connectedUsers.get(userName) == null) {
                throw new UserNotConnectedException("UserNotConnectedException: User name passed to UserCommandService.subscribeCurrentMarket is not connected.");
            } else {
                User u = connectedUsers.get(userName);
                MessagePublisher.getInstance().subscribe(u, product);
            }
        }
    }
}
//end of file