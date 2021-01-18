package graphex2021.view;

import com.brunomnsilva.smartgraph.graph.*;
import graphex2021.model.GXGraph;

import java.util.Collection;


/**
 * Adapter class for {@link GXGraph} to {@link Graph} to be usable in the {@link GraphView}
 *
 * @author Dominik
 * @version  1.0. 18.01.2021
 */
public class GraphAdapter implements Graph {
    private GXGraph graph;


    @Override
    public int numVertices() {
        return graph.numVertices();
    }

    @Override
    public int numEdges() {
        return graph.numEdges();
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
    public Collection<Edge> incidentEdges(Vertex v) throws InvalidVertexException {
        return null;
    }

    @Override
    public Vertex opposite(Vertex v, Edge e) throws InvalidVertexException, InvalidEdgeException {
        return null;
    }

    @Override
    public boolean areAdjacent(Vertex u, Vertex v) throws InvalidVertexException {
        return false;
    }

    @Override
    public Vertex insertVertex(Object vElement) throws InvalidVertexException {
        return null;
    }

    @Override
    public Edge insertEdge(Vertex u, Vertex v, Object edgeElement) throws InvalidVertexException, InvalidEdgeException {
        return null;
    }

    @Override
    public Edge insertEdge(Object vElement1, Object vElement2, Object edgeElement) throws InvalidVertexException,
            InvalidEdgeException {
        return null;
    }

    @Override
    public Object removeVertex(Vertex v) throws InvalidVertexException {
        return null;
    }

    @Override
    public Object removeEdge(Edge e) throws InvalidEdgeException {
        return null;
    }

    @Override
    public Object replace(Vertex v, Object newElement) throws InvalidVertexException {
        return null;
    }

    @Override
    public Object replace(Edge e, Object newElement) throws InvalidEdgeException {
        return null;
    }
}
