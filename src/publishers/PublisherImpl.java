/*
 * PublisherImpl.java
 */
package publishers;

import client.User;
import java.util.HashMap;
import publishers.utils.InvalidSubscriptionException;

/**
 * This class implements the Publisher Interface and is used as a delegate to
 * handle subscriptions for any class that wants to publish information to
 * users.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class PublisherImpl implements Publisher {

    /**
     * HashMap containing subscribers, key is stock symbol, and holds ArrayList
     * of users.
     */
    private HashMap<String, HashMap<String, User>> subscribers;

    /**
     * Public Constructor Method to create a new Publisher Implementation.
     */
    public PublisherImpl() {
        subscribers = new HashMap<>();
    }

    /**
     * Public synchronized method used to subscribe to updates on stock
     * movements.
     *
     * @param u user
     * @param product symbol
     * @throws InvalidSubscriptionException
     */
    @Override
    public synchronized void subscribe(User u, String product) throws InvalidSubscriptionException {
        if (u.getUserName() == null) {
            throw new InvalidSubscriptionException("InvalidSubscriptionException: Username of user is invalid.");
        }
        String userName = u.getUserName();
        if (subscribers.get(product) == null || subscribers.isEmpty()) {
            HashMap<String, User> user = new HashMap<>();
            user.put(userName, u);
            subscribers.put(product, user);
        } else {
            HashMap<String, User> users = (HashMap<String, User>) subscribers.get(product);
            if (users.get(userName) != null) {
                throw new InvalidSubscriptionException("InvalidSubscriptionException: User is already subscribed to this stock.");
            } else {
                users.put(userName, u);
                subscribers.put(product, users);
            }
        }
    }

    /**
     * Public synchronized method used to un-subscribe to updates on stock
     * movements.
     *
     * @param u user
     * @param product symbol
     * @throws InvalidSubscriptionException
     */
    @Override
    public synchronized void unSubscribe(User u, String product) throws InvalidSubscriptionException {
        String userName = u.getUserName();
        if (subscribers.get(product) != null) {
            HashMap<String, User> users = (HashMap<String, User>) subscribers.get(product);
            if (users.get(userName) != null) {
                users.remove(userName);
            } else {
                throw new InvalidSubscriptionException("InvalidSubscriptionException: User is not subscribed to this stock.");
            }
        } else {
            throw new InvalidSubscriptionException("InvalidSubscriptionException: Product has no current subscribers.");
        }
    }

    /**
     * Public get method to get the hash map of subscribers for the
     * implementation.
     *
     * @return HashMap of subscribers
     */
    public synchronized HashMap getSubscribers() {
        return this.subscribers;
    }
}
//end of file