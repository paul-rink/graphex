package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.*;

import java.util.*;

/**
 *
 *
 *
 * @param <E> Elements stored in the graphs edges
 * @param <V> Elements stored in the graphs vertices
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GXGraph<V, E> implements GraphInterface<V, E> {


    /**
     * The starting and end vertices if needed by the algorithm used
     */
    private final GXVertex<V> startingVertex;
    private final GXVertex<V> endingVertex;

    /**
     * Map containing all the vertices in the graph
     */
    private final Map<V, GXVertex<V>> vertices;
    /**
     * Map containing all the edges in the graph
     */
    private final Map<E, GXEdge<E, V>> edges;


    /**
     * Empty constructor just initialising the attributes
     *
     */
    public GXGraph() {
        this.vertices = new HashMap<V, GXVertex<V>>();
        this.edges = new HashMap<E, GXEdge<E, V>>();
        this.startingVertex = null;
        this.endingVertex = null;

    }




    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        return edges.size();
    }

    /**
     * Returns a list containing all the vertices that are part of the graph.
     * TODO check for better way to be able to return  GXVertex and not have to typecast it later.
     * TODO Problem Generics invariant.
     * TODO more checking of how to handle Collection vs. Arraylist or something.
     * Problem is that in case you would use Collection<GXVertex> you would also be able to add Vertex to list.
     * List GXVertex is not a List Vertex since you could potentially insert a different type of Vertex into the list.
     *
     * @return ArrayList containing all the vertices of the Graph. For now can be typecast to GXVertex
     */
    @Override
    public Collection<Vertex<V>> vertices() {
        Collection<Vertex<V>> verticeList = new ArrayList<>();
        verticeList.addAll(vertices.values());
        return verticeList;
    }

    /**
     * Returns a list containing all the edges that are part of the graph.
     * TODO check for better way to be able to return  GXVertex and not have to typecast it later.
     *
     * @return ArrayList containing all the vertices of the Graph. For now can be typecast to GXEdge
     */
    @Override
    public Collection<Edge<E, V>> edges() {
        List<Edge<E, V>> edgeList = new ArrayList<>();
        edgeList.addAll(edges.values());
        return edgeList;
    }


    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> vertex) throws InvalidVertexException {
        GXVertex<V> v = checkVertex(vertex);

        List<Edge<E, V>> incident = new ArrayList<>();
        for (GXEdge<E, V> edge : edges.values()) {
            if (edge.contains(v)) {
                incident.add(edge);
            }
        }
        return incident;
    }

    @Override
    public Vertex opposite(Vertex<V> vertex, Edge<E, V> edge) throws InvalidVertexException, InvalidEdgeException {
        GXVertex<V> v = checkVertex(vertex);
        GXEdge<E,V> e = checkEdge(edge);

        if (!e.contains(v)) {
            // Vertex is not part of the passed edge
            return null;
        }

        if (edge.vertices()[0] == v) {
            return edge.vertices()[1];
        } else {
            return edge.vertices()[0];
        }
    }

    @Override
    public boolean areAdjacent(Vertex vertex, Vertex vertex1) throws InvalidVertexException {
        return false;
    }

    @Override
    public Vertex insertVertex(Object o) throws InvalidVertexException {
        return null;
    }

    @Override
    public Edge insertEdge(Vertex vertex, Vertex vertex1, Object o)
            throws InvalidVertexException, InvalidEdgeException {
        return null;
    }

    @Override
    public Edge insertEdge(Object o, Object v1, Object o2) throws InvalidVertexException, InvalidEdgeException {
        return null;
    }

    @Override
    public Object removeVertex(Vertex vertex) throws InvalidVertexException {
        return null;
    }

    @Override
    public Object removeEdge(Edge edge) throws InvalidEdgeException {
        return null;
    }

    @Override
    public Object replace(Vertex vertex, Object o) throws InvalidVertexException {
        return null;
    }

    @Override
    public Object replace(Edge edge, Object o) throws InvalidEdgeException {
        return null;
    }

    @Override
    public void setEdgeVisible(GXVertex vertex) {
    }

    @Override
    public void setVertexVisible(GXEdge edge) {
    }

    @Override
    public boolean updateDistance() {
        return false;
    }

    @Override
    public void mark(GXEdge edge, GXVertex vertex) {

    }

    @Override
    public GXEdge blockCircles(GXVertex vertex) {
        return null;
    }

    @Override
    public GXVertex getStartingVertex() {
        return null;
    }

    @Override
    public GXVertex getEndingVertex() {
        return null;
    }

    @Override
    public void unmarkVertex(GXVertex vertex) {

    }

    @Override
    public void setVertexInvisible(GXVertex vertex, GXEdge edge) {

    }

    /**
     * Checks if a vertex is in the graph and actually a GXvertex.
     * TODO other way to do the exception handling
     *
     * @return the GXvertex
     */
    private GXVertex<V> checkVertex(Vertex<V> vertex) throws InvalidVertexException {
        if (vertex == null) {
            throw new InvalidVertexException("Vertex is null");
        }

        GXVertex gxVertex;
        try {
            gxVertex =  (GXVertex) vertex;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a GXVertex");
        }
        //TODO ID or element aas key
        if (!vertices.containsKey(vertex.element())) {
            throw  new InvalidVertexException("Vertex is not part of this graph");
        }
        return gxVertex;

    }

    private GXEdge<E, V> checkEdge(Edge<E, V> edge) throws InvalidEdgeException {
        if (edge == null) {
            throw new InvalidEdgeException("Edge is null");
        }
        GXEdge gxEdge;
        try {
            gxEdge = (GXEdge) edge;
        } catch (ClassCastException e) {
            throw new InvalidEdgeException("Not a GxEdge");
        }

        if (!edges.containsKey(edge.element())) {
            throw new InvalidEdgeException("Edge does not belong to this graph");
        }

        return gxEdge;
    }

}
