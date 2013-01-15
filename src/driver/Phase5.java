package driver;

import book.ProductService;
import client.User;
import client.UserImpl;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import price.PriceFactory;


public class Phase5 {

    private static ArrayList<User> users = new ArrayList<>();
    private static int testCount = 1;

    public static void main(String[] args) {
        
        try {
            System.out.println("A) Add some stocks to the Trading System: IBM, CBOE, GOOG, AAPL, GE, T");
            String[] products = {"IBM", "CBOE", "GOOG", "AAPL", "GE", "T"};
            for (String prod : products) {
                ProductService.getInstance().createProduct(prod);
            }
            System.out.println();

            System.out.println("B) Create 3 users: REX, ANN, RAJ");
            
            users.add(new UserImpl("REX"));
            users.add(new UserImpl("ANN"));
            users.add(new UserImpl("RAJ"));
            System.out.println();

            System.out.println("C) Connect users REX, ANN, RAJ to the trading system");
            for (int i = 0; i < 3; i++) {
                users.get(i).connect();
            }
            System.out.println();

            System.out.println("D) Subscribe users REX, ANN, RAJ to Current Market, Last Sale, Ticker, and Messages for all Stocks");
            for (int i = 0; i < 3; i++) {
                for (String prod : products) {
                    users.get(i).subscribeCurrentMarket(prod);
                    users.get(i).subscribeLastSale(prod);
                    users.get(i).subscribeMessages(prod);
                    users.get(i).subscribeTicker(prod);
                }
                users.get(i).showMarketDisplay();
            }
            System.out.println();

            System.out.println("E) User REX queries market state");
            System.out.println(users.get(0).getUserName() + ": Market State Query: " + users.get(0).getMarketState());
            System.out.println();

            System.out.println("F) Put the market in PREOPEN state");
            ProductService.getInstance().setMarketState(constants.MarketState.PREOPEN);
            System.out.println();

            System.out.println("G) User ANN queries market state");
            System.out.println(users.get(1).getUserName() + ": Market State Query: " + users.get(0).getMarketState());
            System.out.println();

            System.out.println("H) Put the market in OPEN state");
            ProductService.getInstance().setMarketState(constants.MarketState.OPEN);
            System.out.println();

            System.out.println("I) User RAJ queries market state");
            System.out.println(users.get(2).getUserName() + ": Market State Query: " + users.get(0).getMarketState());
            System.out.println();
            
            runTests("GOOG");
            runTests("IBM");
            
        } catch (Exception ex) {
            Logger.getLogger(Phase5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void runTests(String stock) {
        try {


            System.out.println(testCount++ + ".1) User REX submits an order for " + stock + ", BUY 100@40.00");
            String rexO1 = users.get(0).submitOrder(stock, PriceFactory.makeLimitPrice("$40.00"), 100, constants.BookSide.BUY);
            System.out.println();

            System.out.println(testCount++ + ".2) User ANN submits a quote for " + stock + ", 100@40.00 x 100@40.50");
            users.get(1).submitQuote(stock, PriceFactory.makeLimitPrice("$40.00"), 100, PriceFactory.makeLimitPrice("$40.50"), 100);
            System.out.println();


            System.out.println(testCount++ + ".3) User RAJ submits an Order for " + stock + ", SELL 135@40.50");
            String o2 = users.get(2).submitOrder(stock, PriceFactory.makeLimitPrice("$40.50"), 135, constants.BookSide.SELL);
            System.out.println();

            System.out.println(testCount++ + ".4) User REX does a Book Depth query for " + stock);
            System.out.println("Book Depth: " + users.get(0).getBookDepth(stock)[0][0] + " -- " + users.get(0).getBookDepth(stock)[1][0]);
            System.out.println();

            System.out.println(testCount++ + ".5) User REX does a query for their orders with remaining quantity for " + stock);
            System.out.println(users.get(0).getUserName() + " Orders With Remaining Qty: " + users.get(0).getOrdersWithRemainingQty(stock));
            System.out.println();

            System.out.println(testCount++ + ".6) User ANN does a query for their orders with remaining quantity for GE " + stock);
            System.out.println(users.get(1).getUserName() + " Orders With Remaining Qty: " + users.get(1).getOrdersWithRemainingQty(stock));
            System.out.println();

            System.out.println(testCount++ + ".7) User RAJ does a query for their orders with remaining quantity for GE " + stock);
            System.out.println(users.get(2).getUserName() + " Orders With Remaining Qty: " + users.get(2).getOrdersWithRemainingQty(stock));
            System.out.println();

            System.out.println(testCount++ + ".8) User REX cancels their order");
            users.get(0).submitOrderCancel(stock, constants.BookSide.BUY, rexO1);
            System.out.println();

            System.out.println(testCount++ + ".9) User ANN cancels their quote");
            users.get(1).submitQuoteCancel(stock);
            System.out.println();

            System.out.println(testCount++ + ".10) User RAJ cancels their order");
            users.get(2).submitOrderCancel(stock, constants.BookSide.SELL, o2);
            System.out.println();

            System.out.println(testCount++ + ".11) Display position values for all users");
            for (User u : users) {
                System.out.print(u.getUserName() + " Stock Value: " + u.getAllStockValue());
                System.out.print(", Account Costs: " + u.getAccountCosts());
                System.out.println(", Net Value: " + u.getNetAccountValue());
            }
            System.out.println();

            System.out.println(testCount++ + ".12) User REX enters an order for " + stock + ", BUY 100@$10.00");
            users.get(0).submitOrder(stock, PriceFactory.makeLimitPrice("$10.00"), 100, constants.BookSide.BUY);
            System.out.println();

            System.out.println(testCount++ + ".13) User ANN enters a quote for " + stock + ", 100@$10.00 x 100@10.10");
            users.get(1).submitQuote(stock, PriceFactory.makeLimitPrice("$10.00"), 100, PriceFactory.makeLimitPrice("$10.10"), 100);
            System.out.println();

            System.out.println(testCount++ + ".14) User RAJ enters an order for " + stock + ", SELL 150@$10.00 - results in a trade");
            users.get(2).submitOrder(stock, PriceFactory.makeLimitPrice("$10.00"), 150, constants.BookSide.SELL);
            System.out.println();

            System.out.println(testCount++ + ".15) User REX does a Book Depth query for " + stock);
            System.out.println("IBM Book Depth: " + users.get(0).getBookDepth(stock)[0][0] + " -- " + users.get(0).getBookDepth(stock)[1][0]);
            System.out.println();

            System.out.println(testCount++ + ".16) User REX enters a market order for " + stock + ", SELL 75@MKT - results in a trade");
            users.get(0).submitOrder(stock, PriceFactory.makeMarketPrice(), 75, constants.BookSide.BUY);
            System.out.println();

            System.out.println(testCount++ + ".17) User ANN does a Book Depth query for " + stock);
            System.out.println("IBM Book Depth: " + users.get(1).getBookDepth(stock)[0][0] + " -- " + users.get(1).getBookDepth(stock)[1][0]);
            System.out.println();

            System.out.println(testCount++ + ".18) User ANN cancels her quote for "+ stock);
            users.get(1).submitQuoteCancel(stock);
            System.out.println();

            System.out.println(testCount++ + ".19) Show stock holdings for all users");
            System.out.println(users.get(0).getUserName() + " Holdings: " + users.get(0).getHoldings());
            System.out.println(users.get(1).getUserName() + " Holdings: " + users.get(1).getHoldings());
            System.out.println(users.get(2).getUserName() + " Holdings: " + users.get(2).getHoldings());
            System.out.println();

            System.out.println(testCount++ + ".20) Show order Id's  for all users");
            System.out.println(users.get(0).getUserName() + " Orders: " + users.get(0).getOrderIds());
            System.out.println(users.get(1).getUserName() + " Orders: " + users.get(1).getOrderIds());
            System.out.println(users.get(2).getUserName() + " Orders: " + users.get(2).getOrderIds());
            System.out.println();


            System.out.println(testCount++ + ".21) Show positions for all users");
            for (User u : users) {
                System.out.print(u.getUserName() + " Stock Value: " + u.getAllStockValue());
                System.out.print(", Account Costs: " + u.getAccountCosts());
                System.out.println(", Net Value: " + u.getNetAccountValue());
            }
            System.out.println();
            

        } catch (Exception ex) {
            Logger.getLogger(Phase5.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

/*

B) Create 3 users: REX, ANN, RAJ

C) Connect users REX, ANN, RAJ to the trading system

D) Subscribe users REX, ANN, RAJ to Current Market, Last Sale, Ticker, and Messages for all Stocks
REX's Market Display is in 'Visible' mode
ANN's Market Display is in 'Visible' mode
RAJ's Market Display is in 'Visible' mode

E) User REX queries market state
REX: Market State Query: CLOSED

F) Put the market in PREOPEN state
[RAJ's GUI], Update Market State: PREOPEN
[REX's GUI], Update Market State: PREOPEN
[ANN's GUI], Update Market State: PREOPEN

G) User ANN queries market state
ANN: Market State Query: PREOPEN

H) Put the market in OPEN state
[RAJ's GUI], Update Market State: OPEN
[REX's GUI], Update Market State: OPEN
[ANN's GUI], Update Market State: OPEN

I) User RAJ queries market state
RAJ: Market State Query: OPEN

1.1) User REX submits an order for GOOG, BUY 100@40.00
[RAJ's GUI], Update Market Data: GOOG: $40.00@100 -- $0.00@0
[ANN's GUI], Update Market Data: GOOG: $40.00@100 -- $0.00@0
[REX's GUI], Update Market Data: GOOG: $40.00@100 -- $0.00@0

2.2) User ANN submits a quote for GOOG, 100@40.00 x 100@40.50
[RAJ's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@100
[ANN's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@100
[REX's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@100

3.3) User RAJ submits an Order for GOOG, SELL 135@40.50
[RAJ's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@235
[ANN's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@235
[REX's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@235

4.4) User REX does a Book Depth query for GOOG
Book Depth: $40.00 x 200 -- $40.50 x 235

5.5) User REX does a query for their orders with remaining quantity for GOOG
REX Orders With Remaining Qty: [Product: GOOG, Price: $40.00, OriginalVolume: 100, RemainingVolume: 100, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$40.001057189840301268]

6.6) User ANN does a query for their orders with remaining quantity for GE GOOG
ANN Orders With Remaining Qty: []

7.7) User RAJ does a query for their orders with remaining quantity for GE GOOG
RAJ Orders With Remaining Qty: [Product: GOOG, Price: $40.50, OriginalVolume: 135, RemainingVolume: 135, CancelledVolume: 0, User: RAJ, Side: SELL, IsQuote: false, Id: RAJGOOG$40.501057189848358442]

8.8) User REX cancels their order
[REX's GUI], Update Market Activity: {2012-10-29 09:53:52.641} Cancel Message: BUY 100 GOOG at $40.00 BUY Order Cancelled [Tradable Id: REXGOOG$40.001057189840301268]
[RAJ's GUI], Update Market Data: GOOG: $40.00@100 -- $40.50@235
[ANN's GUI], Update Market Data: GOOG: $40.00@100 -- $40.50@235
[REX's GUI], Update Market Data: GOOG: $40.00@100 -- $40.50@235

9.9) User ANN cancels their quote
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.688} Cancel Message: BUY 100 GOOG at $40.00 Quote BUY-Side Cancelled [Tradable Id: ANNGOOG1057189845863913]
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.689} Cancel Message: SELL 100 GOOG at $40.50 Quote SELL-Side Cancelled [Tradable Id: ANNGOOG1057189845885471]
[RAJ's GUI], Update Market Data: GOOG: $0.00@0 -- $40.50@135
[ANN's GUI], Update Market Data: GOOG: $0.00@0 -- $40.50@135
[REX's GUI], Update Market Data: GOOG: $0.00@0 -- $40.50@135

10.10) User RAJ cancels their order
[RAJ's GUI], Update Market Activity: {2012-10-29 09:53:52.691} Cancel Message: SELL 135 GOOG at $40.50 SELL Order Cancelled [Tradable Id: RAJGOOG$40.501057189848358442]
[RAJ's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0
[ANN's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0
[REX's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0

11.11) Display position values for all users
REX Stock Value: $0.00, Account Costs: $0.00, Net Value: $0.00
ANN Stock Value: $0.00, Account Costs: $0.00, Net Value: $0.00
RAJ Stock Value: $0.00, Account Costs: $0.00, Net Value: $0.00

12.12) User REX enters an order for GOOG, BUY 100@$10.00
[RAJ's GUI], Update Market Data: GOOG: $10.00@100 -- $0.00@0
[ANN's GUI], Update Market Data: GOOG: $10.00@100 -- $0.00@0
[REX's GUI], Update Market Data: GOOG: $10.00@100 -- $0.00@0

13.13) User ANN enters a quote for GOOG, 100@$10.00 x 100@10.10
[RAJ's GUI], Update Market Data: GOOG: $10.00@200 -- $10.10@100
[ANN's GUI], Update Market Data: GOOG: $10.00@200 -- $10.10@100
[REX's GUI], Update Market Data: GOOG: $10.00@200 -- $10.10@100

14.14) User RAJ enters an order for GOOG, SELL 150@$10.00 - results in a trade
[REX's GUI], Update Market Activity: {2012-10-29 09:53:52.697} Fill Message: BUY 100 GOOG at $10.00 leaving 0 [Tradable Id: REXGOOG$10.001057189905195587]
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.698} Fill Message: BUY 50 GOOG at $10.00 leaving 50 [Tradable Id: ANNGOOG1057189906532932]
[RAJ's GUI], Update Market Activity: {2012-10-29 09:53:52.698} Fill Message: SELL 150 GOOG at $10.00 leaving 0 [Tradable Id: RAJGOOG$10.001057189908078924]
[RAJ's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@100
[ANN's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@100
[REX's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@100
[RAJ's GUI], Update Last Sale: GOOG: $10.00@150
[ANN's GUI], Update Last Sale: GOOG: $10.00@150
[REX's GUI], Update Last Sale: GOOG: $10.00@150
[RAJ's GUI], Update Ticker: GOOG: $10.00  
[ANN's GUI], Update Ticker: GOOG: $10.00  
[REX's GUI], Update Ticker: GOOG: $10.00  

15.15) User REX does a Book Depth query for GOOG
IBM Book Depth: $10.00 x 50 -- $10.10 x 100

16.16) User REX enters a market order for GOOG, SELL 75@MKT - results in a trade
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.702} Fill Message: SELL 75 GOOG at $10.10 leaving 25 [Tradable Id: ANNGOOG1057189906541016]
[REX's GUI], Update Market Activity: {2012-10-29 09:53:52.702} Fill Message: BUY 75 GOOG at $10.10 leaving 0 [Tradable Id: REXGOOGMKT1057189914048470]
[RAJ's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@25
[ANN's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@25
[REX's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@25
[RAJ's GUI], Update Last Sale: GOOG: $10.10@75
[ANN's GUI], Update Last Sale: GOOG: $10.10@75
[REX's GUI], Update Last Sale: GOOG: $10.10@75
[RAJ's GUI], Update Ticker: GOOG: $10.10 ↑
[ANN's GUI], Update Ticker: GOOG: $10.10 ↑
[REX's GUI], Update Ticker: GOOG: $10.10 ↑

17.17) User ANN does a Book Depth query for GOOG
IBM Book Depth: $10.00 x 50 -- $10.10 x 25

18.18) User ANN cancels her quote for GOOG
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.705} Cancel Message: BUY 50 GOOG at $10.00 Quote BUY-Side Cancelled [Tradable Id: ANNGOOG1057189906532932]
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.706} Cancel Message: SELL 25 GOOG at $10.10 Quote SELL-Side Cancelled [Tradable Id: ANNGOOG1057189906541016]
[RAJ's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0
[ANN's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0
[REX's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0

19.19) Show stock holdings for all users
REX Holdings: [GOOG]
ANN Holdings: [GOOG]
RAJ Holdings: [GOOG]

20.20) Show order Id's  for all users
REX Orders: [User REX, BUY GOOG (REXGOOG$40.001057189840301268), User REX, BUY GOOG (REXGOOG$10.001057189905195587), User REX, BUY GOOG (REXGOOGMKT1057189914048470)]
ANN Orders: []
RAJ Orders: [User RAJ, SELL GOOG (RAJGOOG$40.501057189848358442), User RAJ, SELL GOOG (RAJGOOG$10.001057189908078924)]

21.21) Show positions for all users
REX Stock Value: $1,767.50, Account Costs: $-1,757.50, Net Value: $10.00
ANN Stock Value: $-252.50, Account Costs: $257.50, Net Value: $5.00
RAJ Stock Value: $-1,515.00, Account Costs: $1,500.00, Net Value: $-15.00

22.1) User REX submits an order for IBM, BUY 100@40.00
[RAJ's GUI], Update Market Data: IBM: $40.00@100 -- $0.00@0
[ANN's GUI], Update Market Data: IBM: $40.00@100 -- $0.00@0
[REX's GUI], Update Market Data: IBM: $40.00@100 -- $0.00@0

23.2) User ANN submits a quote for IBM, 100@40.00 x 100@40.50
[RAJ's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@100
[ANN's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@100
[REX's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@100

24.3) User RAJ submits an Order for IBM, SELL 135@40.50
[RAJ's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@235
[ANN's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@235
[REX's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@235

25.4) User REX does a Book Depth query for IBM
Book Depth: $40.00 x 200 -- $40.50 x 235

26.5) User REX does a query for their orders with remaining quantity for IBM
REX Orders With Remaining Qty: [Product: IBM, Price: $40.00, OriginalVolume: 100, RemainingVolume: 100, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXIBM$40.001057189920510762]

27.6) User ANN does a query for their orders with remaining quantity for GE IBM
ANN Orders With Remaining Qty: []

28.7) User RAJ does a query for their orders with remaining quantity for GE IBM
RAJ Orders With Remaining Qty: [Product: IBM, Price: $40.50, OriginalVolume: 135, RemainingVolume: 135, CancelledVolume: 0, User: RAJ, Side: SELL, IsQuote: false, Id: RAJIBM$40.501057189923143491]

29.8) User REX cancels their order
[REX's GUI], Update Market Activity: {2012-10-29 09:53:52.713} Cancel Message: BUY 100 IBM at $40.00 BUY Order Cancelled [Tradable Id: REXIBM$40.001057189920510762]
[RAJ's GUI], Update Market Data: IBM: $40.00@100 -- $40.50@235
[ANN's GUI], Update Market Data: IBM: $40.00@100 -- $40.50@235
[REX's GUI], Update Market Data: IBM: $40.00@100 -- $40.50@235

30.9) User ANN cancels their quote
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.715} Cancel Message: BUY 100 IBM at $40.00 Quote BUY-Side Cancelled [Tradable Id: ANNIBM1057189921779969]
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.715} Cancel Message: SELL 100 IBM at $40.50 Quote SELL-Side Cancelled [Tradable Id: ANNIBM1057189921787668]
[RAJ's GUI], Update Market Data: IBM: $0.00@0 -- $40.50@135
[ANN's GUI], Update Market Data: IBM: $0.00@0 -- $40.50@135
[REX's GUI], Update Market Data: IBM: $0.00@0 -- $40.50@135

31.10) User RAJ cancels their order
[RAJ's GUI], Update Market Activity: {2012-10-29 09:53:52.718} Cancel Message: SELL 135 IBM at $40.50 SELL Order Cancelled [Tradable Id: RAJIBM$40.501057189923143491]
[RAJ's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0
[ANN's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0
[REX's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0

32.11) Display position values for all users
REX Stock Value: $1,767.50, Account Costs: $-1,757.50, Net Value: $10.00
ANN Stock Value: $-252.50, Account Costs: $257.50, Net Value: $5.00
RAJ Stock Value: $-1,515.00, Account Costs: $1,500.00, Net Value: $-15.00

33.12) User REX enters an order for IBM, BUY 100@$10.00
[RAJ's GUI], Update Market Data: IBM: $10.00@100 -- $0.00@0
[ANN's GUI], Update Market Data: IBM: $10.00@100 -- $0.00@0
[REX's GUI], Update Market Data: IBM: $10.00@100 -- $0.00@0

34.13) User ANN enters a quote for IBM, 100@$10.00 x 100@10.10
[RAJ's GUI], Update Market Data: IBM: $10.00@200 -- $10.10@100
[ANN's GUI], Update Market Data: IBM: $10.00@200 -- $10.10@100
[REX's GUI], Update Market Data: IBM: $10.00@200 -- $10.10@100

35.14) User RAJ enters an order for IBM, SELL 150@$10.00 - results in a trade
[RAJ's GUI], Update Market Activity: {2012-10-29 09:53:52.722} Fill Message: SELL 150 IBM at $10.00 leaving 0 [Tradable Id: RAJIBM$10.001057189934474349]
[REX's GUI], Update Market Activity: {2012-10-29 09:53:52.723} Fill Message: BUY 100 IBM at $10.00 leaving 0 [Tradable Id: REXIBM$10.001057189932172299]
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.723} Fill Message: BUY 50 IBM at $10.00 leaving 50 [Tradable Id: ANNIBM1057189933202832]
[RAJ's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@100
[ANN's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@100
[REX's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@100
[RAJ's GUI], Update Last Sale: IBM: $10.00@150
[ANN's GUI], Update Last Sale: IBM: $10.00@150
[REX's GUI], Update Last Sale: IBM: $10.00@150
[RAJ's GUI], Update Ticker: IBM: $10.00  
[ANN's GUI], Update Ticker: IBM: $10.00  
[REX's GUI], Update Ticker: IBM: $10.00  

36.15) User REX does a Book Depth query for IBM
IBM Book Depth: $10.00 x 50 -- $10.10 x 100

37.16) User REX enters a market order for IBM, SELL 75@MKT - results in a trade
[REX's GUI], Update Market Activity: {2012-10-29 09:53:52.726} Fill Message: BUY 75 IBM at $10.10 leaving 0 [Tradable Id: REXIBMMKT1057189937937047]
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.726} Fill Message: SELL 75 IBM at $10.10 leaving 25 [Tradable Id: ANNIBM1057189933209376]
[RAJ's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@25
[ANN's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@25
[REX's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@25
[RAJ's GUI], Update Last Sale: IBM: $10.10@75
[ANN's GUI], Update Last Sale: IBM: $10.10@75
[REX's GUI], Update Last Sale: IBM: $10.10@75
[RAJ's GUI], Update Ticker: IBM: $10.10 ↑
[ANN's GUI], Update Ticker: IBM: $10.10 ↑
[REX's GUI], Update Ticker: IBM: $10.10 ↑

38.17) User ANN does a Book Depth query for IBM
IBM Book Depth: $10.00 x 50 -- $10.10 x 25

39.18) User ANN cancels her quote for IBM
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.729} Cancel Message: BUY 50 IBM at $10.00 Quote BUY-Side Cancelled [Tradable Id: ANNIBM1057189933202832]
[ANN's GUI], Update Market Activity: {2012-10-29 09:53:52.729} Cancel Message: SELL 25 IBM at $10.10 Quote SELL-Side Cancelled [Tradable Id: ANNIBM1057189933209376]
[RAJ's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0
[ANN's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0
[REX's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0

40.19) Show stock holdings for all users
REX Holdings: [GOOG, IBM]
ANN Holdings: [GOOG, IBM]
RAJ Holdings: [GOOG, IBM]

41.20) Show order Id's  for all users
REX Orders: [User REX, BUY GOOG (REXGOOG$40.001057189840301268), User REX, BUY GOOG (REXGOOG$10.001057189905195587), User REX, BUY GOOG (REXGOOGMKT1057189914048470), User REX, BUY IBM (REXIBM$40.001057189920510762), User REX, BUY IBM (REXIBM$10.001057189932172299), User REX, BUY IBM (REXIBMMKT1057189937937047)]
ANN Orders: []
RAJ Orders: [User RAJ, SELL GOOG (RAJGOOG$40.501057189848358442), User RAJ, SELL GOOG (RAJGOOG$10.001057189908078924), User RAJ, SELL IBM (RAJIBM$40.501057189923143491), User RAJ, SELL IBM (RAJIBM$10.001057189934474349)]

42.21) Show positions for all users
REX Stock Value: $3,535.00, Account Costs: $-3,515.00, Net Value: $20.00
ANN Stock Value: $-505.00, Account Costs: $515.00, Net Value: $10.00
RAJ Stock Value: $-3,030.00, Account Costs: $3,000.00, Net Value: $-30.00
 */