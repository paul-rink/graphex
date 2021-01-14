package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

/**
 * Edge containing the element of type e and vertices of type V
 *
 *
 * @author Dominik
 * @version 0.1
 * @param <E> type of the element contained in the edge
 * @param <V> type of the elements contained in the vertices
 */
public class GXEdge<E, V> implements Edge<E, V> {


    /**
     * Vertices that are connected by this edge
     */
    private Vertex<V> inboundVertex;
    private Vertex<V> outboundVertex;

    /**
     * The element conatined in the edge
     */
    private E element;


    /**
     * Creates new Vertex
     * TODO finish constructor to contain missing attributes.
     * @param inboundVertex the first vertex of the edge
     * @param outboundVertex the second vertex of the edge
     * @param element the element contained in the edge
     */
    public GXEdge(Vertex<V> inboundVertex, Vertex<V> outboundVertex, E element) {
        this.inboundVertex = inboundVertex;
        this.outboundVertex = outboundVertex;
        this.element = element;

    }


    /**
     * Returns the element stored in the edge
     *
     * @return element stored in the edge
     */
    @Override
    public E element() {
        return null;
    }

    /**
     * Returns both the vertices in the array
     *
     * @return array of length two containing both vertices
     */
    @Override
    public Vertex<V>[] vertices() {

        return new Vertex[]{inboundVertex, outboundVertex};
    }
}
