/*
 * ProductBook.java
 */
package book;

import book.utils.DataValidationException;
import book.utils.OrderNotFoundException;
import constants.BookSide;
import constants.MarketState;
import exceptions.ExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import price.Price;
import price.PriceFactory;
import publishers.CancelMessage;
import publishers.CurrentMarketPublisher;
import publishers.FillMessage;
import publishers.LastSalePublisher;
import publishers.MarketDataDTO;
import publishers.MessagePublisher;
import tradable.Order;
import tradable.Quote;
import tradable.Tradable;
import tradable.TradableDTO;

/**
 * This class maintains the Buy and Sell sides of a stocks "book".
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class ProductBook {

    /**
     * Stock symbol of product.
     */
    private String product;
    /**
     * Buy Book side object maintains buy side of this book.
     */
    private ProductBookSide buyBook;
    /**
     * Sell Book side object maintains sell side of this book.
     */
    private ProductBookSide sellBook;
    /**
     * String to hold latest market data values.
     */
    private String lastCurrentMarket;
    /**
     * Set of User Quotes.
     */
    private HashSet<String> userQuotes = new HashSet<>();
    /**
     * Map of old Entries.
     */
    private HashMap<Price, ArrayList<Tradable>> oldEntries = new HashMap<>();

    /**
     * Public construction method to create a new Product Book for a stock
     * symbol.
     *
     * @param productSymbol of certain stock
     * @throws Exception
     */
    public ProductBook(String productSymbol) throws Exception {
        setProduct(productSymbol);
        buyBook = new ProductBookSide(this, BookSide.BUY);
        sellBook = new ProductBookSide(this, BookSide.SELL);
        lastCurrentMarket = ("");
    }

    /**
     * Private Set method which sets the product symbol of the tradable object.
     *
     * @param productSymbol
     * @throws Exception
     */
    private void setProduct(String productSymbol) throws Exception {
        if (ExceptionHandler.checkString(productSymbol, "book.ProductBook#setProduct.")) {
            product = productSymbol;
        }
    }

    /**
     * Public Get Method which returns the current product symbol.
     *
     * @return String of product symbol
     */
    public String getProduct() {
        return product;
    }

    /**
     * This method will return an ArrayList containing any orders for the
     * specified user that have remaining quantity.
     *
     * @param userName of user
     * @return ArrayList of tradable DTO's
     * @throws Exception
     */
    public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName) throws Exception {
        ArrayList<TradableDTO> tradables = new ArrayList<>();
        if (buyBook.getOrdersWithRemainingQty(userName) != null) {
            ArrayList<TradableDTO> tempBuy = buyBook.getOrdersWithRemainingQty(userName);
            tradables.addAll(tempBuy);
        }
        if (sellBook.getOrdersWithRemainingQty(userName) != null) {
            ArrayList<TradableDTO> tempSell = sellBook.getOrdersWithRemainingQty(userName);
            tradables.addAll(tempSell);
        }
        return tradables;
    }

    /**
     * This method will determine if it is too late to cancel an order.
     *
     * @param orderId of order
     * @throws OrderNotFoundException
     * @throws Exception
     */
    public synchronized void checkTooLateToCancel(String orderId) throws OrderNotFoundException, Exception {
        Iterator it = oldEntries.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entryTrade = (Map.Entry) it.next();
            if (entryTrade.getValue() != null) {
                ArrayList<Tradable> tradables = (ArrayList<Tradable>) entryTrade.getValue();
                for (Tradable t : tradables) {
                    if (t.getId().equals(orderId) && !t.isQuote()) {
                        String details = ("Too late to Cancel");
                        CancelMessage cm = new CancelMessage(t.getUser(), t.getProduct(), t.getPrice(), t.getRemainingVolume(), details, t.getSide(), t.getId());
                        MessagePublisher.getInstance().publishCancel(cm);
                        return;
                    }
                }
            }
        }
        throw new OrderNotFoundException("OrderNotFoundException: Requested order could not be found.");
    }

    /**
     * This method will return a 2-dimensional array of Strings that contain
     * prices and volumes at all prices present in the buy and sell sides of the
     * book.
     *
     * @return 2-dimensional String array
     * @throws Exception
     */
    public synchronized String[][] getBookDepth() throws Exception {
        String[][] bd = new String[2][];
        bd[0] = buyBook.getBookDepth();
        bd[1] = sellBook.getBookDepth();
        return bd;
    }

    /**
     * This method will create a marketDataDTO containing the best buy side
     * price and volume, and the best sell side price and volume.
     *
     * @return MarketDataDTO of buy side and sell side
     * @throws Exception
     */
    public synchronized MarketDataDTO getMarketData() throws Exception {
        Price bestBuy = buyBook.topOfBookPrice();
        bestBuy = check(bestBuy);
        Price bestSell = sellBook.topOfBookPrice();
        bestSell = check(bestSell);
        int bestBuyVolume = buyBook.topOfBookVolume();
        int bestSellVolume = sellBook.topOfBookVolume();
        String thisClass = "book.ProductBook#getMarketData.";
        if (ExceptionHandler.checkIntNegative(bestBuyVolume, thisClass) && ExceptionHandler.checkIntNegative(bestSellVolume, thisClass)) {
            MarketDataDTO bestMarket = new MarketDataDTO(product, bestBuy, bestBuyVolume, bestSell, bestSellVolume);
            return bestMarket;
        } else {
            return null;
        }
    }

    /**
     * This method will add the tradable passed in to the old Entries hash map.
     *
     * @param t tradable to be added to old entries
     * @throws Exception
     */
    public synchronized void addOldEntry(Tradable t) throws Exception {
        if (oldEntries.get(t.getPrice()) == null) {
            ArrayList<Tradable> newTrades = new ArrayList<>();
            oldEntries.put(t.getPrice(), newTrades);
            t.setCancelledVolume(t.getRemainingVolume());
            t.setRemainingVolume(0);
        } else {
            ArrayList<Tradable> tradables = oldEntries.get(t.getPrice());
            tradables.add(t);
        }
    }

    /**
     * This method opens the book for trading.
     *
     * @throws Exception
     */
    public synchronized void openMarket() throws Exception {
        Price buyPrice = buyBook.topOfBookPrice();
        Price sellPrice = sellBook.topOfBookPrice();
        if (buyPrice != null || sellPrice != null) {
            while (buyPrice.greaterOrEqual(sellPrice) || buyPrice.isMarket() || sellPrice.isMarket()) {
                ArrayList<Tradable> topBuy = buyBook.getEntriesAtPrice(buyPrice);
                HashMap<String, FillMessage> allFills = new HashMap<>();
                ArrayList<Tradable> toRemove = new ArrayList<>();
                for (Tradable t : topBuy) {
                    allFills = sellBook.tryTrade(t);
                    if (t.getRemainingVolume() == 0) {
                        toRemove.add(t);
                    }
                }
                for (Tradable t : toRemove) {
                    buyBook.removeTradable(t);
                }
                updateCurrentMarket();
                Price lastSalePrice = determineLastSalePrice(allFills);
                int lastSaleVolume = determineLastSaleQuantity(allFills);
                LastSalePublisher.getInstance().publishLastSale(product, lastSalePrice, lastSaleVolume);
                buyPrice = buyBook.topOfBookPrice();
                sellPrice = sellBook.topOfBookPrice();
                if (buyPrice == null || sellPrice == null) {
                    break;
                }
            }
        }
    }

    /**
     * This method will close the book for trading.
     *
     * @throws Exception
     */
    @SuppressWarnings("empty-statement")
    public synchronized void closeMarket() throws Exception {
        buyBook.cancelAll();;
        sellBook.cancelAll();
        updateCurrentMarket();
    }

    /**
     * This method will cancel the order specified by the provided order ID on
     * the specified side.
     *
     * @param side of book
     * @param orderId of order
     * @throws Exception
     */
    public synchronized void cancelOrder(BookSide side, String orderId) throws Exception {
        if (side == BookSide.BUY) {
            buyBook.submitOrderCancel(orderId);
            updateCurrentMarket();
        } else if (side == BookSide.SELL) {
            sellBook.submitOrderCancel(orderId);
            updateCurrentMarket();
        } else {
            // do nothing and return   
        }
    }

    /**
     * This method will cancel the specified user's quote on both the buy and
     * sell sides.
     *
     * @param userName of user
     * @throws Exception
     */
    public synchronized void cancelQuote(String userName) throws Exception {
        buyBook.submitQuoteCancel(userName);
        sellBook.submitQuoteCancel(userName);
        updateCurrentMarket();
    }

    /**
     * This method will add the provided quote side to the Buy and Sell product
     * book sides.
     *
     * @param q
     * @throws DataValidationException
     * @throws Exception
     */
    public synchronized void addToBook(Quote q) throws DataValidationException, Exception {
        Price sellQuote = q.getQuoteSide(BookSide.SELL).getPrice();
        Price buyQuote = q.getQuoteSide(BookSide.BUY).getPrice();
        sellQuote = check(sellQuote);
        buyQuote = check(buyQuote);
        int sellVolume = q.getQuoteSide(BookSide.SELL).getOriginalVolume();
        int buyVolume = q.getQuoteSide(BookSide.BUY).getOriginalVolume();
        if (sellQuote.lessOrEqual(buyQuote)) {
            throw new DataValidationException("DataValidationException: Sell price is less than or equal to buy price.");
        } else if (buyQuote.lessOrEqual(PriceFactory.makeLimitPrice("00")) || sellQuote.lessOrEqual(PriceFactory.makeLimitPrice("00"))) {
            throw new DataValidationException("DataValidationException: Price is less than or equal to zero.");
        } else if (buyVolume <= 0 || sellVolume <= 0) {
            throw new DataValidationException("DataValidationException: Volume is less than or equal to zero.");
        } else {
            String userName = q.getUserName();
            if (userQuotes.contains(userName)) {
                buyBook.removeQuote(userName);
                sellBook.removeQuote(userName);
            }
            if (ExceptionHandler.checkObject(q, "book.ProductBook#addtoBook.")) {
                Tradable buyQ = q.getQuoteSide(BookSide.BUY);
                Tradable sellQ = q.getQuoteSide(BookSide.SELL);
                addToBook(BookSide.BUY, buyQ);
                addToBook(BookSide.SELL, sellQ);
                userQuotes.add(userName);
                updateCurrentMarket();
            }
        }
    }

    /**
     * This method will add the provided order to the appropriate product book
     * side.
     *
     * @param o order
     * @throws Exception
     */
    public synchronized void addToBook(Order o) throws Exception {
        addToBook(o.getSide(), o);
        updateCurrentMarket();
    }

    /**
     * This method is a key part of the trading system and deals with the
     * addition of Tradable objects to the BUY/SELL product book side, handling
     * the results of any trades.
     *
     * @param side of book
     * @param t tradable
     */
    private synchronized void addToBook(BookSide side, Tradable t) throws Exception {
        if (ProductService.getInstance().getMarketState() == MarketState.PREOPEN) {
            if (side == BookSide.BUY) {
                buyBook.addToBook(t);
            } else if (side == BookSide.SELL) {
                sellBook.addToBook(t);
            }
        } else {
            HashMap<String, FillMessage> allFills = null;
            if (side == BookSide.SELL) {
                allFills = buyBook.tryTrade(t);
            } else if (side == BookSide.BUY) {
                allFills = sellBook.tryTrade(t);
            }
            if (allFills != null && !allFills.isEmpty()) {
                updateCurrentMarket();
                int diff = (t.getOriginalVolume() - t.getRemainingVolume());
                Price lastSalePrice = determineLastSalePrice(allFills);
                lastSalePrice = check(lastSalePrice);
                LastSalePublisher.getInstance().publishLastSale(product, lastSalePrice, diff);
            }
            if (t.getRemainingVolume() > 0) {
                if (t.getPrice().isMarket()) {
                    CancelMessage cm = new CancelMessage(t.getUser(), t.getProduct(), t.getPrice(), t.getRemainingVolume(), "Cancelled", t.getSide(), t.getId());
                    MessagePublisher.getInstance().publishCancel(cm);
                } else {
                    if (side == BookSide.BUY) {
                        buyBook.addToBook(t);
                    } else if (side == BookSide.SELL) {
                        sellBook.addToBook(t);
                    }
                }
            }
        }
    }

    /**
     * This method will determine if the market for this stock product has been
     * updated by some market action.
     *
     * @throws Exception
     */
    public synchronized void updateCurrentMarket() throws Exception {
        Price topBuy = buyBook.topOfBookPrice();
        Price topSell = sellBook.topOfBookPrice();
        topBuy = check(topBuy);
        topSell = check(topSell);
        int buyTopVolume = buyBook.topOfBookVolume();
        int sellTopVolume = sellBook.topOfBookVolume();
        String thisClass = "book.ProductBook#updateCurrentMarket.";
        if (ExceptionHandler.checkIntNegative(buyTopVolume, thisClass) && ExceptionHandler.checkIntNegative(sellTopVolume, thisClass)
                && ExceptionHandler.checkObject(topBuy, thisClass) && ExceptionHandler.checkObject(topSell, thisClass)) {
            String currentMarket = topBuy.toString() + buyTopVolume + topSell.toString() + sellTopVolume;
            if (!lastCurrentMarket.equals(currentMarket)) {
                MarketDataDTO marketData = new MarketDataDTO(product, topBuy, buyTopVolume, topSell, sellTopVolume);
                CurrentMarketPublisher.getInstance().publishCurrentMarket(marketData);
                lastCurrentMarket = currentMarket;
            }
        }
    }

    /**
     * This method will take a hash amp of fill messages and determine if it
     * contains what the last sale price is.
     *
     * @param fills messages
     * @return price of last sale
     * @throws Exception
     */
    private synchronized Price determineLastSalePrice(HashMap<String, FillMessage> fills) throws Exception {
        ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
        Collections.sort(msgs);
        if (msgs.get(0).getSide().equals(BookSide.BUY)) {
            Price p = msgs.get(0).getPrice();
            return p;
        } else {
            Collections.reverse(msgs);
            Price p = msgs.get(0).getPrice();
            return p;
        }
    }

    /**
     * This method will take a hash map of fill messages and determine if it
     * contains what the last sale quantity is.
     *
     * @param fill messages
     * @return integer of last sale volume
     */
    private synchronized int determineLastSaleQuantity(HashMap<String, FillMessage> fills) {
        ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
        Collections.sort(msgs);
        if (msgs.get(0).getSide().equals(BookSide.BUY)) {
            int volume = msgs.get(0).getVolume();
            return volume;
        } else {
            Collections.reverse(msgs);
            int volume = msgs.get(0).getVolume();
            return volume;
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