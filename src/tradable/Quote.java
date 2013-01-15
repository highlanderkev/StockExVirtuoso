/**
 * Quote.java
 */
package tradable;

import constants.BookSide;
import exceptions.ExceptionHandler;
import price.Price;

/**
 * This class represents the prices and volumes of certain stock that the user
 * is willing to BUY or SELL.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class Quote {

    /**
     * userName of user
     */
    private String user;
    /**
     * product symbol of product
     */
    private String product;
    /**
     * Buy Quote side object
     */
    private QuoteSide buyQuoteSide;
    /**
     * Sell Quote side object
     */
    private QuoteSide sellQuoteSide;

    /**
     * Construction Method which creates a new Quote object.
     *
     * @param userName
     * @param productSymbol
     * @param buyPrice
     * @param buyVolume
     * @param sellPrice
     * @param sellVolume
     * @throws Exception
     */
    public Quote(String userName, String productSymbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws Exception {
        setUserName(userName);
        setProduct(productSymbol);
        setBuyQuote(userName, productSymbol, buyPrice, buyVolume);
        setSellQuote(userName, productSymbol, sellPrice, sellVolume);
    }

    /**
     * Private Set method to set the user name.
     *
     * @param userName
     * @throws Exception
     */
    private void setUserName(String userName) throws Exception {
        if (ExceptionHandler.checkString(userName, "tradable.Quote#setUserName.")) {
            user = userName;
        }
    }

    /**
     * Public Get method to return user name.
     *
     * @return User name
     */
    public String getUserName() {
        return user;
    }

    /**
     * Private set method to set the product symbol.
     *
     * @param productSymbol
     * @throws Exception
     */
    private void setProduct(String productSymbol) throws Exception {
        if (ExceptionHandler.checkString(productSymbol, "tradable.Quote#setProduct.")) {
            product = productSymbol;
        }
    }

    /**
     * Public Get method to get the product symbol.
     *
     * @return product symbol
     */
    public String getProduct() {
        return product;
    }

    /**
     * Private set method which sets the Buy side Quote side object.
     *
     * @param userName
     * @param productSymbol
     * @param buyPrice
     * @param buyVolume
     * @throws Exception
     */
    private void setBuyQuote(String userName, String productSymbol, Price buyPrice, int buyVolume) throws Exception {
        String thisClass = "tradable.Quote#SetBuyQuote.";
        if (ExceptionHandler.checkIntNegative(buyVolume, thisClass) && ExceptionHandler.checkIntZero(buyVolume, thisClass)
                && ExceptionHandler.checkString(userName, thisClass) && ExceptionHandler.checkString(productSymbol, thisClass)
                && ExceptionHandler.checkObject(buyPrice, thisClass)) {
            buyQuoteSide = new QuoteSide(userName, productSymbol, buyPrice, buyVolume, BookSide.BUY);
        }
    }

    /**
     * Private set method which sets the Sell side Quote side object.
     *
     * @param userName
     * @param productSymbol
     * @param sellPrice
     * @param sellVolume
     * @throws Exception
     */
    private void setSellQuote(String userName, String productSymbol, Price sellPrice, int sellVolume) throws Exception {
        String thisClass = "tradable.Quote#SetSellQuote.";
        if (ExceptionHandler.checkIntNegative(sellVolume, thisClass) && ExceptionHandler.checkIntZero(sellVolume, thisClass)
                && ExceptionHandler.checkString(userName, thisClass) && ExceptionHandler.checkString(productSymbol, thisClass)
                && ExceptionHandler.checkObject(sellPrice, thisClass)) {
            sellQuoteSide = new QuoteSide(userName, productSymbol, sellPrice, sellVolume, BookSide.SELL);
        }
    }

    /**
     * Public Get Method which returns a copy of the BUY/SELL quote side object.
     *
     * @param sideIn book side BUY/SELL
     * @return copy of QuoteSide object
     * @throws Exception
     */
    public QuoteSide getQuoteSide(BookSide sideIn) throws Exception {
        if (sideIn == BookSide.BUY) {
            QuoteSide qsBuy = new QuoteSide(buyQuoteSide);
            return qsBuy;
        } else {
            QuoteSide qsSell = new QuoteSide(sellQuoteSide);
            return qsSell;
        }
    }

    /**
     * Public to String method which returns a string of all quote values.
     *
     * @return String of quote values
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s quote: %s %s - %s", user, product, buyQuoteSide.toString(), sellQuoteSide.toString()));
        return sb.toString();
    }
}
//end of file