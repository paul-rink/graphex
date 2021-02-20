package graphex2021.model;

import java.io.File;
import java.util.*;

/**
 *This class models the logic of the graph a user is interacting with and a {@link Algorithm} executes at. Graphs
 * consist of {@link GXVertex} and {@link GXEdge}. One {@link GXVertex} should be set as a {@code startingVertex} and
 * one as a {@code endingVertex} which is required for some {@link Algorithm}.
 *
 *
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GXGraph implements GraphInterface<String, String> {


    /**
     * The starting and end vertices if needed by the algorithm used
     */
    private GXVertex startingVertex;
    private GXVertex endingVertex;

    /**
     * Map containing all the vertices in the graph. Vertex Id as
     */
    private final Map<Integer, GXVertex> vertices;
    /**
     * Map containing all the edges in the graph
     */
    private final Map<Integer, GXEdge> edges;


    /**
     * Empty constructor just initialising the attributes
     */
    public GXGraph() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        this.startingVertex = null;
        this.endingVertex = null;
    }

    public GXGraph(File file) throws WrongFileFormatException {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        GraphParser parser = GraphParser.getGraphParser();
        for (GXVertex vertex : parser.parseVertices(file)) {
            insertVertex(vertex);
        }

        for (GXEdge edge : parser.parseEdges(file, vertices())) {
            try {
                insertEdge(edge);
            } catch (ElementNotInGraphException e) {
                throw new WrongFileFormatException("Wrong edges in file: "+ file.getAbsolutePath());
            }
        }
        this.startingVertex = parser.parseStarting(file, vertices());
        this.endingVertex = parser.parseEnding(file, vertices());

        startingVertex.setType(GXVertexType.STARTING);
        endingVertex.setType(GXVertexType.ENDING);

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
     * Problem is that in case you would use Collection<GXVertex> you would also be able to add Vertex to list.
     * List GXVertex is not a List Vertex since you could potentially insert a different type of Vertex into the list.
     *
     * @return ArrayList containing all the vertices of the Graph. For now can be typecast to GXVertex
     */
    @Override
    public Collection<GXVertex> vertices() {
        Collection<GXVertex> verticeList = new ArrayList<>();
        verticeList.addAll(vertices.values());
        return verticeList;
    }



    /**
     * Returns a list containing all the edges that are part of the graph.
     *
     * @return ArrayList containing all the vertices of the Graph. For now can be typecast to GXEdge
     */
    @Override
    public Collection<GXEdge> edges() {
        List<GXEdge> edgeList = new ArrayList<>();
        edgeList.addAll(edges.values());
        return edgeList;
    }


    @Override
    public Collection<GXEdge> incidentEdges(GXVertex vertex) throws ElementNotInGraphException {
        checkVertex(vertex);

        List<GXEdge> incident = new ArrayList<>();
        for (GXEdge edge : edges.values()) {
            if (edge.contains(vertex)) {
                incident.add(edge);
            }
        }
        return incident;
    }

    @Override
    public GXVertex opposite(GXVertex vertex, GXEdge edge) throws ElementNotInGraphException {
        checkVertex(vertex);
        checkEdge(edge);

        if (!edge.contains(vertex)) {
            // Vertex is not part of the passed edge
            return null;
        }

        if (edge.vertices()[0] == vertex) {
            return edge.vertices()[1];
        } else {
            return edge.vertices()[0];
        }
    }

    @Override
    public boolean areAdjacent(GXVertex vertex, GXVertex vertex1) throws ElementNotInGraphException {
        checkVertex(vertex);
        checkVertex(vertex1);
        if (vertex.getId() != vertex1.getId()) {
            for (GXEdge edge : edges.values()) {
                if (edge.contains(vertex) && edge.contains(vertex1)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public GXVertex insertVertex(GXVertex vertex) {
        if (vertex != null) {
            if (vertexInGraph(vertex.getId())) {
                return vertices.get(vertex.getId());
            } else {
                vertices.put(vertex.getId(), vertex);
            }
            return vertex;
        }
        return null;
    }

    //TODO maybe different way to insert an edge
    @Override
    public GXEdge insertEdge(GXVertex u, GXVertex v, String edgeElement) throws ElementNotInGraphException {
        try {
            return getEdge(u, v);
        } catch (ElementNotInGraphException enig) {
            GXEdge edge =  new GXEdge(u, v, edgeElement, Integer.parseInt(edgeElement), edges.size());
            edges.put(edges.size(), edge);
            return edge;
        }
    }

    @Override
    public GXEdge insertEdge(GXEdge edge) throws ElementNotInGraphException {
        for (GXVertex vertex : edge.vertices()) {
            // throws element notInGraph if the vertices of the edge are not correct
            checkVertex(vertex);
        }
        if (edgeInGraph(edge.getId())) {
            return edges.get(edge.getId());
        }
        edges.put(edge.getId(), edge);
        return edge;
    }




    @Override
    public String removeVertex(GXVertex vertex) throws ElementNotInGraphException {
        checkVertex(vertex);
        for (GXEdge edge : incidentEdges(vertex)) {
            removeEdge(edge);
        }
        vertices.remove(vertex.getId());
        return vertex.element();
    }

    @Override
    public String removeEdge(GXEdge edge) throws ElementNotInGraphException {
        checkEdge(edge);
        edges.remove(edge.getId());
        return edge.element();
    }

    @Override
    public void blockCircles(GXVertex vertex) throws ElementNotInGraphException {
        checkVertex(vertex);
        if (vertex.isMarked()) {
            for (GXEdge edge : incidentEdges(vertex)) {
                if (opposite(vertex, edge).isMarked()) {
                    edge.setBlocked(true);
                }
            }
        }
    }

    /**
     * method to unblock all edges incident to a vertex
     * (you can unblock all as the vertex calling this is being unmarked and as a result
     * the edges wont be blocked by this vertex anymore
     * @param vertex the vertex whose edges should be unblocked
     * @throws ElementNotInGraphException if the vertex is not in the graph
     */
    public void unblock(GXVertex vertex) throws ElementNotInGraphException {
        checkVertex(vertex);
        for (GXEdge edge : incidentEdges(vertex)) {
            edge.setBlocked(false);
        }
    }

    @Override
    public GXVertex getStartingVertex() {
        return startingVertex;
    }

    @Override
    public GXVertex getEndingVertex() {
        return endingVertex;
    }

    @Override
    public Collection<GXVertex> getNeighbors(GXVertex v) throws ElementNotInGraphException {
        List<GXVertex> adjacent = new ArrayList<>();
        for (GXEdge edge : incidentEdges(v)) {
            adjacent.add((GXVertex) opposite(v, edge));
        }
        return adjacent;
    }

    @Override
    public void setStartingVertex(GXVertex v) throws ElementNotInGraphException {
        checkVertex(v);
        this.startingVertex = v;
        this.startingVertex.setType(GXVertexType.STARTING);
    }

    @Override
    public void setEndingVertex(GXVertex v) throws ElementNotInGraphException {
        checkVertex(v);
        this.endingVertex = v;
        this.endingVertex.setType(GXVertexType.ENDING);
    }

    /**
     * Checks if a vertex is in the graph.
     *
     * @param vertex you want to check
     * @return true if the {@link GXVertex} is in the graph
     * @throws ElementNotInGraphException if not an allowed {@link GXVertex}
     */
    private boolean checkVertex(GXVertex vertex) throws ElementNotInGraphException {
        if (vertex == null) {
            throw new ElementNotInGraphException("Vertex is null");
        }
        if (!vertices.containsKey(vertex.getId())) {
            throw new ElementNotInGraphException("Vertex is not part of this graph");
        }
        return true;

    }


    /**
     * Checks if a {@link GXEdge} is in the graph.
     *
     * @param edge you want to check
     * @return true if the {@link GXEdge} is in the graph
     * @throws ElementNotInGraphException if not an allowed {@link GXEdge}
     */
    private boolean checkEdge(GXEdge edge) throws ElementNotInGraphException {
        if (edge == null) {
            throw new ElementNotInGraphException("Edge is null");
        }

        if (!edges.containsKey(((GXEdge) edge).getId())) {
            throw new ElementNotInGraphException("Edge does not belong to this graph");
        }

        return true;
    }

    /**
     * Returns for two given vertices the corresponding edge in the graph. The order of the given vertices does not
     * matter
     *
     * @param v 1st {@link GXVertex}of the edge you are looking for
     * @param u 2nd {@link GXVertex} of the edge you are looking for
     * @return the {@link GXEdge} containing {@code v} and {@code u}
     * @throws ElementNotInGraphException if the graph does not contain a {@link GXEdge} with those vertices.
     */
    public GXEdge getEdge(GXVertex v, GXVertex u) throws ElementNotInGraphException {
        for (GXEdge edge : incidentEdges(v)) {
            if (edge.contains(u)) {
                return edge;
            }
        }
        throw new ElementNotInGraphException("there is no Edge inbetween these vertices");
    }

    /**
     * Returns {@link GXEdge} of the graph with a specific id.
     * @param id is the edge you're looking for
     * @return the edge if the graph contains an edge with this id, if the graph does not contain an edge with this id
     * it will return {@code null}
     */
    public GXEdge getEdge(int id) {
        return edges.get(id);
    }

    /**
     * Returns {@link GXVertex} of the graph with a specific id.
     * @param id is the vertex you're looking for
     * @return the vertex if the graph contains a vertex with this id, if the graph does not contain a vertex with this
     * id it will return {@code null}
     */
    public GXVertex getVertex(int id) {
        return vertices.get(id);
    }


    private boolean edgeInGraph(Integer id) {
        return edges.containsKey(id);
    }


    private boolean vertexInGraph(Integer id) {
        return vertices.containsKey(id);
    }
    

}
