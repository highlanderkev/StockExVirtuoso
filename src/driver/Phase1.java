/*
 * This class needs to be in a package called "driver" or it won't compile as is.
 * if you want to put this class in a different package, you need to change the
 * package declaration below to match the desired package name.
 */
package driver;

import java.util.ArrayList;
import price.Price;
import price.PriceFactory;
import price.utils.InvalidPriceOperation;



public class Phase1 {

    private static ArrayList<Price> testPriceHolder = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        makeSomeTestPriceObjects();
        verifyTestPriceValues();
        verifyMathematicalOperations();
        verifyBooleanChecks();
        verifyComparisons();
        verifyFlyweight();
    }

    private static void makeSomeTestPriceObjects() throws Exception {
        System.out.println("Creating some Test Price Objects.");
        testPriceHolder.add(PriceFactory.makeLimitPrice("10.50"));
        testPriceHolder.add(PriceFactory.makeLimitPrice("$1400.99"));
        testPriceHolder.add(PriceFactory.makeLimitPrice("$-51.52"));
        testPriceHolder.add(PriceFactory.makeLimitPrice(".49"));
        testPriceHolder.add(PriceFactory.makeLimitPrice("-0.89"));
        testPriceHolder.add(PriceFactory.makeLimitPrice("12"));
        testPriceHolder.add(PriceFactory.makeLimitPrice("90."));
        testPriceHolder.add(PriceFactory.makeLimitPrice("14.5"));
        testPriceHolder.add(PriceFactory.makeMarketPrice());
    }

    private static void verifyTestPriceValues() {
        System.out.println("Verifying the Values in Your Test Price Objects:");
        String format = "%-9s --> %9s : %s%n";
        System.out.format(format, "$10.50", testPriceHolder.get(0), testPriceHolder.get(0).toString().equals("$10.50") ? "CORRECT" : "ERROR");
        System.out.format(format, "$1,400.99", testPriceHolder.get(1), testPriceHolder.get(1).toString().equals("$1,400.99") ? "CORRECT" : "ERROR");
        System.out.format(format, "$-51.52", testPriceHolder.get(2), testPriceHolder.get(2).toString().equals("$-51.52") ? "CORRECT" : "ERROR");
        System.out.format(format, "$0.49", testPriceHolder.get(3), testPriceHolder.get(3).toString().equals("$0.49") ? "CORRECT" : "ERROR");
        System.out.format(format, "$-0.89", testPriceHolder.get(4), testPriceHolder.get(4).toString().equals("$-0.89") ? "CORRECT" : "ERROR");
        System.out.format(format, "$12.00", testPriceHolder.get(5), testPriceHolder.get(5).toString().equals("$12.00") ? "CORRECT" : "ERROR");
        System.out.format(format, "$90.00", testPriceHolder.get(6), testPriceHolder.get(6).toString().equals("$90.00") ? "CORRECT" : "ERROR");
        System.out.format(format, "$14.50", testPriceHolder.get(7), testPriceHolder.get(7).toString().equals("$14.50") ? "CORRECT" : "ERROR");
        System.out.format(format, "MKT", testPriceHolder.get(8), testPriceHolder.get(8).toString().equals("MKT") ? "CORRECT" : "ERROR");
        System.out.println();
    }

    private static void verifyMathematicalOperations() {
        System.out.println("Verifying the Functionality of your Mathematical Operations:");
        String format = "%-9s %c %9s = %9s : %s%n";
        try {
            Price results = testPriceHolder.get(0).add(testPriceHolder.get(1));
            System.out.format(format, testPriceHolder.get(0), '+', testPriceHolder.get(1), results, results.toString().equals("$1,411.49") ? "CORRECT" : "ERROR");
        } catch (InvalidPriceOperation ex) {
            System.out.println("FAILED: " + ex.getMessage());
        }
        try {
            Price results = testPriceHolder.get(1).subtract(testPriceHolder.get(1));
            System.out.format(format, testPriceHolder.get(1), '-', testPriceHolder.get(1), results, results.toString().equals("$0.00") ? "CORRECT" : "ERROR");
        } catch (InvalidPriceOperation ex) {
            System.out.println("FAILED: " + ex.getMessage());
        }
        try {
            Price results = testPriceHolder.get(2).add(testPriceHolder.get(3));
            System.out.format(format, testPriceHolder.get(2), '+', testPriceHolder.get(3), results, results.toString().equals("$-51.03") ? "CORRECT" : "ERROR");
        } catch (InvalidPriceOperation ex) {
            System.out.println("FAILED: " + ex.getMessage());
        }
        try {
            Price results = testPriceHolder.get(3).multiply(4);
            System.out.format(format, testPriceHolder.get(3), '*', 4, results, results.toString().equals("$1.96") ? "CORRECT" : "ERROR");
        } catch (InvalidPriceOperation ex) {
            System.out.println("FAILED: " + ex.getMessage());
        }
        try {
            Price results = testPriceHolder.get(4).subtract(testPriceHolder.get(5));
            System.out.format(format, testPriceHolder.get(4), '-', testPriceHolder.get(5), results, results.toString().equals("$-12.89") ? "CORRECT" : "ERROR");
        } catch (InvalidPriceOperation ex) {
            System.out.println("FAILED: " + ex.getMessage());
        }
        try {
            Price results = testPriceHolder.get(5).add(testPriceHolder.get(6));
            System.out.format(format, testPriceHolder.get(5), '+', testPriceHolder.get(6), results, results.toString().equals("$102.00") ? "CORRECT" : "ERROR");
        } catch (InvalidPriceOperation ex) {
            System.out.println("FAILED: " + ex.getMessage());
        }
        try {
            testPriceHolder.get(8).add(testPriceHolder.get(0));
            System.out.println("ERROR: Adding a LIMIT price to a MARKET Price succeeded: " + testPriceHolder.get(8) + " + " + testPriceHolder.get(0));
        } catch (InvalidPriceOperation ex) {
            System.out.println("CORRECT: " + ex.getMessage() + ": " + testPriceHolder.get(8) + " + " + testPriceHolder.get(0));
        }
        try {
            testPriceHolder.get(8).subtract(testPriceHolder.get(0));
            System.out.println("ERROR: Subtracting a LIMIT price from a MARKET Price succeeded: " + testPriceHolder.get(8) + " - " + testPriceHolder.get(0));

        } catch (InvalidPriceOperation ex) {
            System.out.println("CORRECT: " + ex.getMessage() + ": " + testPriceHolder.get(8) + " - " + testPriceHolder.get(0));
        }
        try {
            testPriceHolder.get(8).multiply(10);
            System.out.println("ERROR: Multiplying a MARKET price succeeded: " + testPriceHolder.get(8) + " + 10");

        } catch (InvalidPriceOperation ex) {
            System.out.println("CORRECT: " + ex.getMessage() + ": " + testPriceHolder.get(8) + " * 10");
        }
        System.out.println();
    }

    private static void verifyBooleanChecks() {
        System.out.println("Verifying the Functionality of your Boolean Checks:");
        System.out.println("Value      | Is Negative | Is Market");
        System.out.println("------------------------------------");
        String format = "%-9s  | %-12s| %-12s%n";
        System.out.format(format, testPriceHolder.get(0), testPriceHolder.get(0).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(0).isMarket() ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(1), testPriceHolder.get(1).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(1).isMarket() ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(2), testPriceHolder.get(2).isNegative() ? "CORRECT" : "ERROR", testPriceHolder.get(2).isMarket() ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(3), testPriceHolder.get(3).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(3).isMarket() ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(4), testPriceHolder.get(4).isNegative() ? "CORRECT" : "ERROR", testPriceHolder.get(4).isMarket() ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(5), testPriceHolder.get(5).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(5).isMarket() ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(6), testPriceHolder.get(6).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(6).isMarket() ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(7), testPriceHolder.get(7).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(7).isMarket() ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(8), testPriceHolder.get(8).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(8).isMarket() ? "CORRECT" : "ERROR");
        System.out.println();
    }

    private static void verifyComparisons() {
        System.out.println("Verifying the Functionality of your Boolean Comparisons:");
        Price testPrice = testPriceHolder.get(7);

        String format = "%-10s | %-15s | %-12s | %-12s | %-9s%n";
        System.out.println("Comparison\nto " + testPrice + "  | greaterOrEqual  | greaterThan  | lessOrEqual  | lessThan");
        System.out.println("---------------------------------------------------------------------");
        System.out.format(format, testPriceHolder.get(0),
                testPriceHolder.get(0).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(0).greaterThan(testPrice) ? "ERROR" : "CORRECT",
                testPriceHolder.get(0).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(0).lessThan(testPrice) ? "CORRECT" : "ERROR");
        System.out.format(format, testPriceHolder.get(1),
                testPriceHolder.get(1).greaterOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(1).greaterThan(testPrice) ? "CORRECT" : "ERROR",
                testPriceHolder.get(1).lessOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(1).lessThan(testPrice) ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(2),
                testPriceHolder.get(2).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(2).greaterThan(testPrice) ? "ERROR" : "CORRECT",
                testPriceHolder.get(2).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(2).lessThan(testPrice) ? "CORRECT" : "ERROR");
        System.out.format(format, testPriceHolder.get(3),
                testPriceHolder.get(3).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(3).greaterThan(testPrice) ? "ERROR" : "CORRECT",
                testPriceHolder.get(3).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(3).lessThan(testPrice) ? "CORRECT" : "ERROR");
        System.out.format(format, testPriceHolder.get(4),
                testPriceHolder.get(4).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(4).greaterThan(testPrice) ? "ERROR" : "CORRECT",
                testPriceHolder.get(4).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(4).lessThan(testPrice) ? "CORRECT" : "ERROR");
        System.out.format(format, testPriceHolder.get(5),
                testPriceHolder.get(5).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(5).greaterThan(testPrice) ? "ERROR" : "CORRECT",
                testPriceHolder.get(5).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(5).lessThan(testPrice) ? "CORRECT" : "ERROR");
        System.out.format(format, testPriceHolder.get(6),
                testPriceHolder.get(6).greaterOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(6).greaterThan(testPrice) ? "CORRECT" : "ERROR",
                testPriceHolder.get(6).lessOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(6).lessThan(testPrice) ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(7),
                testPriceHolder.get(7).greaterOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(7).greaterThan(testPrice) ? "ERROR" : "CORRECT",
                testPriceHolder.get(7).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(7).lessThan(testPrice) ? "ERROR" : "CORRECT");
        System.out.format(format, testPriceHolder.get(8),
                testPriceHolder.get(8).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(8).greaterThan(testPrice) ? "ERROR" : "CORRECT",
                testPriceHolder.get(8).lessOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(8).lessThan(testPrice) ? "ERROR" : "CORRECT");
        System.out.println();


    }

    private static void verifyFlyweight() throws Exception {
        System.out.println("Verifying your Flyweight Implementation:");
        String format = "Price %-9s is same object as new %9s: %s%n";
        Price p1 = PriceFactory.makeLimitPrice("10.50");
        System.out.format(format, testPriceHolder.get(0), p1, testPriceHolder.get(0) == p1 ? "CORRECT" : "ERROR");
        System.out.format(format, testPriceHolder.get(1), p1, testPriceHolder.get(1) == p1 ? "ERROR" : "CORRECT");

        p1 = PriceFactory.makeMarketPrice();
        System.out.format(format, testPriceHolder.get(8), p1, testPriceHolder.get(8) == p1 ? "CORRECT" : "ERROR");
        System.out.format(format, testPriceHolder.get(1), p1, testPriceHolder.get(1) == p1 ? "ERROR" : "CORRECT");
    }
}
/*
 * Expected output from this "main" method:
-------------------------------------
Creating some Test Price Objects.
Verifying the Values in Your Test Price Objects:
$10.50    -->    $10.50 : CORRECT
$1,400.99 --> $1,400.99 : CORRECT
$-51.52   -->   $-51.52 : CORRECT
$0.49     -->     $0.49 : CORRECT
$-0.89    -->    $-0.89 : CORRECT
$12.00    -->    $12.00 : CORRECT
$90.00    -->    $90.00 : CORRECT
$14.50    -->    $14.50 : CORRECT
MKT       -->       MKT : CORRECT

Verifying the Functionality of your Mathematical Operations:
$10.50    + $1,400.99 = $1,411.49 : CORRECT
$1,400.99 - $1,400.99 =     $0.00 : CORRECT
$-51.52   +     $0.49 =   $-51.03 : CORRECT
$0.49     *         4 =     $1.96 : CORRECT
$-0.89    -    $12.00 =   $-12.89 : CORRECT
$12.00    +    $90.00 =   $102.00 : CORRECT
CORRECT: Cannot add a LIMIT price to a MARKET Price: MKT + $10.50
CORRECT: Cannot subtract a LIMIT price from a MARKET Price: MKT - $10.50
CORRECT: Cannot multiply a MARKET price: MKT * 10

Verifying the Functionality of your Boolean Checks:
Value      | Is Negative | Is Market
------------------------------------
$10.50     | CORRECT     | CORRECT     
$1,400.99  | CORRECT     | CORRECT     
$-51.52    | CORRECT     | CORRECT     
$0.49      | CORRECT     | CORRECT     
$-0.89     | CORRECT     | CORRECT     
$12.00     | CORRECT     | CORRECT     
$90.00     | CORRECT     | CORRECT     
$14.50     | CORRECT     | CORRECT     
MKT        | CORRECT     | CORRECT     

Verifying the Functionality of your Boolean Comparisons:
Comparison
to $14.50  | greaterOrEqual  | greaterThan  | lessOrEqual  | lessThan
---------------------------------------------------------------------
$10.50     | CORRECT         | CORRECT      | CORRECT      | CORRECT  
$1,400.99  | CORRECT         | CORRECT      | CORRECT      | CORRECT  
$-51.52    | CORRECT         | CORRECT      | CORRECT      | CORRECT  
$0.49      | CORRECT         | CORRECT      | CORRECT      | CORRECT  
$-0.89     | CORRECT         | CORRECT      | CORRECT      | CORRECT  
$12.00     | CORRECT         | CORRECT      | CORRECT      | CORRECT  
$90.00     | CORRECT         | CORRECT      | CORRECT      | CORRECT  
$14.50     | CORRECT         | CORRECT      | CORRECT      | CORRECT  
MKT        | CORRECT         | CORRECT      | CORRECT      | CORRECT  

Verifying your Flyweight Implementation:
Price $10.50    is same object as new    $10.50: CORRECT
Price $1,400.99 is same object as new    $10.50: CORRECT
Price MKT       is same object as new       MKT: CORRECT
Price $1,400.99 is same object as new       MKT: CORRECT
 */