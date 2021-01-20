package graphex2021.model;


import com.brunomnsilva.smartgraph.graph.*;

import java.util.Collection;

/**
 * Graph made up of a set of vertices and edges. Edges are undirected and connect two vertices with each other.
 *
 *
 *
 * @param <V> the element stored in the vertices
 * @param <E> the element stored in the edges
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public interface GraphInterface<V, E> {

    /**
     * Returns the number of vertices in the graph
     *
     * @return the total number of vertices in the graph
     */
    int numVertices();

    /**
     * returns the numvber of edges in the graph
     *
     * @return the total number of edges in the graph
     */
    int numEdges();

    /**
     * Returns all the vertices of the graph in a collection.
     *
     * @return collection containing all the {@link GXVertex} in the Graph
     */
    Collection<GXVertex> vertices();

    /**
     * Returns all the edges of the graph in a collection
     *
     * @return collection containing all the {@link GXEdge} in the Graph
     */
    Collection<GXEdge> edges();

    /**
     * Returns all the edges, that are incident to the passed vertex. Edges are incident if they are connected
     * to the passed Vertex. Isolated vertices will return an empty Collection.
     *
     * @param v {@link GXVertex} that you want the incident edges of.
     * @return Collection of the {@link GXEdge} that are incident to v.
     * @throws ElementNotInGraphException if the vertex is not part of the graph.
     */
    Collection<GXEdge> incidentEdges(GXVertex v) throws ElementNotInGraphException;

    /**
     * Will get the opposite {@link GXVertex} given a vertex v and a {@link GXEdge} e.
     *
     * @param v {@link GXVertex} on one end of e
     * @param e {@link GXEdge} connected to v
     * @return opposite vertex of v connected by e
     * @throws ElementNotInGraphException if either v, or e are not in the graph
     */
    GXVertex opposite(GXVertex v, GXEdge e) throws ElementNotInGraphException;


    /**
     * Checks whetheer two {GXVertex} are coonecte by an edge
     *
     * @param u first {@link GXVertex}
     * @param v second {@link GXEdge}
     * @return true if connected, false if not
     * @throws ElementNotInGraphException either vertex is not in the graph.
     */
    boolean areAdjacent(GXVertex u, GXVertex v) throws ElementNotInGraphException;

    /**
     * Adds the passed {@link GXVertex} to the graph
     *
     * @param vertex {@link GXVertex} to be inserted
     * @return the inserted {GXVertex}
     */
    GXVertex insertVertex(GXVertex vertex);

    /**
     * Adds an {@link GXEdge} to the graph  between two {@link GXVertex}.
     * The new edge will have the passed element.
     *
     * @param u first vertex
     * @param v second vertex
     * @param edgeElement element of the newly created edge
     * @return the edge that was inserted
     */
    GXEdge insertEdge(GXVertex u, GXVertex v, String edgeElement) throws ElementNotInGraphException;

    /**
     * TODO maybe not needed
     *
     * @param edge
     * @return
     * @throws ElementNotInGraphException
     */
    GXEdge insertEdge(GXEdge edge) throws ElementNotInGraphException;



    /**
     * Removes a {@link GXVertex} v from the graph. All the incident edges will be removed as well.
     *
     * @param v vertex to be removed.
     * @return the element contained in the removed Vertex.
     * @throws ElementNotInGraphException if the vertex is not in the graph
     */
    String removeVertex(GXVertex v) throws ElementNotInGraphException;

    /**
     * Removes an {@link GXEdge} connecting to vertices from the graph
     *
     * @param e the edge to be removed
     * @return Element stored in the edge
     * @throws ElementNotInGraphException if the edge is not in the graph
     */
    String removeEdge(GXEdge e) throws ElementNotInGraphException;

    /**
     * TODO Check if actually needed
     *
     * @param v
     * @param newElement
     * @return
     * @throws InvalidVertexException
     */
    Object replace(GXVertex v, String newElement) throws ElementNotInGraphException;


    /**
     * TODO Check if actually needed
     *
     * @param e
     * @param newElement
     * @return
     * @throws ElementNotInGraphException
     */
    Object replace(GXEdge e, String newElement) throws ElementNotInGraphException;


    /**
     * Setting all the edges visible that are adjacent to the passed vertex
     *
     * @param vertex vertex which adjacent edges should be made visible
     * @throws ElementNotInGraphException if the vertex was not in the graph
     */
    void setEdgeVisible(GXVertex vertex) throws ElementNotInGraphException;

    /**
     * Making the vertex at the end of an edge visible if before only one vertex was visible.
     *
     * @param edge containing the vertex to be made visible. Needs to contain one visible vertex
     * @throws ElementNotInGraphException if the edge is not in the graph
     */
    void setVertexVisible(GXEdge edge, GXVertex opposite)throws ElementNotInGraphException;

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

    /**
     * Sets all edges including its vertices visible that contain the given {@code vertex}.
     *
     * @param vertex is the vertex for that the neighborhood is set visible.
     */
    void makeIncidentsVisible(GXVertex vertex);


}
