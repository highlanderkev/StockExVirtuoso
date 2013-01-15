/*
 * MarketDataDTO.java
 */
package publishers;

import price.Price;

/**
 * This class is based upon the "Data Transfer Object" pattern and represents
 * the data values of the current market.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class MarketDataDTO {

    /**
     * Stock product symbol.
     */
    public String product;
    /**
     * Current BUY price of the stock.
     */
    public Price buyPrice; // 
    /**
     * Current BUY side volume of the stock.
     */
    public int buyVolume;
    /**
     * Current SELL side price of the stock
     */
    public Price sellPrice;
    /**
     * Current SELL side volume of the stock.
     */
    public int sellVolume;

    /**
     * Construction Method for MarketDataDTO object.
     *
     * @param productSymbol
     * @param BuyPrice
     * @param BuyVolume
     * @param SellPrice
     * @param SellVolume
     */
    public MarketDataDTO(String productSymbol, Price BuyPrice, int BuyVolume, Price SellPrice, int SellVolume) {
        product = productSymbol;
        buyPrice = BuyPrice;
        buyVolume = BuyVolume;
        sellPrice = SellPrice;
        sellVolume = SellVolume;
    }

    /**
     * To String Method which builds a String of the values held by
     * MarketDataDTO object.
     *
     * @return String of the values held by this MarketDataDTO object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Market Data: [Product]: %s, [BuyPrice]: %s at %s", product, buyPrice.toString(), buyVolume));
        sb.append(String.format(", [SellPrice]: %s at %s,", sellPrice.toString(), sellVolume));
        return sb.toString();
    }
}
//end of file
