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
     * Checks whether two {GXVertex} are connected by an edge
     *
     * @param u first {@link GXVertex}
     * @param v second {@link GXEdge}
     * @return true if connected, false if not
     * @throws ElementNotInGraphException either vertex is not in the graph.
     */
    boolean areAdjacent(GXVertex u, GXVertex v) throws ElementNotInGraphException;

    /**
     * Adds the passed {@link GXVertex} to the graph, if there isn't already a {@link GXVertex} with the same ID.
     * If {@code null} is passed null will also be returned and the graph won't change
     *
     * @param vertex {@link GXVertex} to be inserted
     * @return the inserted vertex or the vertex that has the same ID as the passed vertex.
     * {@code null} if null was passed
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
     * Takes a GXEdge and inserts it into the graph. If the graph already contains an edge with the same ID this edge
     * will be returned instead.
     *
     * @param edge the edge to be put in the graph
     * @return the edge that was inserted into the graph or the edge with the same ID as the passed edge.
     * @throws ElementNotInGraphException if either vertex of the edge is not in the graph
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
     * Blocks edges adjacent to passed vertex that potentially will form a circle.
     *
     * @param vertex the newly marked vertex, that might make it necessary to block edges.
     * @throws ElementNotInGraphException if the passed vertex is not in the graph
     */
    void blockCircles(GXVertex vertex) throws ElementNotInGraphException;


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
    Collection<GXVertex> getNeighbors(GXVertex v) throws ElementNotInGraphException;

    /**
     * Sets the passed edge invisible and checks which vertices and edges should now be invisible as well
     *
     *
     * @param vertex that was made visible by the passed edge
     * @param edge that will be set invisible f
     * @throws ElementNotInGraphException if either vertex or edge are not in the vertex
     */
    void setVertexInvisible(GXVertex vertex, GXEdge edge) throws ElementNotInGraphException;

    /**
     * Sets v as starting vertex of the graph. Is needed by some algos.
     * @param v is the vertex that should be the starting vertex.
     */
    void setStartingVertex(GXVertex v);

    /**
     * Sets v as ending vertex of the graph. Is needed by some algos.
     * @param v is the vertex that should be the ending vertex.
     */
    void setEndingVertex(GXVertex v);


}
