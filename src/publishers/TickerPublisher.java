/*
 * TickerPublisher.java
 */
package publishers;

import client.User;
import exceptions.ExceptionHandler;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import price.Price;
import price.PriceFactory;

/**
 * This class implements the "singleton" design pattern, and only one should
 * ever be created.<br> This class handles sending out tickers to users about
 * the market.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class TickerPublisher implements Publisher {

    /**
     * Delegate - will refer to myPublisher implementation object
     */
    private static PublisherImpl myPublisher;
    /**
     * Single instance of the MessagePublisher
     */
    private static TickerPublisher instance;
    /**
     * HashMap holds most up to date stock values
     */
    private static HashMap<String, Price> stocks = new HashMap();

    /**
     * Private Constructor for single instance construction.
     */
    private TickerPublisher() {
    }

    /**
     * Public synchronized get Instance method which returns the instance of the
     * TickerPublisher.
     *
     * @return TickerPublisher instance
     */
    public synchronized static TickerPublisher getInstance() {
        if (instance == null) {
            instance = new TickerPublisher();
            myPublisher = new PublisherImpl();
        }
        return instance;
    }

    /**
     * Public synchronized publish ticker method which sends out tickers about
     * the market.
     *
     * @param product symbol
     * @param p price object
     * @throws Exception
     */
    public synchronized void publishTicker(String product, Price p) throws Exception {
        if (ExceptionHandler.checkString(product, "publishers.TickerPublisher#publishTicker.")) {
            p = checkPrice(p);
            if (!myPublisher.getSubscribers().isEmpty()) {
                HashMap<String, HashMap> subscribers = myPublisher.getSubscribers();
                char movement = ' ';
                if (!stocks.isEmpty() && stocks.get(product) != null) {
                    Price recentPrice = stocks.get(product);
                    if (p.equals(recentPrice)) {
                        movement = '=';
                    } else if (p.greaterThan(recentPrice)) {
                        movement = '↑';
                    } else if (p.lessThan(recentPrice)) {
                        movement = '↓';
                    }
                    stocks.put(product, p);
                } else {
                    stocks.put(product, p);
                }
                if (subscribers.get(product) != null) {
                    HashMap<Integer, User> users = subscribers.get(product);
                    if (!users.isEmpty()) {
                        Iterator it = users.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry userEntry = (Map.Entry) it.next();
                            if (userEntry.getValue() != null) {
                                User u = (User) userEntry.getValue();
                                u.acceptTicker(product, p, movement);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Public synchronized method used to subscribe to updates on stock
     * movements.
     *
     * @param u user
     * @param product symbol
     * @throws Exception
     */
    @Override
    public synchronized void subscribe(User u, String product) throws Exception {
        if (myPublisher != null) {
            myPublisher.subscribe(u, product);
        }
    }

    /**
     * Public synchronized method used to un-subscribe to updates on stock
     * movements.
     *
     * @param u user
     * @param product symbol
     * @throws Exception
     */
    @Override
    public synchronized void unSubscribe(User u, String product) throws Exception {
        if (myPublisher != null) {
            myPublisher.unSubscribe(u, product);
        }
    }

    /**
     * Private method checks the incoming price value, if null it creates a new
     * price of 0.
     *
     * @param p price to be checked for null
     * @throws Exception
     */
    private Price checkPrice(Price p) throws Exception {
        if (p == null) {
            p = PriceFactory.makeLimitPrice("0.00");
            return p;
        } else {
            return p;
        }
    }
}
//end of file