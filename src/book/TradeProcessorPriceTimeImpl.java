/*
 * TradeProcessorPriceTimeImpl.java
 */
package book;

import exceptions.ExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import price.Price;
import publishers.FillMessage;
import tradable.Tradable;

/**
 * This is an implementation of the trade processor interface.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class TradeProcessorPriceTimeImpl implements TradeProcessor {

    /**
     * Map of fill messages.
     */
    private HashMap<String, FillMessage> fillMessages = new HashMap<>();
    /**
     * Reference to book side.
     */
    private ProductBookSide bookSide;

    /**
     * Public constructor which sets up the reference to the product book side.
     *
     * @param productSideBook
     * @throws Exception
     */
    public TradeProcessorPriceTimeImpl(ProductBookSide productSideBook) throws Exception {
        setProductBookSide(productSideBook);
    }

    /**
     * Private set method to set the product book side.
     *
     * @param productSideBook
     * @throws Exception
     */
    private void setProductBookSide(ProductBookSide productSideBook) throws Exception {
        if (ExceptionHandler.checkObject(productSideBook, "book.TradeProcessorPriceTimeImpl.")) {
            bookSide = productSideBook;
        }
    }

    /**
     * This method will make a string key for the fill message.
     *
     * @param fm fill message
     * @throws Exception
     */
    private String makeFillKey(FillMessage fm) throws Exception {
        String fillKey = (fm.getUser() + fm.getId() + fm.getPrice().toString());
        return fillKey;
    }

    /**
     * This method checks the content of the fillMessages to see if the
     * FillMessage is a fill message for an existing known trade.
     *
     * @param fm fill message
     * @return boolean if fill message is a new trade
     * @throws Exception
     */
    private boolean isNewFill(FillMessage fm) throws Exception {
        String fillKey = makeFillKey(fm);
        if (fillMessages.get(fillKey) == null) {
            return true;
        } else {
            FillMessage oldFill = fillMessages.get(fillKey);
            if (oldFill.getSide() != fm.getSide()) {
                return true;
            }
            String oldId = oldFill.getId();
            String newId = fm.getId();
            if (!oldId.equals(newId)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * This method will add a fill message to the fillMessages if it is a new
     * trade or update an existing fill message as another part of an existing
     * trade.
     *
     * @param fm fill message
     * @throws Exception
     */
    private void addFillMessage(FillMessage fm) throws Exception {
        if (isNewFill(fm)) {
            String fillKey = makeFillKey(fm);
            fillMessages.put(fillKey, fm);
        } else {
            String fillKey = makeFillKey(fm);
            FillMessage updateFill = fillMessages.get(fillKey);
            updateFill.setVolume(fm.getVolume() + updateFill.getVolume());
            updateFill.setDetails(fm.getDetails());
        }
    }

    /**
     * This method will be called when it has been determined that a tradable
     * can trade against the content of the book.
     *
     * @param trd tradable
     * @return HashMap of fill messages
     * @throws Exception
     */
    @Override
    public HashMap<String, FillMessage> doTrade(Tradable trd) throws Exception {
        fillMessages = new HashMap<>();
        ArrayList<Tradable> tradedOut = new ArrayList<>();
        ArrayList<Tradable> entriesAtPrice = new ArrayList<>();
        entriesAtPrice = bookSide.getEntriesAtTopOfBook();
        for (Tradable t : entriesAtPrice) {
            if (trd.getRemainingVolume() <= 0) {
                break;
            } else {
                if (trd.getRemainingVolume() >= t.getRemainingVolume()) {
                    tradedOut.add(t);
                    Price tPrice;
                    if (t.getPrice().isMarket()) {
                        tPrice = trd.getPrice();
                    } else {
                        tPrice = t.getPrice();
                    }
                    int remainder = (trd.getRemainingVolume() - t.getRemainingVolume());
                    FillMessage fmT = new FillMessage(t.getUser(), t.getProduct(), tPrice, t.getRemainingVolume(), "leaving 0", t.getSide(), t.getId());
                    addFillMessage(fmT);
                    FillMessage fmTrd = new FillMessage(trd.getUser(), trd.getProduct(), tPrice, t.getRemainingVolume(), ("leaving " + remainder), trd.getSide(), trd.getId());
                    addFillMessage(fmTrd);
                    trd.setRemainingVolume(trd.getRemainingVolume() - t.getRemainingVolume());
                    t.setRemainingVolume(0);
                    bookSide.addOldEntry(t);
                } else {
                    int remainder = (t.getRemainingVolume() - trd.getRemainingVolume());
                    Price tPrice;
                    if (t.getPrice().isMarket()) {
                        tPrice = trd.getPrice();
                    } else {
                        tPrice = t.getPrice();
                    }
                    FillMessage fmT = new FillMessage(t.getUser(), t.getProduct(), tPrice, trd.getRemainingVolume(), ("leaving " + remainder), t.getSide(), t.getId());
                    addFillMessage(fmT);
                    FillMessage fmTrd = new FillMessage(trd.getUser(), trd.getProduct(), tPrice, trd.getRemainingVolume(), "leaving 0", trd.getSide(), trd.getId());
                    addFillMessage(fmTrd);
                    trd.setRemainingVolume(0);
                    t.setRemainingVolume(remainder);
                    bookSide.addOldEntry(trd);
                }
            }
        }
        for (Tradable t : tradedOut) {
            entriesAtPrice.remove(t);
        }
        if (entriesAtPrice.isEmpty()) {
            bookSide.clearIfEmpty(bookSide.topOfBookPrice());
        }
        return fillMessages;
    }
}
//end of file