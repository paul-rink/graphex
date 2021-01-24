package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.*;

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
     * TODO for now ID as key for map ==> Integer
     */
    public GXGraph() {
        this.vertices = new HashMap<Integer, GXVertex>();
        this.edges = new HashMap<Integer, GXEdge>();
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
    public Collection<GXVertex> vertices() {
        Collection<GXVertex> verticeList = new ArrayList<>();
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
    public Collection<GXEdge> edges() {
        List<GXEdge> edgeList = new ArrayList<>();
        edgeList.addAll(edges.values());
        return edgeList;
    }


    @Override
    public Collection<GXEdge> incidentEdges(GXVertex vertex) throws ElementNotInGraphException {
        GXVertex v = checkVertex(vertex);

        List<GXEdge> incident = new ArrayList<>();
        for (GXEdge edge : edges.values()) {
            if (edge.contains(v)) {
                incident.add(edge);
            }
        }
        return incident;
    }

    @Override
    public GXVertex opposite(GXVertex vertex, GXEdge edge) throws ElementNotInGraphException {
        GXVertex v = checkVertex(vertex);
        GXEdge e = checkEdge(edge);

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
    public boolean areAdjacent(GXVertex vertex, GXVertex vertex1) throws ElementNotInGraphException {
        GXVertex gxVertex = checkVertex(vertex);
        GXVertex gxVertex1 = checkVertex(vertex1);

        for (GXEdge edge : edges.values()) {
            if (edge.contains(gxVertex) && edge.contains(gxVertex1))  {
                return true;
            }
        }
        return false;
    }

    @Override
    public GXVertex insertVertex(GXVertex vertex) {
        if (vertexInGraph(vertex.getId())) {
           //TODO how to handle exception here?
        }
        vertices.put(vertex.getId() , vertex);
        return vertex;
    }

    @Override
    public GXEdge insertEdge(GXVertex u, GXVertex v, String edgeElement) throws ElementNotInGraphException {
        return null;
    }

    //TODO maybe different way to insert an edge
    @Override
    public GXEdge insertEdge(GXEdge edge) throws ElementNotInGraphException {
        for (GXVertex vertex : edge.vertices()) {
            // throws element notInGraph if the vertices of the edge are not correct
            checkVertex(vertex);
        }
        if (edgeInGraph(edge.getId())) {
            //TODO how to handle exception here?
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
    public String replace(GXVertex vertex, String element) throws ElementNotInGraphException {
        return null;
    }


    @Override
    public String replace(GXEdge edge, String element) throws ElementNotInGraphException {
        return null;
    }

    //TODO other way to handle error
    @Override
    public void setEdgeVisible(GXVertex vertex) throws ElementNotInGraphException {
        for (GXEdge edge : incidentEdges(vertex)) {
            edge.setVisible(true);
            setVertexVisible(edge, vertex);
        }
    }

    @Override
    public void setVertexVisible(GXEdge edge, GXVertex opposite) throws ElementNotInGraphException {
        //TODO added vertex as param here, so that its easier to find the opposite.
        opposite(opposite, edge).setVisible(true);
    }

    @Override
    public boolean updateDistance() {
        return false;
    }

    @Override
    public void mark(GXEdge edge, GXVertex vertex) throws ElementNotInGraphException {
        //TODO obsolete via mark in DP?
    }

    @Override
    public void blockCircles(GXVertex vertex) throws ElementNotInGraphException {
        for (GXEdge edge : incidentEdges(vertex)) {
            if (opposite(vertex, edge).isMarked()) {
                edge.setBlocked(true);
            }
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
    public Collection<GXVertex> getNeighbors(GXVertex v) {
        List<GXVertex> adjacent = new ArrayList<>();
        try {
            for (GXEdge edge : incidentEdges(v)) {
                adjacent.add((GXVertex) opposite(v, edge));
            }
        } catch (ElementNotInGraphException es) {
            // TODO handle exception better way
        }
        return adjacent;
    }

    /**
     * method to set an vertex to a non marked state
     * @param vertex that should be unmarked
     */
    @Override
    public void unmarkVertex(GXVertex vertex) {
        //TODO should checkVertex() be called?
        vertex.mark(false);
    }

    /**
     * method to set an edge to a non marked state
     * @param edge that should be unmarked
     */
    public void unmarkEdge(GXEdge edge) {
        //TODO should checkEdge() be called?
        edge.mark(false);
    }


    @Override
    public void setVertexInvisible(GXVertex vertex, GXEdge edge) throws ElementNotInGraphException {
        //TODO test this. Really unsure if correct. Unblock edges here?
        //Also
        checkEdge(edge);
        checkVertex(vertex);
        //Checking all the incident edges
        for (GXEdge incidentEdge : incidentEdges(vertex)) {
            //If the edge should not be visible the vertex at the other end needs to be checked
            if (!shouldBeVisible(incidentEdge)) {
                //Set the checked edge invisible
                incidentEdge.setVisible(false);
                //Now to check the opposite vertices of the unmarked vertex
                GXVertex toCheck = opposite(vertex, incidentEdge);
                if (!shouldBeVisible(toCheck)) {
                    //Set the visibility to false, if the vertex has no opposite vertices, that is marked
                    toCheck.setVisible(false);
                }
            }
        }
    }

    @Override
    public void setStartingVertex(GXVertex v) {
        this.startingVertex = v;
    }

    @Override
    public void setEndingVertex(GXVertex v) {
        this.endingVertex = v;
    }

    /**
     * Checks if a vertex is in the graph and actually a GXvertex.
     * TODO other way to do the exception handling
     *
     * @return the GXvertex
     */
    private GXVertex checkVertex(GXVertex vertex) throws InvalidVertexException {
        if (vertex == null) {
            throw new InvalidVertexException("Vertex is null");
        }

        GXVertex gxVertex;
        try {
            gxVertex = vertex;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a GXVertex");
        }
        //TODO ID or element aas key
        if (!vertices.containsKey(vertex.getId())) {
            throw  new InvalidVertexException("Vertex is not part of this graph");
        }
        return gxVertex;

    }

    private GXEdge checkEdge(GXEdge edge) throws InvalidEdgeException {
        if (edge == null) {
            throw new InvalidEdgeException("Edge is null");
        }
        GXEdge gxEdge;
        try {
            gxEdge = (GXEdge) edge;
        } catch (ClassCastException e) {
            throw new InvalidEdgeException("Not a GxEdge");
        }

        if (!edges.containsKey(((GXEdge) edge).getId())) {
            throw new InvalidEdgeException("Edge does not belong to this graph");
        }

        return gxEdge;
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


    private boolean edgeInGraph(Integer id) {
        return edges.containsKey(id);
    }


    private boolean vertexInGraph(Integer id) {
        return vertices.containsKey(id);
    }


    /**
     * Will chekc if an edge should bi visible in the current state of the graph
     *
     * @param edge the edge you want to check whether it should be visible
     * @return true if it should be visible. False else
     */
    private boolean shouldBeVisible(GXEdge edge) throws ElementNotInGraphException {
        checkEdge(edge);
        //An edge can only be visible if at least one of the vertices is marked
        return edge.vertices()[0].isMarked() && edge.vertices()[1].isMarked();
    }

    /**
     * Will check if a vertex should be visible with the current graph state.
     *
     * @param vertex you want to check if it should be visible
     * @return true if it should be visible, false if not
     * @throws ElementNotInGraphException if the vertex was not part of the graph.
     */
    private boolean shouldBeVisible(GXVertex vertex) throws ElementNotInGraphException {
        checkVertex(vertex);
        if (vertex.isMarked()) {
            return true;
        }
        // Checking all the incident edges. A vertex should be visible if it or the opposite vertex is marked
        for (GXEdge edge : incidentEdges(vertex)) {
            if (opposite(vertex, edge).isMarked()) {
                return true;
            }
        }
        // if no opposite vertex is marked it should not be visible
        return false;
    }


}
