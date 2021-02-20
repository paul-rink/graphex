package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import static org.junit.Assert.*;

public class GXGraphTest {
    private static final File GRAPH_FILE = new File("src/test/resources/GraphData/exampleGraph.json");
    private static final File ISOLATE_GRAPH_FILE = new File("src/test/resources/GraphData/isolatesGraph.json");
    private GXGraph exampleGraph;
    private GXGraph isolateExampleGraph;

    @Before
    public void setUp() throws Exception {
        exampleGraph = new GXGraph(GRAPH_FILE);
        isolateExampleGraph = new GXGraph(ISOLATE_GRAPH_FILE);
    }


    @Test (expected = WrongFileFormatException.class)
    public void testGXGraphConstructorWrongFile() throws WrongFileFormatException {
        GXGraph graph = new GXGraph(new File("src/test/resources/GraphData/noVertices.json"));
    }

    @Test
    public void testGXGraphConstructor() {
        try {
            exampleGraph = new GXGraph(GRAPH_FILE);
        } catch (WrongFileFormatException e) {
            e.printStackTrace();
        }
        assertEquals(14, exampleGraph.vertices().size());
        assertEquals(23, exampleGraph.edges().size());
        assertEquals("S", exampleGraph.getStartingVertex().element());
        assertEquals("Z", exampleGraph.getEndingVertex().element());
    }


    /**
     * Tests the case if null is passed to the insert vertex method. Should return null
     */
    @Test
    public void testInsertVerticesNull() {
        GXVertex vertex = null;
        assertNull(exampleGraph.insertVertex(vertex));
    }

    /**
     * If there already is a vertex with the same ID as the one of the vertex inserted the vertex already in the graph
     * should be returned
     */
    @Test
    public void testInsertVertexIDAlreadyIn() {
        final int vertexNumbers = exampleGraph.numVertices();
        GXVertex vertex = new GXVertex("A", 1, null);

        GXVertex vertex1 = exampleGraph.getVertex(1);
        GXVertex newVertex1 = exampleGraph.insertVertex(vertex);

        //Returned vertex equals the added vertex since its already in
        assertEquals(vertex1, newVertex1);
        //Number of vertices hasn't changed before and after
        assertEquals(vertexNumbers, exampleGraph.numVertices());
    }

    /**
     * Adding a new vertex with an unused id should add the vertex to the graph
     *
     */
    @Test
    public void testInsertVertexNewVertex() {
        final int vertexNumbers = exampleGraph.numVertices();
        GXVertex vertex  = new GXVertex("M", 14, null);
        assertEquals(exampleGraph.insertVertex(vertex), vertex);
        assertEquals(vertex, exampleGraph.getVertex(14));
        //Number of vertices should be one grater than before
        assertEquals(vertexNumbers, exampleGraph.numVertices() - 1);

    }

    /**
     * Adding new Vertex with the same Element as an other vertex
     *
     */
    @Test
    public void testInsertVertexElementAlreadyIn() {
        final int vertexNumbers = exampleGraph.numVertices();
        GXVertex vertex =  new GXVertex("A", 14, null);
        assertEquals(exampleGraph.insertVertex(vertex), vertex);
        assertEquals(vertex, exampleGraph.getVertex(14));
        //Number of vertices should be one grater than before
        assertEquals(vertexNumbers, exampleGraph.numVertices() - 1);
    }

    /**
     * Checking whether the number of edges and vertices are correctly returned for an empty graph
     */
    @Test
    public void testNumVerticesEdgesEmptyGraph() {
        GXGraph test = new GXGraph();
        assertEquals(0, test.numVertices());
        assertEquals(0, test.numEdges());
    }

    /**
     * Tests if the edge with the id of the passed edge is returned if it was already in the graph
     */
    @Test
    public void testInsertEdgeDuplicateID() {
        GXEdge testInsert = new GXEdge(exampleGraph.getVertex(1)
                , exampleGraph.getVertex(5), null, 5, 0 );
        try {
            assertEquals(exampleGraph.getEdge(0), exampleGraph.insertEdge(testInsert));
        } catch (ElementNotInGraphException e) {
            fail();
        }
    }

