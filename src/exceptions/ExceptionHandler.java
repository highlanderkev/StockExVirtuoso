/*
 * ExceptionHandler.java
 */
package exceptions;

/**
 * This the exception handler which checks and throws exceptions for input data
 * coming from the user.
 *
 * @author Kevin Patrick Westropp
 * @version StockExVirtuoso.Version.1.0
 */
public class ExceptionHandler {

    /**
     * Public method checks if object is null and throws exception if invalid.
     *
     * @param obj
     * @param msg
     * @return boolean true if valid input
     * @throws InvalidValueException
     */
    public static boolean checkObject(Object obj, String msg) throws InvalidValueException {
        if (obj == null) {
            throw new InvalidValueException("InvalidValueException: Null object passed to " + msg);
        } else {
            return true;
        }
    }

    /**
     * Public method checks string value to make sure it only contains numeric
     * characters.
     *
     * @param value as a string
     * @param msg
     * @return boolean true if valid input
     * @throws InvalidValueException
     * @see price.PriceFactory#makeLimitPrice(java.lang.String)
     */
    public static boolean checkPriceInput(String value, String msg) throws InvalidValueException {
        if (value.matches("^[0-9]+$") == false) {
            throw new InvalidValueException("InvalidValueException: String not in numeric format given to " + msg);
        } else {
            return true;
        }
    }

    /**
     * Public method checks the incoming String value and throws an exception if
     * invalid.
     *
     * @param value
     * @param msg
     * @return boolean true if valid input
     * @throws InvalidValueException
     */
    public static boolean checkString(String value, String msg) throws InvalidValueException {
        if (value == null || value.isEmpty()) {
            throw new InvalidValueException("InvalidValueException: " + (value == null ? "NULL" : "Empty") + " String passed into " + msg);
        } else {
            return true;
        }
    }

    /**
     * Public method checks the incoming long value and throws an exception if
     * invalid.
     *
     * @param value to be checked
     * @param msg
     * @return boolean true if valid input
     * @throws InvalidValueException
     */
    public static boolean checkLongNegative(Long value, String msg) throws InvalidValueException {
        if (value < 0) {
            throw new InvalidValueException("InvalidValueException: Negative value passed to " + msg);
        } else {
            return true;
        }
    }

    /**
     * Public method checks the incoming long value and throws an exception if
     * invalid.
     *
     * @param value to be checked
     * @param msg
     * @return boolean true if valid input
     * @throws InvalidValueException
     */
    public static boolean checkLongZero(Long value, String msg) throws InvalidValueException {
        if (value == 0) {
            throw new InvalidValueException("InvalidValueException: Zero value passed to " + msg);
        } else {
            return true;
        }
    }

    /**
     * Public method checks the incoming integer value and throws an exception
     * if invalid.
     *
     * @param value to be checked
     * @param msg
     * @return boolean true if valid input
     * @throws InvalidValueException
     */
    public static boolean checkIntNegative(int value, String msg) throws InvalidValueException {
        if (value < 0) {
            throw new InvalidValueException("InvalidValueException: Negative value passed to " + msg);
        } else {
            return true;
        }
    }

    /**
     * Public method checks the incoming integer value and throws an exception
     * if invalid.
     *
     * @param value to be checked
     * @param msg
     * @return boolean true if valid input
     * @throws InvalidValueException
     */
    public static boolean checkIntZero(int value, String msg) throws InvalidValueException {
        if (value == 0) {
            throw new InvalidValueException("InvalidValueException: Zero value passed to " + msg);
        } else {
            return true;
        }
    }
}
