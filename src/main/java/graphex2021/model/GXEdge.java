package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.Edge;


/**
 * This class models edges for {@link GXGraph}. An edge is represented by two vertices {@link GXVertex} and identified
 * by a unique {@code id}. {@link GXEdge}s are undirected and can have a {@code weight}. Every edge also has an
 * {@code element} implemented from {@link Edge} that might be used to represent the label of the edge. An edge can
 * be assigned for {@code marked}, {@code blocked} and {@code visible}.
 *
 *@author D. Flohs, K. Marquardt, P. Rink
 *@version 1.0 14.01.2021
 */
public class GXEdge implements Edge<String, String> {


    /**
     * Vertices that are connected by this edge
     */
    private GXVertex inboundVertex;
    private GXVertex outboundVertex;


    /**
     * The element contained in the edge
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
     * Creates new Vertex with a weight
     * TODO finish constructor to contain missing attributes. Check how to generate the IDs
     * @param inboundVertex the first vertex of the edge
     * @param outboundVertex the second vertex of the edge
     * @param element the element contained in the edge
     * @param id the unique id given to the edge
     * @param weight the weight of the edge
     */
    public GXEdge(GXVertex inboundVertex, GXVertex outboundVertex, String element, int weight, int id) {
        this.inboundVertex = inboundVertex;
        this.outboundVertex = outboundVertex;
        this.element = element;
        this.visible = false;
        this.blocked = false;
        this.marked = false;
        this.weight = weight;
        this.id = id;

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

    /**
     * Sets the marked state of the edge to the passed value
     *
     */
    public void mark() {
        this.marked = true;
    }

    /**
     * Unmarks the edge.
     *
     */
    public void unmark() {
        this.marked = false;
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

    /**
     * Returns whether the edge is set visible.
     *
     * @return {@code true} if the edge is visible, {@code false} else.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Returns the id given to this edge
     *
     * @return the ID given to this edge
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the weight of the edge.
     * @param w is the new weight of the edge.
     */
    public void setWeight(int w) {
        this.weight = w;
    }
}
