package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.Vertex;


/**
 * Vertex containing the element <code>V</code> to be used in the Graph.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GXVertex implements Vertex<String> {

    /**
     * The element stored in the vertex.
     */
    private String element;

    /**
     * Unique identifier for the vertex
     */
    private int id;

    private boolean visible;
    private boolean marked;
    private boolean isHint;

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
     *
     * @param element stored in the vertex
     * @param id unique id for vertex in a graph. Needs to be uniquely created before setting
     * @param pos the position you want the vertex to have
     */
    public GXVertex(String element, int id, GXPosition pos) {
        this.element = element;
        this.position = pos;
        this.id = id;
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
    public String element() {
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
    public boolean isMarked() {
        return this.marked;
    }

    /**
     * Sets the marked state of the vertex to the desired state.
     * Setting the previous edge to null also.
     *
     */
    public void unmark() {
        this.marked = false;
        this.previous = null;
        this.currentDistance = -1;
    }

    /**
     * Allows you to mark a vertex if an edge that contains this vertex is selected
     * will also set the previousEdge and the Distance of the vertex by adding the weight of the previous edge to the
     * currentDistance of previous vertex.
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
     * Just marks this vertex
     */
    public void mark() {
        this.marked = true;
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
     * Returns the position of the vert
     *
     * @return the GXPosition of the vertex
     */
    public GXPosition getPosition() {
        return position;
    }

    /**
     * Checks if to vertices are the same. Two vertices are equal, if they have the same {@code id}.
     * @param o ist the vertex you want to check.
     * @return {@code true} if id of the 2nd vertex is equal to id of this vertex, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        boolean isGXVertex = o.getClass().isInstance(this);
        boolean hasSameID = this.id == ((GXVertex) o).getId();
        return isGXVertex && hasSameID;
    }

    public void setHint(boolean isHint) {
        this.isHint = isHint;
    }

    public boolean isHint() {
        return this.isHint;
    }
}