    /**
     * Checks if the number of edges and vertices remains correct after they are added to an empty graph.
     *
     */
    @Test
    public void testNumVerticesEdgesAdding() {
        GXGraph graph =  new GXGraph();
        //Adding a new Vertex
        graph.insertVertex(new GXVertex("1", 1, null));
        assertEquals(1, graph.numVertices());
        //Adding second vertex
        graph.insertVertex(new GXVertex("2", 2, null));
        assertEquals(2, graph.numVertices());
        try {
            //Adding an edge
            graph.insertEdge(graph.getVertex(1), graph.getVertex(2), "1");
        } catch (ElementNotInGraphException e) {
            fail("vertex wasn't added correctly");
        }
        assertEquals(1, graph.numEdges());
        graph.insertVertex(new GXVertex("3",3, null));
        assertEquals(3, graph.numVertices());
        //Adding second edge
        try {
            graph.insertEdge(graph.getVertex(1), graph.getVertex(3), "2");
        } catch (ElementNotInGraphException e) {
            fail("vertex wasn't added correctly");
        }
        assertEquals(2, graph.numEdges());
        try {
            graph.insertEdge(graph.getVertex(2), graph.getVertex(3), "2");
        } catch (ElementNotInGraphException e) {
            fail("vertex wasn't added correctly");
        }
        assertEquals(3, graph.numEdges());
    }

    /**
     * Checks whether the correct exception is thrown if, the vertex passed to the incidentEdges method is null.
     * @throws ElementNotInGraphException expected since not in graph
     */
    @Test(expected = ElementNotInGraphException.class)
    public void testIncidentEdgesNull() throws ElementNotInGraphException {
        exampleGraph.incidentEdges(null);
    }

    /**
     * Checks whether the correct Exception is thrown when calling incidentEdges with a vertex not in graph
     * @throws ElementNotInGraphException expected since not in graph
     */
    @Test(expected = ElementNotInGraphException.class)
    public void testIncidentEdgesNotInGraph() throws ElementNotInGraphException {
        exampleGraph.incidentEdges(new GXVertex("AB", 100, null));
    }

    /**
     * Incident edges should return empty collection if the vertex passed is an isolate vertex.
     */
    @Test
    public void testIncidentEdgesNoIncidents() {
        GXVertex isolate = isolateExampleGraph.getVertex(1);
        assertNotNull(isolate);
        try {
            assertTrue(isolateExampleGraph.incidentEdges(isolate).isEmpty());
        } catch (ElementNotInGraphException e) {
            fail();
        }
    }

    /**
     * Testing whether all the edges that are incident to a vertex are correctly added returned here
     */
    @Test
    public void testIncidentEdgesAllEdges() {
        //getting the first vertex. should not be null
        GXVertex vert0 = isolateExampleGraph.getVertex(0);
        assertNotNull(vert0);
        //Adding edges to all other vertices.
        for (GXVertex vertex : isolateExampleGraph.vertices()) {
            try {
                //TODO allow null as element
               GXEdge newEdge = isolateExampleGraph.insertEdge(vert0, vertex, "1");
               assertEquals(newEdge, isolateExampleGraph.getEdge(vertex, vert0));
            } catch (ElementNotInGraphException e) {
                fail(vertex.getId() + " not in graph");
            }
        }
        //Same number of vertices as edges for each new vertices
        assertEquals(isolateExampleGraph.numVertices(), isolateExampleGraph.numEdges());
        try {
            //correct number of incident edges
            assertEquals(isolateExampleGraph.numVertices(),
                    isolateExampleGraph.incidentEdges(vert0).size());
        } catch (ElementNotInGraphException e) {
            fail("vert not in Graph");
        }
        //All the edges in incidents visible contain vert0
        try {
            for (GXEdge edge : isolateExampleGraph.incidentEdges(vert0)) {
                assertTrue(edge.contains(vert0));
            }
        } catch (ElementNotInGraphException e) {
            fail();
        }
    }


    /**
     * Elements not in graph check adjacent
      * @throws ElementNotInGraphException
     */
    @Test (expected = ElementNotInGraphException.class)
    public void testAreAdjacentNotInGraph() throws ElementNotInGraphException {
        GXVertex vertexA = new GXVertex("ABC", 1111, null);
        GXVertex vertexB = exampleGraph.getVertex(1);
        exampleGraph.areAdjacent(vertexA, vertexB);
    }

    /**
     * Checks correctly throwing Exception when vertex is null
     * @throws ElementNotInGraphException when vertex is null should be thrown
     */
    @Test (expected = ElementNotInGraphException.class)
    public void testAreAdjacentNull() throws ElementNotInGraphException {
        exampleGraph.areAdjacent(null, null);
    }

    /**
     * A vertex should not be adjacent to itself.
     */
    @Test
    public void testAreAdjacentSameVertex() {
        GXVertex vertex = exampleGraph.getVertex(1);
        try {
            assertFalse(exampleGraph.areAdjacent(vertex, vertex));
        } catch (ElementNotInGraphException e) {
            fail();
        }
    }

