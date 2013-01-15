/*
 * ProductService.java
 */
package book;

import book.utils.InvalidMarketStateException;
import book.utils.InvalidMarketStateTransition;
import book.utils.NoSuchProductException;
import book.utils.OrderNotFoundException;
import book.utils.ProductAlreadyExistsException;
import constants.BookSide;
import constants.MarketState;
import exceptions.ExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import publishers.MarketDataDTO;
import publishers.MarketMessage;
import publishers.MessagePublisher;
import tradable.Order;
import tradable.Quote;
import tradable.TradableDTO;

/**
 * This class implements the Facade design pattern to other entities that make
 * up the products and the product books.<br> This will also be implemented as a
 * thread-safe singleton.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class ProductService {

    /**
     * Map of all books.
     */
    private static HashMap<String, ProductBook> allBooks = new HashMap<>();
    /**
     * State of market OPEN, CLOSED or PREOPEN.
     */
    private static MarketState state = MarketState.CLOSED;
    /**
     * Single instance of the Product Service.
     */
    private static ProductService instance;

    /**
     * Private Constructor Method.
     */
    private ProductService() {
    }

    /**
     * Public Get Instance Method which returns the single instance of Product
     * Service.
     *
     * @return Product Service
     */
    public synchronized static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    /**
     * This method will return a List of TradableDTOs containing any orders with
     * remaining quantity for the user and the stock.
     *
     * @param userName of user
     * @param product symbol
     * @return ArrayList of Tradable DTOs
     * @throws Exception
     * @throws OrderNotFoundException
     */
    public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName, String product) throws OrderNotFoundException, Exception {
        if (!allBooks.isEmpty()) {
            ProductBook theBook = allBooks.get(product);
            ArrayList<TradableDTO> tradables = theBook.getOrdersWithRemainingQty(userName);
            return tradables;
        } else {
            throw new OrderNotFoundException("OrderNotFoundException: The books are currently empty.");
        }
    }

    /**
     * This method will return a List of MarketData DTOs containing the best buy
     * price/volume sell price/volume for the stock.
     *
     * @param product stock
     * @return MarketDTO data
     * @throws OrderNotFoundException
     * @throws Exception
     */
    public synchronized MarketDataDTO getMarketData(String product) throws OrderNotFoundException, Exception {
        if (!allBooks.isEmpty()) {
            ProductBook theBook = allBooks.get(product);
            MarketDataDTO marketData = theBook.getMarketData();
            return marketData;
        } else {
            throw new OrderNotFoundException("OrderNotFoundException: The books are currently empty.");
        }
    }

    /**
     * Returns the market state.
     *
     * @return market state
     */
    public synchronized MarketState getMarketState() {
        return state;
    }

    /**
     * This method will call get the product book for the product book and call
     * the get book depth of that book.
     *
     * @param product symbol
     * @return 2 dimensional string array
     * @throws NoSuchProductException
     * @throws Exception
     */
    public synchronized String[][] getBookDepth(String product) throws NoSuchProductException, Exception {
        if (allBooks.get(product) == null) {
            throw new NoSuchProductException("NoSuchProductException: No product currently on the books.");
        } else {
            ProductBook theBook = allBooks.get(product);
            String[][] bookDepth = theBook.getBookDepth();
            return bookDepth;
        }
    }

    /**
     * This method will return an ArrayList containing all the keys in the
     * allBooks.
     *
     * @return keys of allBooks
     */
    public synchronized ArrayList<String> getProductList() {
        return new ArrayList<>(allBooks.keySet());
    }

    /**
     * This method will update the market data to the new state.
     *
     * @param ms new market state
     * @throws Exception
     */
    public synchronized void setMarketState(MarketState ms) throws Exception {
        check(ms);
        state = ms;
        MarketMessage mm = new MarketMessage(ms);
        MessagePublisher.getInstance().publishMarketMessage(mm);
        if (ms == MarketState.OPEN) {
            Iterator it = allBooks.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entryBook = (Map.Entry) it.next();
                if (entryBook.getValue() != null) {
                    ProductBook theBook = (ProductBook) entryBook.getValue();
                    theBook.openMarket();
                }
            }
        } else if (ms == MarketState.CLOSED) {
            Iterator it = allBooks.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entryBook = (Map.Entry) it.next();
                if (entryBook.getValue() != null) {
                    ProductBook theBook = (ProductBook) entryBook.getValue();
                    theBook.closeMarket();
                }
            }
        }
    }

    /**
     * This method will create a new stock product that can be used for trading.
     *
     * @param product of new stock
     * @throws ProductAlreadyExistsException
     * @throws Exception
     */
    public synchronized void createProduct(String product) throws ProductAlreadyExistsException, Exception {
        if (allBooks.get(product) != null) {
            throw new ProductAlreadyExistsException("ProductAlreadyExistsException: Product is already on the books.");
        } else if (ExceptionHandler.checkString(product, "book.ProductService#createProduct.")) {
            ProductBook newBook = new ProductBook(product);
            allBooks.put(product, newBook);
        }
    }

    /**
     * This method will forward the provided quote to the appropriate product
     * book.
     *
     * @param q quote
     * @throws InvalidMarketStateException
     * @throws NoSuchProductException
     * @throws Exception
     */
    public synchronized void submitQuote(Quote q) throws InvalidMarketStateException, NoSuchProductException, Exception {
        String product = q.getProduct();
        if (state == MarketState.CLOSED) {
            throw new InvalidMarketStateException("InvalidMarketStateException: Market is CLOSED.");
        } else if (allBooks.get(product) == null) {
            throw new NoSuchProductException("NoSuchProductException: No product on the books.");
        } else {
            ProductBook theBook = allBooks.get(product);
            theBook.addToBook(q);
        }
    }

    /**
     * This method will forward the provided order to the appropriate product
     * book.
     *
     * @param o order
     * @return String of Order/system ID
     * @throws InvalidMarketStateException
     * @throws NoSuchProductException
     * @throws Exception
     */
    public synchronized String submitOrder(Order o) throws NoSuchProductException, InvalidMarketStateException, Exception {
        if (state == MarketState.CLOSED) {
            throw new InvalidMarketStateException("InvalidMarketStateException: Market is CLOSED.");
        } else if (state == MarketState.PREOPEN && o.getPrice().isMarket()) {
            throw new InvalidMarketStateException("InvalidMarketStateException: Market is PREOPEN, cannot submit Market orders during this time.");
        } else if (allBooks.get(o.getProduct()) == null) {
            throw new NoSuchProductException("NoSuchProductException: No product on the books.");
        } else {
            ProductBook theBook = allBooks.get(o.getProduct());
            theBook.addToBook(o);
            return o.getId();
        }
    }

    /**
     * This method will forward the provided cancel order to the appropriate
     * book.
     *
     * @param product symbol
     * @param side of book
     * @param orderId system ID
     * @throws NoSuchProductException
     * @throws InvalidMarketStateException
     * @throws Exception
     */
    public synchronized void submitOrderCancel(String product, BookSide side, String orderId) throws NoSuchProductException, InvalidMarketStateException, Exception {
        if (state == MarketState.CLOSED) {
            throw new InvalidMarketStateException("InvalidMarketStateException: Market is CLOSED.");
        } else if (allBooks.get(product) == null) {
            throw new NoSuchProductException("NoSuchProductException: No product on the books.");
        } else {
            ProductBook theBook = allBooks.get(product);
            theBook.cancelOrder(side, orderId);
        }
    }

    /**
     * This method will forward the provided cancel quote to the appropriate
     * product book.
     *
     * @param userName of user
     * @param product symbol
     * @throws NoSuchProductException
     * @throws InvalidMarketStateException
     * @throws Exception
     */
    public synchronized void submitQuoteCancel(String userName, String product) throws NoSuchProductException, InvalidMarketStateException, Exception {
        if (state == MarketState.CLOSED) {
            throw new InvalidMarketStateException("InvalidMarketStateException: Market is CLOSED.");
        } else if (allBooks.get(product) == null) {
            throw new NoSuchProductException("NoSuchProductException: No product on the books.");
        } else {
            ProductBook theBook = allBooks.get(product);
            theBook.cancelQuote(userName);
        }
    }

    /**
     * This method will check to make sure it is a valid transition of one
     * market state to the next or else throw an exception.
     *
     * @param ms new Market State
     */
    private synchronized void check(MarketState ms) throws InvalidMarketStateTransition {
        if (state == MarketState.CLOSED) {
            if (ms == MarketState.OPEN) {
                throw new InvalidMarketStateTransition("InvalidMarketStateTransition: Market cannot go directly from CLOSED to OPEN.");
            } else if (ms == MarketState.CLOSED) {
                throw new InvalidMarketStateTransition("InvalidMarketStateTransition: Market is already CLOSED.");
            }
        } else if (state == MarketState.OPEN) {
            if (ms == MarketState.PREOPEN) {
                throw new InvalidMarketStateTransition("InvalidMarketStateTransition: Market cannot go from OPEN to PREOPEN.");
            } else if (ms == MarketState.OPEN) {
                throw new InvalidMarketStateTransition("InvalidMarketStateTransition: Market is already OPEN.");
            }
        } else if (state == MarketState.PREOPEN) {
            if (ms == MarketState.CLOSED) {
                throw new InvalidMarketStateTransition("InvalidMarketStateTransition: Market cannot go from PREOPEN to CLOSED.");
            } else if (ms == MarketState.PREOPEN) {
                throw new InvalidMarketStateTransition("InvalidMarketStateTransition: Market is already PREOPEN.");
            }
        } else {
            //return the market state Transition is correct.
        }
    }
}
//end of file

