/**
 * Position.java
 */
package client;

import constants.BookSide;
import exceptions.ExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import price.Price;
import price.PriceFactory;

/**
 * This class is used to hold an individual users profits and losses.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class Position {

    /**
     * Stock/Volume holdings of the user.
     */
    private HashMap<String, Integer> holdings;
    /**
     * Running Account costs of user.
     */
    private Price accountCosts;
    /**
     * Last sales of the user.
     */
    private HashMap<String, Price> lastSales;

    /**
     * Public construction method for creating a new position object.
     *
     * @throws Exception
     */
    public Position() throws Exception {
        accountCosts = price.PriceFactory.makeLimitPrice("$0.00");
        lastSales = new HashMap<>();
        holdings = new HashMap<>();
    }

    /**
     * This method will update the holdings list and account costs when activity
     * occurs.
     *
     * @param product
     * @param price
     * @param side
     * @param volume
     * @throws Exception
     */
    public void updatePosition(String product, Price price, BookSide side, int volume) throws Exception {
        String thisClass = "client.Position#updatePosition.";
        if (ExceptionHandler.checkString(product, thisClass) && ExceptionHandler.checkObject(price, thisClass)
                && ExceptionHandler.checkObject(side, thisClass)) {
            int adjustedVolume = 0;
            if (side == BookSide.BUY) {
                adjustedVolume = volume;
            } else {
                adjustedVolume = (-volume);
            }
            if (holdings.get(product) == null || holdings.isEmpty()) {
                holdings.put(product, adjustedVolume);
            } else {
                int holdingVolume = holdings.get(product);
                holdingVolume = holdingVolume + adjustedVolume;
                if (holdingVolume == 0) {
                    holdings.remove(product);
                } else {
                    holdings.put(product, holdingVolume);
                }
            }
            Price totalPrice = price.multiply(volume);
            if (side == BookSide.BUY) {
                accountCosts = accountCosts.subtract(totalPrice);
            } else {
                accountCosts = accountCosts.add(totalPrice);
            }
        }
    }

    /**
     * This method will insert the last sale for the stock into the last sales.
     *
     * @param product
     * @param price
     * @throws Exception
     */
    public void updateLastSale(String product, Price price) throws Exception {
        String thisClass = "client.Position#updateLastSale.";
        if (ExceptionHandler.checkString(product, thisClass) && ExceptionHandler.checkObject(price, thisClass)) {
            lastSales.put(product, price);
        }
    }

    /**
     * This method will return the volume of the specified stock this user owns.
     *
     * @param product
     * @return integer volume
     */
    public int getStockPositionVolume(String product) {
        if (holdings.get(product) == null) {
            return 0;
        } else {
            int positionVolume = holdings.get(product);
            return positionVolume;
        }
    }

    /**
     * This method will return a sorted ArrayList of Strings containing the
     * stock symbols this user owns
     *
     * @return ArrayList of stock symbols
     */
    public ArrayList<String> getHoldings() {
        ArrayList<String> h = new ArrayList(holdings.keySet());
        Collections.sort(h);
        return h;
    }

    /**
     * This method will return the current value of the stock symbol passed in.
     *
     * @param product
     * @return Price of current value
     * @throws Exception
     */
    public Price getStockPositionValue(String product) throws Exception {
        if (holdings.get(product) == null) {
            Price p = PriceFactory.makeLimitPrice("$0.00");
            return p;
        } else {
            Price lastSale = lastSales.get(product);
            if (lastSale == null) {
                lastSale = PriceFactory.makeLimitPrice("$0.00");
            }
            int volume = holdings.get(product);
            lastSale = lastSale.multiply(volume);
            return lastSale;
        }
    }

    /**
     * This method will return the account costs data member.
     *
     * @return current Account
     */
    public Price getAccountCosts() {
        Price p = accountCosts;
        return p;
    }

    /**
     * This method will return the total current value of all stocks this user
     * owns.
     *
     * @return total current value of stocks
     * @throws Exception
     */
    public Price getAllStockValue() throws Exception {
        Price currentValue = PriceFactory.makeLimitPrice("0.00");
        if (!holdings.isEmpty()) {
            Iterator it = holdings.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String product = (String) entry.getKey();
                Price stockPrice = getStockPositionValue(product);
                currentValue = currentValue.add(stockPrice);
            }
        }
        return currentValue;
    }

    /**
     * This method will return the total current value of all stocks this user
     * owns plus the account costs.
     *
     * @return Price of current account costs
     * @throws Exception
     */
    public Price getNetAccountValue() throws Exception {
        Price netValue = getAllStockValue();
        netValue = netValue.add(accountCosts);
        return netValue;
    }
}
//end of file