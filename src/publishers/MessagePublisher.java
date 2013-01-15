/*
 * MessagePublisher.java
 */
package publishers;

import client.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class implements the "singleton" Design pattern, there should only ever
 * be one created instance.<br> This class sends out messages related to cancels
 * or fills.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class MessagePublisher implements Publisher {

    private static PublisherImpl myPublisher; // delegate - will refer to some implementation object 
    private static MessagePublisher instance; // single instance of the MessagePublisher

    private MessagePublisher() {
    }

    /**
     * Public synchronized instance method to return instance of
     * MessagePublisher, or if one has not been created, it creates one and
     * returns it.
     *
     * @return MessagePublisher
     */
    public synchronized static MessagePublisher getInstance() {
        if (instance == null) {
            instance = new MessagePublisher();
            myPublisher = new PublisherImpl();
        }
        return instance;
    }

    /**
     * Public synchronized publish cancel messages to send out cancel messages
     * to users.
     *
     * @param cm cancel message
     */
    public synchronized void publishCancel(CancelMessage cm) {
        if (!myPublisher.getSubscribers().isEmpty()) {
            HashMap<String, HashMap> subscribers = myPublisher.getSubscribers();
            String userName = cm.getUser();
            String product = cm.getProduct();
            if (subscribers.get(product) != null) {
                HashMap<String, User> users = subscribers.get(product);
                if (users.get(userName) != null) {
                    User u = (User) users.get(userName);
                    u.acceptMessage(cm);
                }
            }
        }
    }

    /**
     * Public synchronized publish fill method to send out fill messages to
     * users.
     *
     * @param fm fill message
     */
    public synchronized void publishFill(FillMessage fm) {
        if (!myPublisher.getSubscribers().isEmpty()) {
            HashMap<String, HashMap> subscribers = myPublisher.getSubscribers();
            String userName = fm.getUser();
            String product = fm.getProduct();
            if (subscribers.get(product) != null) {
                HashMap<String, User> users = subscribers.get(product);
                if (users.get(userName) != null) {
                    User u = (User) users.get(userName);
                    u.acceptMessage(fm);
                }
            }
        }
    }

    /**
     * Public synchronized publish market message method to send out market
     * messages to users.
     *
     * @param mm market message
     */
    public synchronized void publishMarketMessage(MarketMessage mm) {
        if (!myPublisher.getSubscribers().isEmpty()) {
            HashMap<String, HashMap> subscribers = myPublisher.getSubscribers();
            if (!subscribers.isEmpty()) {
                Iterator itSub = subscribers.entrySet().iterator();
                ArrayList<User> contacted = new ArrayList<>();
                while (itSub.hasNext()) {
                    Map.Entry subscribersEntry = (Map.Entry) itSub.next();
                    if (subscribersEntry.getValue() != null) {
                        HashMap<String, User> users = (HashMap) subscribersEntry.getValue();
                        Iterator it = users.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry userEntry = (Map.Entry) it.next();
                            if (userEntry.getValue() != null) {
                                User u = (User) userEntry.getValue();
                                if (!contacted.contains(u)) {
                                    u.acceptMarketMessage(mm.toString());
                                    contacted.add(u);
                                }
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
}
//end of file