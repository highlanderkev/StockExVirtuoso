/**
 * Price.java
 */
package price;

import price.utils.InvalidPriceOperation;

/**
 * This class governs the construction and operation of a Price object.<br> This
 * object is immutable and cannot be changed after creation.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public final class Price implements Comparable<Price> {

    /**
     * Value of price.
     */
    private final long VALUE;
    /**
     * Boolean Market Price: Yes or No?.
     */
    private final boolean MKT;

    /**
     * Package-visible Construction Method for a new Limit Price object.
     *
     * @param value Price value passed into setValue
     */
    Price(long value) {
        this.VALUE = value;
        this.MKT = false;
    }

    /**
     * Package-visible Construction Method for a new Market Price object.
     *
     * @see #setValue(long)
     * @see #setMarket(boolean)
     */
    Price() {
        this.VALUE = 0;
        this.MKT = true;
    }

    /**
     * Private Get method to return value of current object.
     *
     * @return currentValue
     */
    private long getValue() {
        return VALUE;
    }

    /**
     * Private Get method to return true is Market Price.
     *
     * @return currentMKT
     */
    private boolean getMarketPrice() {
        return MKT;
    }

    /**
     * Public Add method which adds two prices and returns new price.
     *
     * @param p Price object
     * @return Price object
     * @throws InvalidPriceOperation if either of the objects are a Market Price
     */
    public Price add(Price p) throws InvalidPriceOperation {
        if (isMarket() == true || p.isMarket() == true) {
            throw new InvalidPriceOperation("InvalidPriceOperation: Cannot add a LIMIT price to a MARKET Price");
        } else {
            long currentValue = getValue();
            long addValue = p.VALUE;
            long newValue = (currentValue + addValue);
            Price newPrice = PriceFactory.makeLimitPrice(newValue);
            return newPrice;
        }
    }

    /**
     * Public Subtract method which subtracts two prices and returns new price.
     *
     * @param p Price object
     * @return Price object
     * @throws InvalidPriceOperation if either of the objects are a Market Price
     */
    public Price subtract(Price p) throws InvalidPriceOperation {
        if (isMarket() == true || p.isMarket() == true) {
            throw new InvalidPriceOperation("InvalidPriceOperation: Cannot subtract a LIMIT price from a MARKET Price");
        } else {
            long currentValue = getValue();
            long subValue = p.VALUE;
            long newValue = (currentValue - subValue);
            Price newPrice = PriceFactory.makeLimitPrice(newValue);
            return newPrice;
        }
    }

    /**
     * Public Multiply method which multiplies the current price by a number to
     * get a new price.
     *
     * @param p Price object
     * @return Price object
     * @throws InvalidPriceOperation if the current price is a market Price or
     * multiply value less than or equal to 0
     * @see #getValue()
     */
    public Price multiply(int p) throws InvalidPriceOperation {
        if (isMarket() == true) {
            throw new InvalidPriceOperation("InvalidPriceOperation: Cannot multiply a MARKET price");
        } else {
            long currentValue = getValue();
            long newValue = (currentValue * p);
            Price newPrice = PriceFactory.makeLimitPrice(newValue);
            return newPrice;
        }
    }

    /**
     * Public method which compares the current price to another price.
     *
     * @param p Price Object
     * @return integer 1 if greater than, -1 if les than, 0 if equal
     * @see #getValue()
     */
    @Override
    public int compareTo(Price p) {
        long comparable = p.VALUE;

        if (getValue() == comparable) {
            return 0;
        } else if (getValue() < comparable) {
            return -1;
        } else if (getValue() > comparable) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Public method which compares two prices for greater than or equal to.
     *
     * @param p Price Object
     * @return boolean if greater than or equal to comparable price
     * @see #compareTo(price.Price)
     * @see #isMarket()
     */
    public boolean greaterOrEqual(Price p) {
        if (isMarket() || p.isMarket()) {
            return false;
        } else if (compareTo(p) == 0 || compareTo(p) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Public Greater Than method which compares if the current price is greater
     * than another price.
     *
     * @param p Price object
     * @return boolean true if greater than, false otherwise or if market price
     * @see #isMarket()
     * @see #compareTo(price.Price)
     */
    public boolean greaterThan(Price p) {
        if (isMarket() || p.isMarket()) {
            return false;
        } else if (compareTo(p) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Public Less than or Equal method which compares if the current price is
     * less than or equal to another price.
     *
     * @param p Price object
     * @return boolean true if less than or equal to, false otherwise or if
     * market price
     * @see #isMarket()
     * @see #compareTo(price.Price)
     */
    public boolean lessOrEqual(Price p) {
        if (isMarket() || p.isMarket()) {
            return false;
        } else if (compareTo(p) == 0 || compareTo(p) == -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Public Less Than method which compares if the current price is less than
     * another.
     *
     * @param p Price object
     * @return boolean true if less than, false otherwise or if market price
     * @see #isMarket()
     * @see #compareTo(price.Price)
     */
    public boolean lessThan(Price p) {
        if (isMarket() || p.isMarket()) {
            return false;
        } else if (compareTo(p) == -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Public Equals method which compares if the two prices are equal.
     *
     * @param p Price object
     * @return boolean true if the two prices are equal, false otherwise or if
     * market prices
     * @see #isMarket()
     * @see #compareTo(price.Price)
     */
    public boolean equals(Price p) {
        if (isMarket() || p.isMarket()) {
            return false;
        } else if (compareTo(p) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Public Is Market method to see if the price is a market price.
     *
     * @return boolean true if market, false if not
     * @see #getMarketPrice()
     */
    public boolean isMarket() {
        if (getMarketPrice() == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Public Is Negative method to check if price is of negative value.
     *
     * @return boolean true if negative value or false if not
     * @see #getValue()
     */
    public boolean isNegative() {
        if (getValue() < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Public To String method which returns the price value in Dollars.
     *
     * @return String of price object
     * @see #getMarketPrice()
     * @see #getValue()
     */
    @Override
    public String toString() {
        // returns MKT if market price
        String market = ("MKT");
        if (getMarketPrice() == true) {
            return market;
        } else // returns string of value
        {
            long priceValue = getValue();
            double d = (double) priceValue / 100.0;
            return String.format("$%,.2f", d);
        }
    }
}
//end of file