package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.Vertex;


/**
 * Vertex containing the element <code>V</code> to be used in the Graph.
 *
 * TODO Finish the attributes and methods
 *
 * @author Dominik
 * @version 1.0
 * @param <V> Type of the element that is stored in the vertex
 */
public class GXVertex<V> implements Vertex<V> {

    /**
     * the element stored in the vertex.
     */
    private V element;

    /**
     * Creates new Vertex containing the element <code>V</code>
     *
     *
     * @param element stored in the vertex
     */
    public GXVertex(V element) {

    }

    /**
     * Returns the Element that is stored in the Vertex
     *
     * @return the element stored in the vertex
     */
    @Override
    public V element() {
        return element;
    }
}
