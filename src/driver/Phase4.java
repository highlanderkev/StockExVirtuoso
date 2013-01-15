package driver;

import book.ProductService;
import client.TradableUserData;
import client.User;
import constants.BookSide;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import price.Price;
import price.PriceFactory;
import publishers.CancelMessage;
import publishers.CurrentMarketPublisher;
import publishers.FillMessage;
import publishers.LastSalePublisher;
import publishers.MessagePublisher;
import publishers.TickerPublisher;
import tradable.Order;
import tradable.Quote;
import tradable.TradableDTO;


/* YOU NEED TO ADD IMPORT STATEMENTS FOR YOUR CLASSES HERE   */
/* Mine were here but I removed them to not cause confusion. */
/* You will need to import any class used in this "main".    */

//import ....
//import ....

/**
 * You will need to make changes in this class to match your design. The class 
 * names and method names used here should be *close* to what
 * you have. It's ok to adjust this "main" to work with your classes.
 *
 * You cannot REMOVE tests, but you can feel free to make adjustments to match
 * your particular implementation. If you are unable to get this to compile and
 * run with your version of the assignment, email me the project and together we
 * can get it working.
 *
 */
public class Phase4 {

    private static User u1, u2;

    public static void main(String[] args) {

        makeTestUsers();

        doTradingScenarios("GOOG");

        doBadTests("GOOG");
    }

