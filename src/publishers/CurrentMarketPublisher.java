/*
 * CurrentMarketPublisher.java
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
 * This class implements the "singleton" design pattern, and only a single
 * instance should ever be created.<br> This class sends out current market
 * updates.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class CurrentMarketPublisher implements Publisher {

    /**
     * Delegate - will refer to myPublisher implementation object.
     */
    private static PublisherImpl myPublisher;
    /**
     * Single instance of the MessagePublisher.
     */
    private static CurrentMarketPublisher instance;

    /**
     * Private constructor method for single instance construction.
     */
    private CurrentMarketPublisher() {
    }

    /**
     * Public Get Instance Method which returns instance, or starts a new one,
     * if not created.
     *
     * @return Instance of Current Market Publisher
     */
    public synchronized static CurrentMarketPublisher getInstance() {
        if (instance == null) {
            instance = new CurrentMarketPublisher();
            myPublisher = new PublisherImpl();
        }
        return instance;
    }

    /**
     * Public synchronized publish method to publish current market data.
     *
     * @param md market data transfer object
     * @throws Exception
     */
    public synchronized void publishCurrentMarket(MarketDataDTO md) throws Exception {
        int bv = md.buyVolume;
        int sv = md.sellVolume;
        String product = md.product;
        String exString = "publishers.CurrentMarketPublisher#publishCurrentMarket";
        if (ExceptionHandler.checkIntNegative(bv, exString) && ExceptionHandler.checkIntNegative(sv, exString)
                && ExceptionHandler.checkString(product, exString)) {
            Price bp = md.buyPrice;
            bp = checkPrice(bp);
            Price sp = md.sellPrice;
            sp = checkPrice(sp);
            if (!myPublisher.getSubscribers().isEmpty()) {
                HashMap<String, HashMap> subscribers = myPublisher.getSubscribers();
                if (subscribers.get(product) != null) {
                    HashMap<Integer, User> users = subscribers.get(product);
                    if (!users.isEmpty()) {
                        Iterator it = users.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry userEntry = (Map.Entry) it.next();
                            if (userEntry.getValue() != null) {
                                User u = (User) userEntry.getValue();
                                u.acceptCurrentMarket(product, bp, bv, sp, sv);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Public synchronized subscribe method used to subscribe to updates on
     * stock movements.
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
     * Public synchronized un-subscribe method used to un-subscribe to updates
     * on stock movements.
     *
     * @param u user to un-subscribe
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
     * Private method checks the incoming price value and sets a null price to
     * 0.
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