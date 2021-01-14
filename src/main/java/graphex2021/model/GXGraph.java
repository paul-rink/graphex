package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.*;

import java.util.Collection;

/**
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GXGraph<V,E> implements Graph {


    @Override
    public int numVertices() {
        return 0;
    }

    @Override
    public int numEdges() {
        return 0;
    }

    @Override
    public Collection<Vertex> vertices() {
        return null;
    }

    @Override
    public Collection<Edge> edges() {
        return null;
    }

    @Override
    public Collection<Edge> incidentEdges(Vertex vertex) throws InvalidVertexException {
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
    public Edge insertEdge(Vertex vertex, Vertex vertex1, Object o) throws InvalidVertexException, InvalidEdgeException {
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
}
