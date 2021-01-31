package graphex2021.model;

/**
 * this exception is thrown if a graph is parsed which does not match the specified json format
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */

public class WrongFileFormatException extends Exception {
    public WrongFileFormatException(String s) {
        super(s);
    }
}
