package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.*;

import java.util.Collection;

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


    @Override
    public int numVertices() {
        return 0;
    }

    @Override
    public int numEdges() {
        return 0;
    }

    @Override
    public Collection<Vertex<V>> vertices() {
        return null;
    }

    @Override
    public Collection<Edge<E, V>> edges() {
        return null;
    }

    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex vertex) throws InvalidVertexException {
        return null;
    }

    @Override
    public Vertex opposite(Vertex vertex, Edge edge) throws InvalidVertexException, InvalidEdgeException {
        return null;
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
    public boolean setEdgeVisible(GXVertex vertex) {
        return false;
    }

    @Override
    public boolean setVertexVisible(GXEdge edge) {
        return false;
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
}
