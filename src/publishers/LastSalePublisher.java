/*
 * LastSalePublisher.java
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
 * This class implements the "singleton" design pattern, and only one instance
 * should ever be created.<br> This class sends out last sales about the market.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class LastSalePublisher implements Publisher {

    /**
     * Delegate - will refer to myPublisher implementation object.
     */
    private static PublisherImpl myPublisher;
    /**
     * Single instance of the MessagePublisher.
     */
    private static LastSalePublisher instance;

    /**
     * Private constructor for single instance construction.
     */
    private LastSalePublisher() {
    }

    /**
     * Public synchronized get Instance method to get LastSalePublisher.
     *
     * @return LastSalePublisher
     */
    public synchronized static LastSalePublisher getInstance() {
        if (instance == null) {
            instance = new LastSalePublisher();
            myPublisher = new PublisherImpl();
        }
        return instance;
    }

    /**
     * Public synchronized publish method which sends out last sale messages.
     *
     * @param product symbol
     * @param p price object
     * @param v volume
     * @throws Exception
     */
    public synchronized void publishLastSale(String product, Price p, int v) throws Exception {
        String thisClass = "publishers.LastSalePublisher#publishLastSale.";
        if (ExceptionHandler.checkString(product, thisClass) && ExceptionHandler.checkIntNegative(v, thisClass)
                && ExceptionHandler.checkIntZero(v, thisClass)) {
            p = check(p);
            if (myPublisher.getSubscribers() != null) {
                HashMap<String, HashMap> subscribers = myPublisher.getSubscribers();
                if (subscribers.get(product) != null) {
                    HashMap<String, User> users = (HashMap<String, User>) subscribers.get(product);
                    if (!users.isEmpty()) {
                        Iterator it = users.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry userEntry = (Map.Entry) it.next();
                            if (userEntry.getValue() != null) {
                                User u = (User) userEntry.getValue();
                                u.acceptLastSale(product, p, v);
                            }
                        }
                        TickerPublisher.getInstance().publishTicker(product, p);
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
     * Private method checks the incoming price value and creates a 0 price if
     * null.
     *
     * @param p price to be checked for null
     * @throws Exception
     */
    private Price check(Price p) throws Exception {
        if (p == null) {
            p = PriceFactory.makeLimitPrice("0.00");
            return p;
        } else {
            return p;
        }
    }
}
//end of file 