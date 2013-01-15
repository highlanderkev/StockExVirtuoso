/**
 * PriceFactory.java
 */
package price;

import exceptions.ExceptionHandler;
import java.util.HashMap;

/**
 * This class creates Price objects for the DePaul Stock Exchange Project.<br>
 * This class implements a simple factory/flyweight design pattern.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class PriceFactory {

    /**
     * HashMap which contains all prices values made so far.
     */
    private static HashMap<Integer, Price> prices = new HashMap<>();

    /**
     * Public Factory method takes an incoming String value and converts it to a
     * long value then passes it to the makeLimitPrice(long value) to return a
     * new Price object.
     *
     * @param value of new price object
     * @return Limit Price object
     * @throws Exception
     * @see #makeLimitPrice(long)
     */
    public static Price makeLimitPrice(String value) throws Exception {
        long numValue = 0;
        String newValue, dollars, cents;
        String[] splitValue;
        boolean isNegative = false;

        //string algorithm for transfer to long value
        if (value.contains("$")) {
            newValue = value.replace("$", "");
            value = newValue;
        }

        if (value.contains(",")) {
            newValue = value.replace(",", "");
            value = newValue;
        }

        if (value.contains("-")) {
            isNegative = true;
            newValue = value.replace("-", "");
            value = newValue;
        }

        if (value.contains(".")) {
            if (value.startsWith(".")) {
                newValue = value.replace(".", "");
                value = newValue.substring(0, 2);
                if (ExceptionHandler.checkPriceInput(value, "Price.PriceFactory#makeLimitPrice.")) {
                    numValue = Long.valueOf(value);
                }
            } else if (value.endsWith(".")) {
                newValue = value.replace(".", "");
                value = newValue.concat("00");
                if (ExceptionHandler.checkPriceInput(value, "Price.PriceFactory#makeLimitPrice.")) {
                    numValue = Long.valueOf(value);
                }
            } else {
                splitValue = value.split("\\.");
                dollars = splitValue[0];
                cents = splitValue[1];
                if (dollars.isEmpty() && cents.isEmpty()) {
                    throw new Exception("THIS IS A SERIOUS ERROR!");
                } else if (Long.valueOf(dollars) != 0) {
                    if (ExceptionHandler.checkPriceInput(dollars, "Price.PriceFactory#makeLimitPrice.")) {
                        numValue = Long.valueOf(dollars);
                        numValue = (numValue * 100);
                    }
                }
                if (cents.isEmpty()) {
                    newValue = cents.concat("00");
                    cents = newValue;
                    value = cents;
                    if (ExceptionHandler.checkPriceInput(value, "Price.PriceFactory#makeLimitPrice.")) {
                        numValue = (numValue + Long.valueOf(value));
                    }
                } else if (cents.length() == 1) {
                    newValue = cents.concat("0");
                    value = newValue;
                    if (ExceptionHandler.checkPriceInput(value, "Price.PriceFactory#makeLimitPrice.")) {
                        numValue = (numValue + Long.valueOf(value));
                    }
                } else if (cents.length() == 2) {
                    value = cents;
                    if (ExceptionHandler.checkPriceInput(value, "Price.PriceFactory#makeLimitPrice.")) {
                        numValue = (numValue + Long.valueOf(value));
                    }
                } else if (cents.length() >= 3) {
                    newValue = cents.substring(0, 2);
                    value = newValue;
                    if (ExceptionHandler.checkPriceInput(value, "Price.PriceFactory#makeLimitPrice.")) {
                        numValue = (numValue + Long.valueOf(value));
                    }
                }
            }
        } else {
            if (ExceptionHandler.checkPriceInput(value, "Price.PriceFactory#makeLimitPrice.")) {
                numValue = Long.valueOf(value);
                numValue = (numValue * 100);
            }
        }

        if (isNegative == true) {
            numValue = (numValue * -1);
        }

        // transfer long value to makeLimitPrice(long value) 
        Price limitPrice = makeLimitPrice(numValue);
        return limitPrice;
    }

    /**
     * This method checks if the value is already in the hash map(Prices), if so
     * it returns with that Price object, if not it returns with a new Price
     * object based off its value.
     *
     * @param value long
     * @return Price object
     * @see price.Price#Price(long)
     */
    public static Price makeLimitPrice(long value) {
        Long valueKey = value;
        Integer key = valueKey.hashCode();
        Price limitPrice;
        /* check to see if value is already stored in prices */
        if (prices.containsKey(key) == true) {
            limitPrice = prices.get(key);
            return limitPrice;
        } else /* create a new Limit Price with the specified value and store it in the hashmap */ {
            limitPrice = new Price(value);
            prices.put(key, limitPrice);
            return limitPrice;
        }
    }

    /**
     * Public Static method to make a Market Price object, checks to see if a
     * market price object is already stored in the hash map(prices) if so it
     * returns with that object, if not it creates a new market price object and
     * returns that.
     *
     * @return Market Price object
     */
    public static Price makeMarketPrice() {
        /* check to see if a Market Price object is already been created */
        Boolean checkMKT = true;
        int key = checkMKT.hashCode();
        Price marketPrice;
        if (prices.containsKey(key) == true) {
            marketPrice = prices.get(key);
            return marketPrice;
        } else // create a new Market Price object and store it in the hashmap
        {
            marketPrice = new Price();
            prices.put(key, marketPrice);
            return marketPrice;
        }
    }
}
//end of file
