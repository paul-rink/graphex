package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.Vertex;


/**
 * Vertex containing the element <code>V</code> to be used in the Graph.
 *
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
     * The position you want the vertex to be in
     */
    private GXPosition position;


    /**
     * Creates new Vertex containing the element <code>V</code>
     * TODO check how the ids should be created
     *
     * @param element stored in the vertex
     * @param pos the position you want the vertex to have
     */
    public GXVertex(V element, GXPosition pos) {
        this.element = element;
        this.position = pos;
        this.previous = null;
        this.marked = false;
        this.visible = false;
        this.currentDistance = 0;
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
     * TODO modify so that currentDistance and previous are correctly unmarked in that case
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

    /**
     * TODO implement and check what is exactly needed as the label
     *
     * @return the label matching this vertex
     */
    public String getLabel() {
        return null;
    }

    /**
     * Returning the unique id for this vertex
     *
     * @return the vertex id
     */
    public int getId() {
        return this.id;
    }


    /**
     * Returns the edge that was selected to select this vertex
     *
     * @return the edge that was selected previous to the marking of this vertex
     */
    public GXEdge getPrevious() {
        return previous;
    }

    /**
     * Returns the current distance that this vertex is from the starting vertex
     *
     * @return the current distance from the starting vertex
     */
    public int getCurrentDistance() {
        return currentDistance;
    }

    /**
     * TODO don't think it is necessesary to have this rather this be changed when marked or unmarked
     *
     * @param currentDistance prob not needed
     */
    public void setCurrentDistance(int currentDistance) {
        this.currentDistance = currentDistance;
    }

    /**
     * Returns the position of the vert
     *
     * @return the GXPosition of the vertex
     */
    public GXPosition getPosition() {
        return position;
    }

}