    private static void doBadTests(String stockSymbol) {

        try {
            System.out.println("31) Change Market State to PREOPEN then to OPEN. Market messages received");
            ProductService.getInstance().setMarketState(constants.MarketState.PREOPEN);
            ProductService.getInstance().setMarketState(constants.MarketState.OPEN);
            System.out.println();

            System.out.println("32) User " + u1.getUserName() + " cancels a non-existent order ");
            ProductService.getInstance().submitOrderCancel(stockSymbol, constants.BookSide.BUY, "ABC123");
            System.out.println("Did not catch non-existent Stock error!");

        } catch (Exception ex) {
            System.out.println("Properly caught error: " + ex.getMessage());
            System.out.println();
        }

        try {
            System.out.println("33) User " + u1.getUserName() + " cancels a non-existent quote ");
            ProductService.getInstance().submitQuoteCancel(u1.getUserName(), stockSymbol);
            System.out.println("Cancelling a non-existant quote does nothing as expected");
            System.out.println();

        } catch (Exception ex) {
            System.out.println("Caught error that should not have occurred: " + ex.getMessage());
        }
        
        try {
            System.out.println("34) Try to create a bad product");
            ProductService.getInstance().createProduct(null);
        } catch (Exception ex) {
            System.out.println("Caught error on bad product: " + ex.getMessage());
            System.out.println();
        } 
        
        try {
            System.out.println("35) User " + u1.getUserName() + " enters order on non-existent stock");
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), "X11", PriceFactory.makeLimitPrice("$641.10"), 111, constants.BookSide.BUY));
            System.out.println("Did not catch non-existent Stock error!");
        } catch (Exception ex) {
            System.out.println("Caught error on order for bad class: " + ex.getMessage());
            System.out.println();
        }
        
        try {
            System.out.println("36) User " + u1.getUserName() + " enters quote on non-existent stock");
            ProductService.getInstance().submitQuote(
                    new Quote(u1.getUserName(), "X11", PriceFactory.makeLimitPrice("$641.10"), 120, PriceFactory.makeLimitPrice("$641.15"), 150));
            System.out.println("Did not catch non-existent Stock error!");
        } catch (Exception ex) {
            System.out.println("Caught error on quote for bad class: " + ex.getMessage());
            System.out.println();
        }
    }

    private static void doTradingScenarios(String stockSymbol) {
        try {
            System.out.println("1) Check the initial Market State:\n" + ProductService.getInstance().getMarketState() + "\n");

            System.out.print("2) Create a new Stock product in our Trading System: " + stockSymbol);
            ProductService.getInstance().createProduct(stockSymbol);

            System.out.println(", then Query the ProductService for all Stock products:\n"
                    + ProductService.getInstance().getProductList() + "\n");


            System.out.println("3) Subscribe 2 test users for CurrentMarket, LastSale, Ticker & Messages");
            CurrentMarketPublisher.getInstance().subscribe(u1, stockSymbol);
            CurrentMarketPublisher.getInstance().subscribe(u2, stockSymbol);
            LastSalePublisher.getInstance().subscribe(u1, stockSymbol);
            LastSalePublisher.getInstance().subscribe(u2, stockSymbol);
            TickerPublisher.getInstance().subscribe(u1, stockSymbol);
            TickerPublisher.getInstance().subscribe(u2, stockSymbol);
            MessagePublisher.getInstance().subscribe(u1, stockSymbol);
            MessagePublisher.getInstance().subscribe(u2, stockSymbol);

            System.out.println("   then change Market State to PREOPEN and verify the Market State...");
            ProductService.getInstance().setMarketState(constants.MarketState.PREOPEN);
            System.out.println("Product State: " + ProductService.getInstance().getMarketState() + "\n");

            System.out.println("4) User " + u1.getUserName() + " enters a quote, Current Market updates received by both users: ");
            ProductService.getInstance().submitQuote(
                    new Quote(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.10"), 120, PriceFactory.makeLimitPrice("$641.15"), 150));
            System.out.println();

            System.out.println("5) Verify Quote is in Book: ");
            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
            System.out.println();

            System.out.println("6) Get MarketDataDTO for " + stockSymbol + " (Your format might vary but the data content should be the same)");
            System.out.println(ProductService.getInstance().getMarketData(stockSymbol));
            System.out.println();

            System.out.println("7) Cancel that Quote, Cancels and Current Market updated received");
            ProductService.getInstance().submitQuoteCancel(u1.getUserName(), stockSymbol);
            System.out.println();

            System.out.println("8) Verify Quote is NOT in Book: ");
            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
            System.out.println();

            System.out.println("9) Get MarketDataDTO for " + stockSymbol);
            System.out.println(ProductService.getInstance().getMarketData(stockSymbol));
            System.out.println();


            System.out.println("10) User " + u2.getUserName() + " enters a quote, Current Market received by both users: ");
            ProductService.getInstance().submitQuote(
                    new Quote(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.10"), 120, PriceFactory.makeLimitPrice("$641.15"), 150));
            System.out.println();

            System.out.println("11) User " + u1.getUserName() + " enters 5 BUY orders, Current Market updates received (10) by both users for each order: ");
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.10"), 111, constants.BookSide.BUY));
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.11"), 222, constants.BookSide.BUY));
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.12"), 333, constants.BookSide.BUY));
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.13"), 444, constants.BookSide.BUY));
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.14"), 555, constants.BookSide.BUY));

            System.out.println();

            System.out.println("12) Verify Book: ");
            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
            System.out.println();


            System.out.println("13) User " + u2.getUserName() + " enters several 5 Sell orders - no Current Market received - does not improve the market: ");
            ProductService.getInstance().submitOrder(
                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.16"), 111, constants.BookSide.SELL));
            ProductService.getInstance().submitOrder(
                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.17"), 222, constants.BookSide.SELL));
            ProductService.getInstance().submitOrder(
                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.18"), 333, constants.BookSide.SELL));
            ProductService.getInstance().submitOrder(
                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.19"), 444, constants.BookSide.SELL));
            ProductService.getInstance().submitOrder(
                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.20"), 555, constants.BookSide.SELL));
            System.out.println();

            System.out.println("14) Verify Book: ");
            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
            System.out.println();


            System.out.println("15) User " + u2.getUserName() + " enters a BUY order that is tradable (Current Market received), but won't trade as market is in PREOPEN: ");
            ProductService.getInstance().submitOrder(
                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.15"), 105, constants.BookSide.BUY));
            System.out.println();

            System.out.println("16) Change Market State to OPEN State...Trade should occur.");
            System.out.println("    Both users should get Market Message, Fill Messages, Current Market, Last Sale & Tickers.");
            ProductService.getInstance().setMarketState(constants.MarketState.OPEN);
            System.out.println();

            System.out.println("17) Verify Book after the trade: ");
            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
            System.out.println();

            System.out.println("18) User " + u1.getUserName() + " enters a big MKT BUY order to trade with all the SELL side:");
            System.out.println("    Both users should get many Fill Messages, as well as Current Market, Last Sale & Tickers - and a cancel for the unfilled volume");
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeMarketPrice(), 1750, constants.BookSide.BUY));
            System.out.println();

            System.out.println("19) Verify Book: ");
            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
            System.out.println();

            System.out.println("20) Get Orders with Remaining Quantity: ");
            ArrayList<TradableDTO> ords = ProductService.getInstance().getOrdersWithRemainingQty(u1.getUserName(), stockSymbol);
            for (TradableDTO dto : ords) {
                System.out.println(dto);
            }
            System.out.println();

            System.out.println("21) Change Market State to CLOSED State...Both users should get a Market message, many Cancel Messages, and a Current Market Update.");
            ProductService.getInstance().setMarketState(constants.MarketState.CLOSED);
            System.out.println();

            System.out.println("22) Verify Book: ");
            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
            System.out.println();


            System.out.println("23) Change Market State to PREOPEN then to OPEN. Market messages received");
            ProductService.getInstance().setMarketState(constants.MarketState.PREOPEN);
            ProductService.getInstance().setMarketState(constants.MarketState.OPEN);
            System.out.println();

            System.out.println("24) User " + u1.getUserName() + " enters a BUY order, Current Market received");
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice(64130), 369, constants.BookSide.BUY));
            System.out.println();

            System.out.println("25) User " + u2.getUserName() + " enters a SELL order, users receive Fill Messages, as well as Current Market, Last Sale & Tickers");
            ProductService.getInstance().submitOrder(
                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice(64130), 369, constants.BookSide.SELL));
            System.out.println();

            System.out.println("26) User " + u1.getUserName() + " enters a MKT BUY order, cancelled as there is no market");
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeMarketPrice(), 456, constants.BookSide.BUY));
            System.out.println();

            System.out.println("27) User " + u1.getUserName() + " enters a BUY order, Current Market received");
            ProductService.getInstance().submitOrder(
                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("641.1"), 151, constants.BookSide.BUY));
            System.out.println();

            System.out.println("28) User " + u2.getUserName() + " enters a SELL order, users receive Fill Messages, as well as Current Market, Last Sale & Tickers");
            ProductService.getInstance().submitOrder(
                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice(64110), 51, constants.BookSide.SELL));
            System.out.println();
            
            System.out.println("29) Change Market State to CLOSED State...Both users should get a Market message, many Cancel Messages, and a Current Market Update.");
            ProductService.getInstance().setMarketState(constants.MarketState.CLOSED);
            System.out.println();

            System.out.println("30) Verify Book: ");
            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
            System.out.println();
            


        } catch (Exception ex) {
            Logger.getLogger(Phase4.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    private static void printOutBD(String[][] bd) {
        String[] buy = bd[0];
        String[] sell = bd[1];
        System.out.println("Buy Side:");
        for (String s : buy) {
            System.out.println("\t" + s);
        }
        System.out.println("Sell Side:");
        for (String s : sell) {
            System.out.println("\t" + s);
        }
    }

    private static void makeTestUsers() {
        u1 = new UserImpl("REX");
        u2 = new UserImpl("ANN");

    }
////////////////////////////////////////////////////////////////////////

    static class UserImpl implements User {

        private String uname;

        public UserImpl(String u) {
            uname = u;
        }

        @Override
        public String getUserName() {
            return uname;
        }

        @Override
        public void acceptLastSale(String product, Price p, int v) {
            System.out.println("User " + getUserName() + " Received Last Sale for " + product + " " + v + "@" + p);
        }

        @Override
        public void acceptMessage(FillMessage fm) {
            System.out.println("User " + getUserName() + " Received Fill Message: " + fm);
        }

        @Override
        public void acceptMessage(CancelMessage cm) {
            System.out.println("User " + getUserName() + " Received Cancel Message: " + cm);
        }

        @Override
        public void acceptMarketMessage(String message) {
            System.out.println("User " + getUserName() + " Received Market Message: " + message);
        }

        @Override
        public void acceptTicker(String product, Price p, char direction) {
            System.out.println("User " + getUserName() + " Received Ticker for " + product + " " + p + " " + direction);
        }

        @Override
        public void acceptCurrentMarket(String product, Price bp, int bv, Price sp, int sv) {
            System.out.println("User " + getUserName() + " Received Current Market for " + product + " " + bv + "@" + bp + " - " + sv + "@" + sp);
        }

        @Override
        public void connect() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void disConnect() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void showMarketDisplay() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String submitOrder(String product, Price price, int volume, BookSide side) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void submitOrderCancel(String product, BookSide side, String orderId) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void submitQuote(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void submitQuoteCancel(String product) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void subscribeCurrentMarket(String product) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void subscribeLastSale(String product) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void subscribeMessages(String product) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void subscribeTicker(String product) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Price getAllStockValue() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Price getAccountCosts() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Price getNetAccountValue() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String[][] getBookDepth(String product) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getMarketState() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ArrayList<TradableUserData> getOrderIds() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ArrayList<String> getProductList() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Price getStockPositionValue(String sym) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getStockPositionVolume(String product) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ArrayList<String> getHoldings() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}


/*
1) Check the initial Market State:
CLOSED

2) Create a new Stock product in our Trading System: GOOG, then Query the ProductService for all Stock products:
[GOOG]

3) Subscribe 2 test users for CurrentMarket, LastSale, Ticker & Messages
   then change Market State to PREOPEN and verify the Market State...
User REX Received Market Message: PREOPEN
User ANN Received Market Message: PREOPEN
Product State: PREOPEN

4) User REX enters a quote, Current Market updates received by both users: 
User ANN Received Current Market for GOOG 120@$641.10 - 150@$641.15
User REX Received Current Market for GOOG 120@$641.10 - 150@$641.15

5) Verify Quote is in Book: 
Buy Side:
	$641.10 x 120
Sell Side:
	$641.15 x 150

6) Get MarketDataDTO for GOOG (Your format might vary but the data content should be the same)
Market Data [GOOG] 120@$641.10 x 150@$641.15

7) Cancel that Quote, Cancels and Current Market updated received
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.10, Volume: 120, Details: Quote BUY-Side Cancelled, Side: BUY, Id: REXGOOG105982873160140
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.15, Volume: 150, Details: Quote SELL-Side Cancelled, Side: SELL, Id: REXGOOG105982873185932
User ANN Received Current Market for GOOG 0@$0.00 - 0@$0.00
User REX Received Current Market for GOOG 0@$0.00 - 0@$0.00

8) Verify Quote is NOT in Book: 
Buy Side:
	<Empty>
Sell Side:
	<Empty>

9) Get MarketDataDTO for GOOG
Market Data [GOOG] 0@$0.00 x 0@$0.00

10) User ANN enters a quote, Current Market received by both users: 
User ANN Received Current Market for GOOG 120@$641.10 - 150@$641.15
User REX Received Current Market for GOOG 120@$641.10 - 150@$641.15

11) User REX enters 5 BUY orders, Current Market updates received (10) by both users for each order: 
User ANN Received Current Market for GOOG 231@$641.10 - 150@$641.15
User REX Received Current Market for GOOG 231@$641.10 - 150@$641.15
User ANN Received Current Market for GOOG 222@$641.11 - 150@$641.15
User REX Received Current Market for GOOG 222@$641.11 - 150@$641.15
User ANN Received Current Market for GOOG 333@$641.12 - 150@$641.15
User REX Received Current Market for GOOG 333@$641.12 - 150@$641.15
User ANN Received Current Market for GOOG 444@$641.13 - 150@$641.15
User REX Received Current Market for GOOG 444@$641.13 - 150@$641.15
User ANN Received Current Market for GOOG 555@$641.14 - 150@$641.15
User REX Received Current Market for GOOG 555@$641.14 - 150@$641.15

12) Verify Book: 
Buy Side:
	$641.14 x 555
	$641.13 x 444
	$641.12 x 333
	$641.11 x 222
	$641.10 x 231
Sell Side:
	$641.15 x 150

13) User ANN enters several 5 Sell orders - no Current Market received - does not improve the market: 

14) Verify Book: 
Buy Side:
	$641.14 x 555
	$641.13 x 444
	$641.12 x 333
	$641.11 x 222
	$641.10 x 231
Sell Side:
	$641.15 x 150
	$641.16 x 111
	$641.17 x 222
	$641.18 x 333
	$641.19 x 444
	$641.20 x 555

15) User ANN enters a BUY order that is tradable (Current Market received), but won't trade as market is in PREOPEN: 
User ANN Received Current Market for GOOG 105@$641.15 - 150@$641.15
User REX Received Current Market for GOOG 105@$641.15 - 150@$641.15

16) Change Market State to OPEN State...Trade should occur.
    Both users should get Market Message, Fill Messages, Current Market, Last Sale & Tickers.
User REX Received Market Message: OPEN
User ANN Received Market Message: OPEN
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.15, Fill Volume: 105, Details: leaving 0, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.15, Fill Volume: 105, Details: leaving 45, Side: SELL
User ANN Received Current Market for GOOG 555@$641.14 - 45@$641.15
User REX Received Current Market for GOOG 555@$641.14 - 45@$641.15
User ANN Received Last Sale for GOOG 105@$641.15
User REX Received Last Sale for GOOG 105@$641.15
User ANN Received Ticker for GOOG $641.15  
User REX Received Ticker for GOOG $641.15  

17) Verify Book after the trade: 
Buy Side:
	$641.14 x 555
	$641.13 x 444
	$641.12 x 333
	$641.11 x 222
	$641.10 x 231
Sell Side:
	$641.15 x 45
	$641.16 x 111
	$641.17 x 222
	$641.18 x 333
	$641.19 x 444
	$641.20 x 555

18) User REX enters a big MKT BUY order to trade with all the SELL side:
    Both users should get many Fill Messages, as well as Current Market, Last Sale & Tickers - and a cancel for the unfilled volume
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.19, Fill Volume: 444, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.15, Fill Volume: 45, Details: leaving 1705, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.16, Fill Volume: 111, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.16, Fill Volume: 111, Details: leaving 1594, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.20, Fill Volume: 555, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.20, Fill Volume: 555, Details: leaving 40, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.17, Fill Volume: 222, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.17, Fill Volume: 222, Details: leaving 1372, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.15, Fill Volume: 45, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.18, Fill Volume: 333, Details: leaving 1039, Side: BUY
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.19, Fill Volume: 444, Details: leaving 595, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.18, Fill Volume: 333, Details: leaving 0, Side: SELL
User ANN Received Current Market for GOOG 555@$641.14 - 0@$0.00
User REX Received Current Market for GOOG 555@$641.14 - 0@$0.00
User ANN Received Last Sale for GOOG 1710@$641.15
User REX Received Last Sale for GOOG 1710@$641.15
User ANN Received Ticker for GOOG $641.15 =
User REX Received Ticker for GOOG $641.15 =
User REX Received Cancel Message: User: REX, Product: GOOG, Price: MKT, Volume: 40, Details: Cancelled, Side: BUY, Id: REXGOOGMKT105982897051413

19) Verify Book: 
Buy Side:
	$641.14 x 555
	$641.13 x 444
	$641.12 x 333
	$641.11 x 222
	$641.10 x 231
Sell Side:
	<Empty>

20) Get Orders with Remaining Quantity: 
Product: GOOG, Price: $641.14, OriginalVolume: 555, RemainingVolume: 555, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.14105982888527284
Product: GOOG, Price: $641.13, OriginalVolume: 444, RemainingVolume: 444, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.13105982887739275
Product: GOOG, Price: $641.12, OriginalVolume: 333, RemainingVolume: 333, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.12105982886980138
Product: GOOG, Price: $641.11, OriginalVolume: 222, RemainingVolume: 222, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.11105982886213301
Product: GOOG, Price: $641.10, OriginalVolume: 111, RemainingVolume: 111, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.10105982885392185

21) Change Market State to CLOSED State...Both users should get a Market message, many Cancel Messages, and a Current Market Update.
User REX Received Market Message: CLOSED
User ANN Received Market Message: CLOSED
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.14, Volume: 555, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.14105982888527284
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.13, Volume: 444, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.13105982887739275
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.12, Volume: 333, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.12105982886980138
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.11, Volume: 222, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.11105982886213301
User ANN Received Cancel Message: User: ANN, Product: GOOG, Price: $641.10, Volume: 120, Details: Quote BUY-Side Cancelled, Side: BUY, Id: ANNGOOG105982883377314
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.10, Volume: 111, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.10105982885392185
User ANN Received Current Market for GOOG 0@$0.00 - 0@$0.00
User REX Received Current Market for GOOG 0@$0.00 - 0@$0.00

22) Verify Book: 
Buy Side:
	<Empty>
Sell Side:
	<Empty>

23) Change Market State to PREOPEN then to OPEN. Market messages received
User REX Received Market Message: PREOPEN
User ANN Received Market Message: PREOPEN
User REX Received Market Message: OPEN
User ANN Received Market Message: OPEN

24) User REX enters a BUY order, Current Market received
User ANN Received Current Market for GOOG 369@$641.30 - 0@$0.00
User REX Received Current Market for GOOG 369@$641.30 - 0@$0.00

25) User ANN enters a SELL order, users receive Fill Messages, as well as Current Market, Last Sale & Tickers
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.30, Fill Volume: 369, Details: leaving 0, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.30, Fill Volume: 369, Details: leaving 0, Side: SELL
User ANN Received Current Market for GOOG 0@$0.00 - 0@$0.00
User REX Received Current Market for GOOG 0@$0.00 - 0@$0.00
User ANN Received Last Sale for GOOG 369@$641.30
User REX Received Last Sale for GOOG 369@$641.30
User ANN Received Ticker for GOOG $641.30 ↑
User REX Received Ticker for GOOG $641.30 ↑

26) User REX enters a MKT BUY order, cancelled as there is no market
User REX Received Cancel Message: User: REX, Product: GOOG, Price: MKT, Volume: 456, Details: Cancelled, Side: BUY, Id: REXGOOGMKT105982905228309

27) User REX enters a BUY order, Current Market received
User ANN Received Current Market for GOOG 151@$641.10 - 0@$0.00
User REX Received Current Market for GOOG 151@$641.10 - 0@$0.00

28) User ANN enters a SELL order, users receive Fill Messages, as well as Current Market, Last Sale & Tickers
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.10, Fill Volume: 51, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.10, Fill Volume: 51, Details: leaving 100, Side: BUY
User ANN Received Current Market for GOOG 100@$641.10 - 0@$0.00
User REX Received Current Market for GOOG 100@$641.10 - 0@$0.00
User ANN Received Last Sale for GOOG 51@$641.10
User REX Received Last Sale for GOOG 51@$641.10
User ANN Received Ticker for GOOG $641.10 ↓
User REX Received Ticker for GOOG $641.10 ↓

29) Change Market State to CLOSED State...Both users should get a Market message, many Cancel Messages, and a Current Market Update.
User REX Received Market Message: CLOSED
User ANN Received Market Message: CLOSED
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.10, Volume: 100, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.10105982905384602
User ANN Received Current Market for GOOG 0@$0.00 - 0@$0.00
User REX Received Current Market for GOOG 0@$0.00 - 0@$0.00

30) Verify Book: 
Buy Side:
	<Empty>
Sell Side:
	<Empty>

31) Change Market State to PREOPEN then to OPEN. Market messages received
User REX Received Market Message: PREOPEN
User ANN Received Market Message: PREOPEN
User REX Received Market Message: OPEN
User ANN Received Market Message: OPEN

32) User REX cancels a non-existent order 
Properly caught error: Cannot find order ABC123

33) User REX cancels a non-existent quote 
Cancelling a non-existant quote does nothing as expected

34) Try to create a bad product
Caught error on bad product: Null or empty Stock Symbol passed to ProductService createProduct.

35) User REX enters order on non-existent stock
Caught error on order for bad class: Product X11 does not exist

36) User REX enters quote on non-existent stock
Caught error on quote for bad class: Product X11 does not exist

BUILD SUCCESSFUL (total time: 1 second)

 */