package graphex2021.view;

import com.brunomnsilva.smartgraph.graph.*;
import graphex2021.model.ElementNotInGraphException;
import graphex2021.model.GXEdge;
import graphex2021.model.GXGraph;
import graphex2021.model.GXVertex;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Adapter class for {@link GXGraph} to {@link Graph} to be usable in the {@link GraphView}.
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
        Collection<Vertex> vertices = new ArrayList<>(graph.vertices());
        return vertices;
    }

    @Override
    public Collection<Edge> edges() {
        Collection<Edge> edges = new ArrayList<>(graph.edges());
        return edges;
    }

    @Override
    public Collection<Edge> incidentEdges(Vertex v) throws InvalidVertexException {
        Collection<Edge> incidentEdges = null;
        try {
            //TODO better way to call methods on Graph?
            incidentEdges = new ArrayList<>(graph.incidentEdges((GXVertex) v));
        } catch (ElementNotInGraphException eni) {
            throw new InvalidVertexException();
        }
        return incidentEdges;
    }

    @Override
    public Vertex opposite(Vertex v, Edge e) throws InvalidVertexException, InvalidEdgeException {
        try {
            return graph.opposite((GXVertex) v, (GXEdge) e);
        } catch (ElementNotInGraphException eni) {
            //TODO very ugly
            if (eni.getMessage().contains("Vertex")) {
                throw new InvalidVertexException();
            } else {
                throw new InvalidEdgeException();
            }
        }
    }

    @Override
    public boolean areAdjacent(Vertex u, Vertex v) throws InvalidVertexException {
        try {
            return graph.areAdjacent((GXVertex) u, (GXVertex) v);
        } catch (ElementNotInGraphException eni) {
            throw new InvalidVertexException();
        }
    }

    //TODO Kinda not needed from here on
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