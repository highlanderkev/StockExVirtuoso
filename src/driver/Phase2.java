package driver;

import constants.BookSide;
import price.PriceFactory;
import tradable.Order;
import tradable.Quote;
import tradable.Tradable;
import tradable.TradableDTO;

/* NOTE - Since the BUY & SELL indicators are being represented by you all in a variety of
 * ways (constants, enum types, etc), I cannot code this driver to use one specific method of representation.
 * 
 * So to account for that - I have put the text [BUY] or [SELL] in the code where I
 * want you to replace that [BUY] or [SELL] text with YOUR representation of the BUY and SELL side.
 */
public class Phase2 {

    public static void main(String[] args) throws Exception {

        // First, create an Order and a Tradable variable to use for testing
        Order order1 = null;
        Tradable tradable1;
        

        //1
        try {
            System.out.println("1) Create and print the content of a valid Order:");
            order1 = new Order("USER1", "GE", PriceFactory.makeLimitPrice("$21.59"), 250, BookSide.BUY); // Replace "[BUY]" with YOUR BUY representation
            System.out.println(order1); // This should execute
        } catch (Exception e) { // Catch anything you throw.
            // This catch block should be skipped if everything is working as it should.
            System.out.println("Creating of a valid Order resulted in an exception!");
            e.printStackTrace();
        }
        System.out.println();
        //////////

        //2
        System.out.println("2) Refer to the Order using a Tradable reference, and display content:");
        tradable1 = order1;
        System.out.println(tradable1);
        System.out.println();
        //////////
        
        //3
        System.out.println("3) Create and print the content of a TradableDTO (your format may vary, but the data content should be the same):");
        TradableDTO tDTO = new TradableDTO(tradable1.getProduct(), tradable1.getPrice(),
                tradable1.getOriginalVolume(), tradable1.getRemainingVolume(),
                tradable1.getCancelledVolume(), tradable1.getUser(),
                tradable1.getSide(), tradable1.isQuote(), tradable1.getId());
        System.out.println(tDTO);
        System.out.println();
        //////////
        
        //4
        try {
            System.out.println("4) Attempt to create an order using INVALID data (Zero volume) - should throw an exception:");
            order1 = new Order("USER1", "GE", PriceFactory.makeLimitPrice("$21.59"), 0, BookSide.BUY); // Replace "[BUY]" with YOUR BUY representation
            System.out.println("If this prints then you have accepted invalid data in your Order - ERROR!");
        } catch (Exception e) { // Catch anything you throw.
            // This block SHOULD execute.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());

        }
        System.out.println();
        //////////
        
        //5
        System.out.println("5) Change the cancelled and remaining volume of the order and display resulting tradable:");
        tradable1.setRemainingVolume(150);
        tradable1.setCancelledVolume(100);
        System.out.println(tradable1);
        System.out.println();
        //////////

        //6 & 7
        Quote quote1;
        try {
            System.out.println("6) Create and print the content of a valid Quote:");
            quote1 = new Quote("USER2", "GE", PriceFactory.makeLimitPrice("$21.56"), 100, PriceFactory.makeLimitPrice("$21.62"), 100);
            System.out.println(quote1);
            System.out.println();

            System.out.println("7) Display the individual Quote Sides of the new Quote object:");
            System.out.println("\t" + quote1.getQuoteSide(BookSide.BUY)); // Replace "[BUY]" with YOUR BUY representation
            System.out.println("\t" + quote1.getQuoteSide(BookSide.SELL)); // Replace "[SELL]" with YOUR SELL representation
        } catch (Exception ex) {
            // This catch block should not execute!
            System.out.println("Creating of a valid Quote resulted in an exception!");
            ex.printStackTrace();
        }
        System.out.println();
        //////////

        //8
        try {
            System.out.println("8) Attempt to create a quote using INVALID data (Zero sell volume) - should throw an exception:");
            quote1 = new Quote("USER2", "GE", PriceFactory.makeLimitPrice("$21.56"), 100, PriceFactory.makeLimitPrice("$21.62"), -50);
            System.out.println("If this prints then you have accepted invalid data in your Quote - ERROR!");
        } catch (Exception e) { // Catch anything you throw.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());

        }
        System.out.println();



    }
}

/*
 * Expected Driver Output:
  
1) Create and print the content of a valid Order:
USER1 order: BUY 250 GE at $21.59 (Original Vol: 250, CXL'd Vol: 0), ID: USER1GE$21.5944072698321781

2) Refer to the Order using a Tradable reference, and display content:
USER1 order: BUY 250 GE at $21.59 (Original Vol: 250, CXL'd Vol: 0), ID: USER1GE$21.5944072698321781

3) Create and print the content of a TradableDTO (your format may vary, but the data content should be the same):
Product: GE, Price: $21.59, OriginalVolume: 250, RemainingVolume: 250, CancelledVolume: 0, User: USER1, Side: BUY, IsQuote: false, Id: USER1GE$21.5944072698321781

4) Attempt to create an order using INVALID data (Zero volume) - should throw an exception:
Properly handled an invalid volume! Error message is: Negative or Zero value passed to Order 'setOriginalVolume': 0

5) Change the cancelled and remaining volume of the order and display resulting tradable:
USER1 order: BUY 150 GE at $21.59 (Original Vol: 250, CXL'd Vol: 100), ID: USER1GE$21.5944072698321781

6) Create and print the content of a valid Quote:
USER2 quote: GE $21.56 x 100 (Original Vol: 100, CXL'd Vol: 0) [USER2GE44072703026721] - $21.62 x 100 (Original Vol: 100, CXL'd Vol: 0) [USER2GE44072703047509]

7) Display the individual Quote Sides of the new Quote object:
	$21.56 x 100 (Original Vol: 100, CXL'd Vol: 0) [USER2GE44072703026721]
	$21.62 x 100 (Original Vol: 100, CXL'd Vol: 0) [USER2GE44072703047509]

8) Attempt to create a quote using INVALID data (Zero sell volume) - should throw an exception:
Properly handled an invalid volume! Error message is: Negative value passed to QuoteSide 'setOriginalVolume': -50


 */
