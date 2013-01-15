/**
 * TradableDTO.java
 */
package tradable;

import constants.BookSide;
import price.Price;

/**
 * This class uses the "data transfer object" design pattern.<br> It acts as a
 * data holder for values of a tradable object.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class TradableDTO {

    /**
     * Product symbol (IBM, GOOG) of tradable.
     */
    public String product;
    /**
     * Price object of tradable.
     */
    public Price price;
    /**
     * Original Volume of tradable.
     */
    public int originalVolume;
    /**
     * Remaining Volume of tradable.
     */
    public int remainingVolume;
    /**
     * Canceled Volume of tradable.
     */
    public int cancelledVolume;
    /**
     * User id of user.
     */
    public String user;
    /**
     * Side of book/market BUY/SELL.
     */
    public BookSide side;
    /**
     * Boolean if it is a quote object.
     */
    public boolean isQuote;
    /**
     * System or tradable id.
     */
    public String id;

    /**
     * Construction Method for the Tradable Data Transfer object.
     *
     * @param productSymbol
     * @param origVolume original volume
     * @param p price
     * @param isQ is it a quote object
     * @param Id of tradable
     * @param remainVolume
     * @param s
     * @param cancelVolume
     * @param userName
     */
    public TradableDTO(String productSymbol, Price p, int origVolume, int remainVolume,
            int cancelVolume, String userName, BookSide s, boolean isQ, String Id) {
        product = productSymbol;
        price = p;
        originalVolume = origVolume;
        remainingVolume = remainVolume;
        cancelledVolume = cancelVolume;
        user = userName;
        side = s;
        isQuote = isQ;
        id = Id;
    }

    /**
     * Copy constructor for copying values of a tradable to a new TradableDTO.
     *
     * @param t tradable object
     * @throws Exception
     */
    public TradableDTO(Tradable t) throws Exception {
        product = t.getProduct();
        price = t.getPrice();
        originalVolume = t.getOriginalVolume();
        remainingVolume = t.getRemainingVolume();
        cancelledVolume = t.getCancelledVolume();
        user = t.getUser();
        side = t.getSide();
        isQuote = t.isQuote();
        id = t.getId();
    }

    /**
     * To String Method which builds a String of the values held by TradableDTO
     * object.
     *
     * @return String of the values held by this Tradable DTO object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Product: %s, Price: %s", product, price.toString()));
        sb.append(String.format(", OriginalVolume: %s, RemainingVolume: %s", originalVolume, remainingVolume));
        sb.append(String.format(", CancelledVolume: %s, User: %s", cancelledVolume, user));
        sb.append(String.format(", Side: %s, IsQuote: %b, Id: %s", side, isQuote, id));
        return sb.toString();
    }
}
//end of file
