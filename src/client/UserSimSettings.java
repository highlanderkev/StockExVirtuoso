/**
 * UserSimSettings.java
 */
package client;

import java.util.HashMap;

/**
 * This class will handle user simulated trading.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version1.0
 */
public class UserSimSettings {

    private static HashMap<String, Double> buySideBases = new HashMap<>();
    private static HashMap<String, Double> sellSideBases = new HashMap<>();
    private static HashMap<String, Integer> volumeBases = new HashMap<>();
    public static final double priceVariance = 0.05;
    public static final double volumeVariance = 0.25;

    /**
     * Private constructor.
     */
    private UserSimSettings() {
    }

    /**
     * Public method to add product data.
     *
     * @param product
     * @param bb
     * @param sb
     * @param vol
     */
    public static void addProductData(String product, double bb, double sb, int vol) {
        buySideBases.put(product, bb);
        sellSideBases.put(product, sb);
        volumeBases.put(product, vol);
    }

    /**
     * Public method to get buy price base.
     *
     * @param product
     * @return double
     */
    public static double getBuyPriceBase(String product) {
        if (!buySideBases.containsKey(product)) {
            return 0.0;
        } else {
            return buySideBases.get(product);
        }
    }

    /**
     * Public method to get sell price base.
     *
     * @param product
     * @return double
     */
    public static double getSellPriceBase(String product) {
        if (!sellSideBases.containsKey(product)) {
            return 0.0;
        } else {
            return sellSideBases.get(product);
        }
    }

    /**
     * Public method to get volume base.
     *
     * @param product
     * @return integer
     */
    public static int getVolumeBase(String product) {
        if (!volumeBases.containsKey(product)) {
            return 0;
        } else {
            return volumeBases.get(product);
        }
    }
}
//end of file