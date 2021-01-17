package graphex2021.model;

/**
 * this exception is thrown if an element, vertex or edge, was passed to a function on the graph,
 * and the element is not in the graph
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class ElementNotInGraphException extends Exception {

    public ElementNotInGraphException(String s) {
        super(s);
    }
}
