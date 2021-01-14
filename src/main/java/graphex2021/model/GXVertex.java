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
     * The element stored in the vertex.
     */
    private V element;

    /**
     * Unique identifier for the vertex
     */
    private int id;

    private boolean visible;
    private boolean marked;

    /**
     * The previous edge that was chosen to mark this vertex
     *
     */
    private GXEdge previous;

    /**
     * The calculated distance from the starting vertex to this one.
     * Length of the path to get to this vertex.
     *
     */
    private int currentDistance;

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

    /**
     * Checks whether the vertex is visible
     *
     * @return whether the vertex is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibilty of the vertex
     *
     * @param visible visibility you want the vertex to have
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns whether the vertex is already marked

     * @return whether the vertex is marked
     */
    public boolean marked() {
        return this.marked;
    }

    /**
     * Sets the marked state of the vertex to the desired state
     *
     * @param marked lets you mark or unmark the vertex
     */
    public void mark(boolean marked) {
        this.marked = marked;
    }

    /**
     * Allows you to mark a vertex if an edge that contains this vertex is selected
     * will also set the previousEdge and the Distance of the vertex
     *
     * @param prevVertexDistance the distance that the prevVertex was from the startingvertex
     * @param previousEdge the edge that was marked to get to this vertex
     */
    public void mark(int prevVertexDistance, GXEdge previousEdge) {
        this.marked = true;
        this.currentDistance = prevVertexDistance + previousEdge.getWeight();
        this.previous = previousEdge;
    }
}