    /**
     * Checks if for every edge the vertices are correctly marked as adjacent
     */
    @Test
    public void testAreAdjacentAllEdges() {
        for (GXEdge edge : exampleGraph.edges()) {
            try {
                assertTrue(exampleGraph.areAdjacent(edge.vertices()[0], edge.vertices()[1]));
                assertTrue(exampleGraph.areAdjacent(edge.vertices()[1], edge.vertices()[0]));
            } catch (ElementNotInGraphException e) {
                fail();
            }
        }
    }


    /**
     * Checking that the returned vertices for an empty graph are actually an empty list and not null.
     */
    @Test
    public void testVerticesEmptyGraph() {
        GXGraph emptyGraph = new GXGraph();
        LinkedList<GXVertex> vertices = new LinkedList<>();
        assertEquals(vertices, emptyGraph.vertices() );
    }

    /**
     * Checking for correct exception to be thrown whe the passed vertex is null
     * @throws ElementNotInGraphException if element not in graph
     */
    @Test (expected = ElementNotInGraphException.class)
    public void testOppositeVertexNull() throws ElementNotInGraphException {
        GXEdge edge = exampleGraph.getEdge(1);
        exampleGraph.opposite(null,edge);
    }

    /**
     * Checking for correct behaviour if passed edge is null.
     * @throws ElementNotInGraphException
     */
    @Test (expected = ElementNotInGraphException.class)
    public void testOppositeVertexEdgeNull() throws ElementNotInGraphException {
        GXVertex vertex = exampleGraph.getVertex(0);
        exampleGraph.opposite(vertex, null);
    }

    /**
     * Checks whether null is correctly returned if the edge does not contain the vertex.
     */
    @Test
    public void testOppositeVertexEdgeNotContainingVertex() {
        try {
            assertNull(exampleGraph.opposite(exampleGraph.getVertex(0), exampleGraph.getEdge(5)));
        } catch (ElementNotInGraphException e) {
            fail();
        }
    }

    @Test (expected = ElementNotInGraphException.class)
    public void testOppositeEdgeNotInGraph() throws ElementNotInGraphException {
        GXEdge edge = new GXEdge(exampleGraph.getVertex(0), exampleGraph.getVertex(10)
                , null, 0, 10000);
            exampleGraph.opposite(exampleGraph.getVertex(0), edge);
    }

    /**
     * Tests whether two vertices on an edge are always opposite of each other
     */
    @Test
    public void testOppositeVertexBothVertices() {
        try {
            for (GXEdge edge : exampleGraph.edges()) {
                GXVertex vertex = edge.vertices()[0];
                GXVertex vertex1 = edge.vertices()[1];
                assertEquals(vertex1, exampleGraph.opposite(vertex, edge));
                assertEquals(vertex, exampleGraph.opposite(vertex1, edge));
            }
        } catch (ElementNotInGraphException e) {
            fail("No edge between the vertices");
        }
    }

    /**
     * Tests whether a vertex is correctly removed from graph, as well as its incident edges
     */
    @Test
    public void testRemoveVertex() {
        ArrayList<Integer> removedEdges = new ArrayList<>();
        int vertexToBeRemoved = new Random().nextInt(exampleGraph.numVertices());
        GXVertex vertex = exampleGraph.getVertex(vertexToBeRemoved);
        ArrayList<Integer> incidentEdges = new ArrayList<>();
        try {
            for (GXEdge edge : exampleGraph.incidentEdges(vertex)) {
                incidentEdges.add(edge.getId());
            }
        } catch (ElementNotInGraphException e) {
            fail("incidents somehow wrong");
        }
        try {
            exampleGraph.removeVertex(vertex);
        } catch (ElementNotInGraphException e) {
            fail("vertex not in graph");
        }
        for (Integer id : incidentEdges) {
            //Checking whether the adjacent edges are not in the graph anymore
            assertNull(exampleGraph.getEdge(id));
        }
        //Vertex to be removed not in graph anymore
        assertNull(exampleGraph.getVertex(vertexToBeRemoved));
    }
    /**
     * Checks whether the correct exception is thrown when the removed vertex is null
     * @throws ElementNotInGraphException if the vertex null or not in graph
     */
    @Test (expected = ElementNotInGraphException.class)
    public void testRemoveVertexNull() throws ElementNotInGraphException {
        exampleGraph.removeVertex(null);
    }

    /**
     * Checks whether the correct exception is thrown when the removed vertex is not in the graph
     * @throws ElementNotInGraphException if the vertex null or not in graph
     */
    @Test (expected = ElementNotInGraphException.class)
    public void testRemoveVertexNotInGraph() throws ElementNotInGraphException {
        exampleGraph.removeVertex(new GXVertex("0", -1, null));
    }


