package graphex2021.model;
/**
 * this exception is thrown if a GXPosition is created with coordinate outside of
 * allowed range of [0-1000]
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class NonValidCoordinatesException extends Exception {
    public NonValidCoordinatesException(String s) {
        super(s);
    }

}
