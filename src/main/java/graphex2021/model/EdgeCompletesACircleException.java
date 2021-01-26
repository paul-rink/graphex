package graphex2021.model;

/**
 * this exception is thrown if you try to mark an edge that would complete a circle.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 26.01.2021
 */
public class EdgeCompletesACircleException extends Exception {
    EdgeCompletesACircleException(String s) {
        super(s);
    }
}
