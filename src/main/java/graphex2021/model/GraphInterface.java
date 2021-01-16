package graphex2021.model;


import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;

import java.util.Collection;

/**
 * Graph made up of a set of vertices and edges. Edges are undirected and connect two vertices with each other.
 *
 *
 * TODO why boolean return values? Generally check Javadocs
 *
 * @param <V> the element stored in the vertices
 * @param <E> the element stored in the edges
 * @author Dominik
 * @version 1.0
 */
public interface GraphInterface<V, E> extends Graph<V, E> {
    
    /**
     * Setting all the edges visible that are adjacent to the passed vertex
     * //TODO changed return type
     *
     * @param vertex vertex which adjacent edges should be made visible
     * @throws ElementNotInGraphException if the vertex was not in the graph
     */
    void setEdgeVisible(GXVertex vertex) throws ElementNotInGraphException;

    /**
     * Making the vertex at the end of an edge visible if before only one vertex was visible.
     * //TODO check changed return type
     *
     * @param edge containing the vertex to be made visible. Needs to contain one visible vertex
     * @throws ElementNotInGraphException if the edge is not in the graph
     */
    void setVertexVisible(GXEdge edge)throws ElementNotInGraphException;

    /**
     * //TODO propably best done in the vertex when marking or unmarking so propably not needed
     *
     * @return
     */
    boolean updateDistance();

    /**
     * Marks the edge and vertex passed by to the function.
     *
     * @param edge to be marked
     * @param vertex to be marked
     * @throws ElementNotInGraphException if either the vertex or the edge are not in the graph
     */

    void mark(GXEdge edge, GXVertex vertex) throws ElementNotInGraphException;

    /**
     * Gets edge adjacent to passed vertex that potentially needs to be blocked to avoid a circle being formed.
     * TODO Check if potentially more than one GXEdge could need to be blocked to avoid circles
     *
     * @param vertex
     * @return the edge that needs to be blocked
     * @throws ElementNotInGraphException if the passed vertex is not in the graph
     */
    GXEdge blockCircles(GXVertex vertex) throws ElementNotInGraphException;


    /**
     * Returns the starting vertex that was set for the graph.
     *
     * @return the starting vertex for algorithms on this graph
     */
    GXVertex getStartingVertex();

    /**
     * Returns the ending vertex that was set for the graph.
     *
     * @return the ending vertex for algorithms on this graph
     */
    GXVertex getEndingVertex();

    /**
     * Returns all adjacent vertices of a given {@link GXVertex}, i.e. the neighbors of {@code v}.
     *
     * @param v is the {@link GXVertex} you want to know all adjacent vertices of.
     * @return a collection of all adjacent vertices.
     */
    Collection<GXVertex> getNeighbors(GXVertex v);


    /**
     * Unmarks the vertex passed to this function.
     *
     *
     * @param vertex to be unmarked
     * @throws ElementNotInGraphException if the passed vertex is not in the graph
     */
    void unmarkVertex(GXVertex vertex) throws ElementNotInGraphException;

    /**
     * Sets the passed edge invisible and checks whcih vertices and edges should now be invisible as well
     *
     *
     * @param vertex that was made visible by the passed edge
     * @param edge that will be set invisible from
     * @throws ElementNotInGraphException if either vertex or edge are not in the vertex
     */
    void setVertexInvisible(GXVertex vertex, GXEdge edge) throws ElementNotInGraphException;


}
