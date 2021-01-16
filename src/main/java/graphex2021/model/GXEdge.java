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
public class GXEdge implements Edge<String, String> {


    /**
     * Vertices that are connected by this edge
     */
    private GXVertex inboundVertex;
    private GXVertex outboundVertex;


    /**
     * The element conatined in the edge
     */
    private String element;

    /**
     * Unique identifier for this edge
     */
    private int id;

    /**
     * Stores the weight of this edge
     */
    private int weight;

    /**
     * Stores whether the edge is marked
     */
    private boolean marked;

    /**
     * Stores whether the edge is blocked because of a potential circle
     */
    private boolean blocked;

    /**
     * Stores whether the edge is currently visible
     */
    private boolean visible;





    /**
     * Creates new Vertex
     * TODO finish constructor to contain missing attributes. Check how to generate the IDs
     * @param inboundVertex the first vertex of the edge
     * @param outboundVertex the second vertex of the edge
     * @param element the element contained in the edge
     */
    public GXEdge(GXVertex inboundVertex, GXVertex outboundVertex, String element) {
        this.inboundVertex = inboundVertex;
        this.outboundVertex = outboundVertex;
        this.element = element;
        this.visible = false;
        this.blocked = false;
        this.marked = false;

    }


    /**
     * Returns the element stored in the edge
     *
     * @return element stored in the edge
     */
    @Override
    public String element() {
        return null;
    }

    /**
     * Returns both the vertices in the array
     * TODO for now needs to return Vertex<String> should
     *
     * @return array of length two containing both vertices
     */
    @Override
    public GXVertex[] vertices() {

        return new GXVertex[]{inboundVertex, outboundVertex};
    }

    /**
     * Returns the weight of this edge
     *
     * @return weight of this edge
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Returns whether this edge is blocked because of a potential circle
     *
     * @return whether the edge is blocked
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Returns whether this edge is currently marked
     *
     * @return whether edge is marked
     */
    public boolean isMarked() {
        return marked;
    }


    /**
     * Gets the unmarked GXVertex from this edge, which will be the next to mark if edge is picked
     * TODO check the right error handling if both edges are marked. Should not be the case. Add blocked check
     *
     * @return the unmarked vertex that is part of this edge
     */
    public GXVertex getNextVertex() {
        if (inboundVertex.isMarked()) {
            return inboundVertex;
        } else if (outboundVertex.isMarked()) {
            return outboundVertex;
        } else {
            throw new IllegalArgumentException("Both vertices of this edge are marked - Should be blocked");
        }
    }

    public void mark(boolean marked) {

    }

    /**
     * Checks whether a given GXVertex is on this edge.
     *
     * @param vertex checks this vertex
     * @return whether the given GXVertex is on this edge
     */
    public boolean contains(GXVertex vertex) {
        return this.outboundVertex.getId() == vertex.getId() || this.inboundVertex.getId() == vertex.getId();
    }


    /**
     * Blocks or unblocks the edge
     *
     * @param blocked sets the blocked state for the edge
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }


    /**
     * Makes the edge visivile or invisible
     *
     * @param visible sets the visibility for the edge
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
