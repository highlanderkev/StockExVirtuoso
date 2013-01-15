/*
 * ProductBookSide.java
 */
package book;

import constants.BookSide;
import exceptions.ExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import price.Price;
import publishers.CancelMessage;
import publishers.FillMessage;
import publishers.MessagePublisher;
import tradable.Tradable;
import tradable.TradableDTO;

/**
 * This class maintains one side (BUY/SELL) of a stock (product) book.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class ProductBookSide {

    /**
     * Map of all bookEntries for this side.
     */
    private HashMap<Price, ArrayList<Tradable>> bookEntries = new HashMap();
    /**
     * Side that this productBookside represents.
     */
    private BookSide side;
    /**
     * Reference to trade processor object.
     */
    private TradeProcessor trader;
    /**
     * Reference to the product book.
     */
    private ProductBook theBook;

    /**
     * Public construction method to create a new ProductBookSide object.
     *
     * @param book
     * @param side
     * @throws Exception
     */
    public ProductBookSide(ProductBook book, BookSide side) throws Exception {
        setTrader();
        setProductBook(book);
        setBookSide(side);
    }

    /**
     * Private set method to set the ProductBook reference.
     *
     * @param book
     * @throws Exception
     */
    private void setProductBook(ProductBook book) throws Exception {
        if (ExceptionHandler.checkObject(book, "book.ProductBookSide#setProductBook.")) {
            theBook = book;
        }
    }

    /**
     * Private get method to get reference to Product book.
     *
     * @return
     */
    private ProductBook getProductBook() {
        return theBook;
    }

    /**
     * Private set method which sets the side of the book (BUY/SELL).
     *
     * @param side
     * @throws Exception
     */
    private void setBookSide(BookSide side) throws Exception {
        if (ExceptionHandler.checkObject(side, "book.ProductBookSide#setBookSide.")) {
            this.side = side;
        }
    }

    /**
     * Private get method to return side of Book.
     *
     * @return side of Book
     */
    private BookSide getSide() {
        return side;
    }

    /**
     * Private set method to set trader processor to new traderProcessor.
     */
    private void setTrader() throws Exception {
        if (TradeProcessorFactory.makeTradeProcessor(this) != null) {
            trader = TradeProcessorFactory.makeTradeProcessor(this);
        }
    }

    /**
     * Private get method to get reference to Trade Processor.
     *
     * @return reference to trade Processor.
     */
    private TradeProcessor getTradeProcessor() {
        return trader;
    }

    /**
     * This method will generate and return an ArrayList of TradableDTO's on all
     * orders in the ProductBookSide that have remaining quantity for the
     * specified user.
     *
     * @param userName of user
     * @return ArrayList of tradableDTO's
     * @throws Exception
     */
    public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName) throws Exception {
        ArrayList<TradableDTO> usersTradables = new ArrayList<>();
        if (!isEmpty()) {
            Iterator it = bookEntries.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entryTrade = (Map.Entry) it.next();
                ArrayList<Tradable> Tradables = (ArrayList<Tradable>) entryTrade.getValue();
                for (Tradable t : Tradables) {
                    if (!t.isQuote()) {
                        if (t.getRemainingVolume() > 0 && t.getUser().equals(userName)) {
                            TradableDTO newTrade = new TradableDTO(t);
                            usersTradables.add(newTrade);
                        }
                    }
                }
            }
        }
        return usersTradables;
    }

    /**
     * This method will return an ArrayList of the tradable objects that are at
     * the best price in the book entries hash map.
     *
     * @return ArrayList of tradable objects
     */
    synchronized ArrayList<Tradable> getEntriesAtTopOfBook() {
        if (!isEmpty()) {
            ArrayList<Price> sorted = new ArrayList(bookEntries.keySet());
            Collections.sort(sorted);
            if (getSide() == BookSide.BUY) {
                Collections.reverse(sorted);
            }
            return getEntriesAtPrice(sorted.get(0));
        } else {
            return null;
        }
    }

    /**
     * This method will return an array of Strings, where each index holds a
     * "Price x Volume" String.
     *
     * @return Array of Strings
     * @throws Exception
     */
    public synchronized String[] getBookDepth() throws Exception {
        if (isEmpty()) {
            String[] emptyBooks = new String[1];
            emptyBooks[0] = "<Empty>";
            return emptyBooks;
        } else {
            int sizeOfBooks = bookEntries.size();
            String[] copyBooks = new String[sizeOfBooks];
            ArrayList<Price> sorted = new ArrayList<>(bookEntries.keySet());
            Collections.sort(sorted);
            if (getSide() == BookSide.BUY) {
                Collections.reverse(sorted);
            }
            int counter = 0;
            for (Price p : sorted) {
                ArrayList<Tradable> tradables = getEntriesAtPrice(p);
                int sumVolume = 0;
                if (tradables != null) {
                    for (Tradable t : tradables) {
                        sumVolume = sumVolume + t.getRemainingVolume();
                    }
                    String forBooks = (p.toString() + " x " + Integer.toString(sumVolume));
                    copyBooks[counter] = forBooks;
                    counter++;
                }
            }
            return copyBooks;
        }
    }

    /**
     * Package Visible Method to return all tradable objects in this book side
     * at the price.
     *
     * @param price
     * @return ArrayList of tradable objects
     */
    synchronized ArrayList<Tradable> getEntriesAtPrice(Price price) {
        if (bookEntries.get(price) == null) {
            return null;
        } else {
            return bookEntries.get(price);
        }
    }

    /**
     * This method will return true if the product book contains a market price.
     *
     * @return true is bookEntries contains a market price object
     */
    public synchronized boolean hasMarketPrice() {
        if (!isEmpty()) {
            Iterator it = bookEntries.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entryPrice = (Map.Entry) it.next();
                if (entryPrice.getKey() != null) {
                    Price p = (Price) entryPrice.getKey();
                    if (p.isMarket()) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * This method will return true if the ONLY price in the product book is a
     * market price.
     *
     * @return true if ONLY containing a market price
     */
    public synchronized boolean hasOnlyMarketPrice() {
        if (!isEmpty()) {
            Iterator it = bookEntries.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entryPrice = (Map.Entry) it.next();
                if (entryPrice.getKey() != null) {
                    Price p = (Price) entryPrice.getKey();
                    if (p.isMarket() && bookEntries.size() == 1) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * This method will return the best Price in the book side.
     *
     * @return best price in the book
     */
    public synchronized Price topOfBookPrice() {
        if (!isEmpty()) {
            ArrayList<Price> sorted = new ArrayList<>(bookEntries.keySet());
            Collections.sort(sorted);
            if (getSide() == BookSide.BUY) {
                Collections.reverse(sorted);
            }
            return sorted.get(0);
        } else {
            return null;
        }
    }

    /**
     * This method will return the volume associated with the best price in the
     * book.
     *
     * @return top volume on the book
     */
    public synchronized int topOfBookVolume() {
        if (!isEmpty()) {
            ArrayList<Price> sorted = new ArrayList<>(bookEntries.keySet());
            Collections.sort(sorted);
            if (getSide() == BookSide.BUY) {
                Collections.reverse(sorted);
            }
            Price p = sorted.get(0);
            ArrayList<Tradable> tradables = getEntriesAtPrice(p);
            int sumVolume = 0;
            if (tradables != null) {
                for (Tradable t : tradables) {
                    if (t != null) {
                        sumVolume = (t.getRemainingVolume() + sumVolume);
                    }
                }
                return sumVolume;
            }
            return 0;
        } else {
            return 0;
        }
    }

    /**
     * Returns true if the product book is empty, false otherwise.
     *
     * @return true if product book is empty
     */
    public synchronized boolean isEmpty() {
        if (bookEntries.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method will cancel every order or quote side at every price in the
     * book.
     *
     * @throws Exception
     */
    public synchronized void cancelAll() throws Exception {
        while (!isEmpty()) {
            ArrayList<Price> sorted = new ArrayList<>(bookEntries.keySet());
            Collections.sort(sorted);
            if (getSide() == BookSide.BUY) {
                Collections.reverse(sorted);
            }
            for (int i = 0; i < bookEntries.size(); i++) {
                Price p = sorted.get(i);
                ArrayList<Tradable> tradables = getEntriesAtPrice(p);
                if (tradables != null) {
                    for (Tradable t : tradables) {
                        if (t.isQuote()) {
                            submitQuoteCancel(t.getUser());
                            if (tradables.isEmpty()) {
                                break;
                            }
                        } else {
                            submitOrderCancel(t.getId());
                            if (tradables.isEmpty()) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method will search the book entries to find the user quote and
     * remove it.
     *
     * @param user
     * @return Tradable DTO of the quote being removed
     * @throws Exception
     */
    public synchronized TradableDTO removeQuote(String user) throws Exception {
        if (!isEmpty()) {
            Iterator it = bookEntries.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entryTrade = (Map.Entry) it.next();
                if (entryTrade.getValue() != null) {
                    ArrayList<Tradable> tradables = (ArrayList) entryTrade.getValue();
                    for (Tradable t : tradables) {
                        if (t.getUser().equals(user) && t.isQuote()) {
                            TradableDTO userTradable = new TradableDTO(t);
                            tradables.remove(t);
                            if (tradables.isEmpty()) {
                                it.remove();
                            }
                            return userTradable;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method will cancel the order that has the specified identifier.
     *
     * @param orderId
     * @throws Exception
     */
    public synchronized void submitOrderCancel(String orderId) throws Exception {
        Iterator it = bookEntries.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entryTrade = (Map.Entry) it.next();
            if (entryTrade.getValue() != null) {
                ArrayList<Tradable> tradables = (ArrayList<Tradable>) entryTrade.getValue();
                for (Tradable t : tradables) {
                    if (t.getId().equals(orderId) && !t.isQuote()) {
                        tradables.remove(t);
                        String details = (t.getSide() + " Order Cancelled");
                        CancelMessage aCancelMessage = new CancelMessage(t.getUser(), t.getProduct(), t.getPrice(), t.getRemainingVolume(), details, t.getSide(), t.getId());
                        MessagePublisher.getInstance().publishCancel(aCancelMessage);
                        addOldEntry(t);
                        if (tradables.isEmpty()) {
                            it.remove();
                        }
                        return;
                    }
                }
            }
        }
        theBook.checkTooLateToCancel(orderId);
    }

    /**
     * This method will cancel the quote side with the specified userName.
     *
     * @param userName of the quote side to cancel
     * @throws Exception
     */
    public synchronized void submitQuoteCancel(String userName) throws Exception {
        TradableDTO quoteTrade = removeQuote(userName);
        if (quoteTrade != null) {
            String details = ("Quote " + quoteTrade.side + "-Side Cancelled");
            CancelMessage aCancelMessage = new CancelMessage(quoteTrade.user, quoteTrade.product, quoteTrade.price, quoteTrade.remainingVolume, details, quoteTrade.side, quoteTrade.id);
            MessagePublisher.getInstance().publishCancel(aCancelMessage);
        }
    }

    /**
     * This method will add the tradable passed in to the "parent" product books
     * "old entries" list.
     *
     * @param t tradable
     * @throws Exception
     */
    public void addOldEntry(Tradable t) throws Exception {
        theBook.addOldEntry(t);
    }

    /**
     * This method will add the tradable passed in to the book entries.
     *
     * @param t tradable
     * @throws Exception
     */
    public synchronized void addToBook(Tradable t) throws Exception {
        if (getEntriesAtPrice(t.getPrice()) == null) {
            ArrayList<Tradable> newTradables = new ArrayList<>();
            newTradables.add(t);
            bookEntries.put(t.getPrice(), newTradables);
        } else {
            ArrayList<Tradable> tradables = getEntriesAtPrice(t.getPrice());
            tradables.add(t);
        }
    }

    /**
     * This method will attempt a trade of the provided tradable against entries
     * in this ProductBookSide.
     *
     * @param t tradable to trade
     * @return HashMap of fill messages
     * @throws Exception
     */
    public HashMap<String, FillMessage> tryTrade(Tradable t) throws Exception {
        HashMap<String, FillMessage> fillMsgs = new HashMap<>();
        if (getSide() == BookSide.BUY) {
            fillMsgs = trySellAgainstBuySideTrade(t);
        } else if (getSide() == BookSide.SELL) {
            fillMsgs = tryBuyAgainstSellSideTrade(t);
        }
        Iterator itFill = fillMsgs.entrySet().iterator();
        while (itFill.hasNext()) {
            Map.Entry nextEntry = (Map.Entry) itFill.next();
            FillMessage fm = (FillMessage) nextEntry.getValue();
            MessagePublisher.getInstance().publishFill(fm);
        }
        return fillMsgs;
    }

    /**
     * THis method will try to fill the SELL side Tradable passed in against the
     * content of the book.
     *
     * @param t tradable
     * @return HashMap of fill messages
     * @throws Exception
     */
    public synchronized HashMap<String, FillMessage> trySellAgainstBuySideTrade(Tradable t) throws Exception {
        HashMap<String, FillMessage> allFills = new HashMap<>();
        HashMap<String, FillMessage> fillMsgs = new HashMap<>();
        while (t.getRemainingVolume() > 0 && !isEmpty() && (t.getPrice().lessOrEqual(topOfBookPrice()) || t.getPrice().isMarket())) {
            HashMap<String, FillMessage> someMsgs = trader.doTrade(t);
            fillMsgs = mergeFills(fillMsgs, someMsgs);
        }
        allFills.putAll(fillMsgs);
        return allFills;
    }

    /**
     * This method will merge multiple fill messages together into one
     * consistent list.
     *
     * @param existing fill messages
     * @param newOnes fill messages
     * @return a complete Hash map of fill messages
     * @throws Exception
     */
    private HashMap<String, FillMessage> mergeFills(HashMap<String, FillMessage> existing, HashMap<String, FillMessage> newOnes) throws Exception {
        if (existing.isEmpty()) {
            return new HashMap<>(newOnes);
        } else {
            HashMap<String, FillMessage> results = new HashMap<>(existing);
            for (String key : newOnes.keySet()) {
                if (existing.get(key) == null) {
                    results.put(key, newOnes.get(key));
                } else {
                    FillMessage fm = results.get(key);
                    fm.setVolume(newOnes.get(key).getVolume());
                    fm.setDetails(newOnes.get(key).getDetails());
                }
            }
            return results;
        }
    }

    /**
     * This method will fill the BUY side tradable passed in against the content
     * of the book.
     *
     * @param t tradable
     * @return hash map of fill messages
     * @throws Exception
     */
    public synchronized HashMap<String, FillMessage> tryBuyAgainstSellSideTrade(Tradable t) throws Exception {
        HashMap<String, FillMessage> allFills = new HashMap<>();
        HashMap<String, FillMessage> fillMsgs = new HashMap<>();
        while (t.getRemainingVolume() > 0 && !isEmpty() && (t.getPrice().greaterOrEqual(topOfBookPrice()) || t.getPrice().isMarket())) {
            HashMap<String, FillMessage> someMsgs = trader.doTrade(t);
            fillMsgs = mergeFills(fillMsgs, someMsgs);
        }
        allFills.putAll(fillMsgs);
        return allFills;
    }

    /**
     * This method will remove a key/value pair from the book entries if the
     * ArrayList associated with the price passed in is empty.
     *
     * @param p price
     * @throws Exception
     */
    public synchronized void clearIfEmpty(Price p) throws Exception {
        if (bookEntries.get(p) != null) {
            ArrayList<Tradable> tradables = getEntriesAtPrice(p);
            if (tradables != null) {
                if (tradables.isEmpty()) {
                    bookEntries.remove(p);
                }
            }
        }
    }

    /**
     * This method will remove the tradable passed in from the book entries.
     *
     * @param t tradable
     * @throws Exception
     */
    public synchronized void removeTradable(Tradable t) throws Exception {
        if (bookEntries.get(t.getPrice()) != null) {
            ArrayList<Tradable> tradables = getEntriesAtPrice(t.getPrice());
            if (tradables != null && !tradables.isEmpty()) {
                boolean success = tradables.remove(t);
                if (success) {
                    if (tradables.isEmpty()) {
                        clearIfEmpty(t.getPrice());
                    }
                }
            }
        }
    }
}
//end of file