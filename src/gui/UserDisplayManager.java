/*
 * UserDisplayManager.java
 */
package gui;

import client.User;
import client.utils.UserNotConnectedException;
import price.Price;

/**
 * This class implements the facade design pattern and interacts with the Market
 * Display class.
 *
 * @author hieldc
 * @version StockExVirtuoso.Version.1.0
 */
public class UserDisplayManager {

    private User user;
    private MarketDisplay marketDisplay;

    /**
     * Public constructor method to user and market display.
     *
     * @param u
     */
    public UserDisplayManager(User u) {
        user = u;
        marketDisplay = new MarketDisplay(u, this);
    }

    /**
     * Public method sets the market display to visible.
     *
     * @throws UserNotConnectedException
     */
    public void showMarketDisplay() throws UserNotConnectedException {
        marketDisplay.setVisible(true);
    }

    /**
     * Public method updates market data for user.
     *
     * @param product
     * @param bp
     * @param bv
     * @param sp
     * @param sv
     * @throws Exception
     */
    public void updateMarketData(String product, Price bp, int bv, Price sp, int sv) throws Exception {
        marketDisplay.updateMarketData(product, bp, bv, sp, sv);
    }

    /**
     * Public method to update the last sale.
     *
     * @param product
     * @param p
     * @param v
     * @throws Exception
     */
    public void updateLastSale(String product, Price p, int v) throws Exception {
        marketDisplay.updateLastSale(product, p, v);
    }

    /**
     * Public method to update the ticker.
     *
     * @param product
     * @param p
     * @param direction
     */
    public void updateTicker(String product, Price p, char direction) {
        marketDisplay.updateTicker(product, p, direction);
    }

    /**
     * Public method to update the market activity.
     *
     * @param activityText
     */
    public void updateMarketActivity(String activityText) {
        marketDisplay.updateMarketActivity(activityText);
    }

    /**
     * Public method to update the market state.
     *
     * @param message
     */
    public void updateMarketState(String message) {
        marketDisplay.updateMarketState(message);
    }
}
//end of file