    /**
     * Checks whether edges between a passed vertex and a marked neighbour are blocked
     */
    @Test
    public void testBlockCircles() {
        int vertexID = new Random().nextInt(exampleGraph.numVertices());
        GXVertex vert = exampleGraph.getVertex(vertexID);
        vert.mark();
        try {
            for (GXVertex vertex : exampleGraph.getNeighbors(vert)) {
                if (new Random().nextInt(10000) % 2 == 0) {
                    //Randomly marking adjacent vertices
                    vertex.mark();
                }
            }
            exampleGraph.blockCircles(vert);
        } catch (ElementNotInGraphException e) {
            fail("Random vertex not in graph");
        }
        // Checking whether all the edges between the two marked vertices are correctly blocked
        try {
            for (GXEdge edge : exampleGraph.incidentEdges(vert)) {
                if (exampleGraph.opposite(vert, edge).isMarked()) {
                    assertTrue(edge.isBlocked());
                } else {
                    assertFalse(edge.isBlocked());
                }
            }
        } catch (ElementNotInGraphException e) {
            fail("Vertices not correctly in graph");
        }
    }


    @Test (expected = ElementNotInGraphException.class)
    public void testBlockCirclePassedVertexNull() throws ElementNotInGraphException {
        exampleGraph.blockCircles(null);
    }

    @Test (expected = ElementNotInGraphException.class)
    public void testBlockCirclePassedVertexNotInGraph() throws ElementNotInGraphException {
        exampleGraph.blockCircles(new GXVertex(null, -1, null));
    }

    /**
     * If the passed vertex is not marked no adjacent edges should be blocked
     */
    @Test
    public void testBLockCirclePassedVertexNotMarked() {
        int vertexID = new Random().nextInt(exampleGraph.numVertices());
        // Vertex itSelf is not marked
        GXVertex vert = exampleGraph.getVertex(vertexID);
        try {
            for (GXVertex vertex : exampleGraph.getNeighbors(vert)) {
                vertex.mark();
            }
            exampleGraph.blockCircles(vert);
        } catch (ElementNotInGraphException e) {
            fail("Random vertex not in graph");
        }
        // Checking whether all the edges between the two marked vertices are not blocked
        // since the passed vertex is not marked
        try {
            for (GXEdge edge : exampleGraph.incidentEdges(vert)) {
                if (exampleGraph.opposite(vert, edge).isMarked()) {
                    assertFalse(edge.isBlocked());
                }
            }
        } catch (ElementNotInGraphException e) {
            fail("Vertices not correctly in graph");
        }
    }

    @Test
    public void testUnblock() {
        int vertexID = new Random().nextInt(exampleGraph.numVertices());
        GXVertex exampleGraphVertex = exampleGraph.getVertex(vertexID);
        exampleGraphVertex.mark();
        try {
            //Neighbours are marked
            for (GXVertex vertex : exampleGraph.getNeighbors(exampleGraphVertex)) {
                vertex.mark();
            }
            exampleGraph.blockCircles(exampleGraphVertex);
            for (GXVertex vertex : exampleGraph.getNeighbors(exampleGraphVertex)) {
                //Asserting the edges are all blocked as they should be
                assertTrue(exampleGraph.getEdge(vertex, exampleGraphVertex).isBlocked());
            }
            exampleGraph.unblock(exampleGraphVertex);
            for (GXVertex vertex : exampleGraph.getNeighbors(exampleGraphVertex)) {
                //Asserting the edges are now all correctly unblocked
                assertFalse(exampleGraph.getEdge(vertex, exampleGraphVertex).isBlocked());
            }
        } catch (ElementNotInGraphException e) {
            fail("randomVertex not in graph");
        }
    }

    @Test (expected =  ElementNotInGraphException.class)
    public void testUnblockNull() throws ElementNotInGraphException {
        exampleGraph.unblock(null);
    }

    @Test (expected =  ElementNotInGraphException.class)
    public void testUnblockNotInGraph() throws ElementNotInGraphException {
        exampleGraph.unblock(new GXVertex(null, -1, null));
    }

    @Test (expected = ElementNotInGraphException.class)
    public void testSetStartingVertexNull() {
        exampleGraph.setStartingVertex(null);
    }

    @Test (expected = ElementNotInGraphException.class)
    public void testSetStartingVertexNotInGraph() {
        exampleGraph.setStartingVertex(new GXVertex(null, -1, null));
    }

    @Test (expected = ElementNotInGraphException.class)
    public void testSetEndingVertexNull() {
        exampleGraph.setEndingVertex(null);
    }

    @Test (expected = ElementNotInGraphException.class)
    public void testSetEndingVertexNotInGraph() {
        exampleGraph.setEndingVertex(new GXVertex(null, -1, null));
    }




    @After
    public void tearDown() throws Exception {
        exampleGraph = null;
        isolateExampleGraph = null;
    }
}