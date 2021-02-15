package graphex2021.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GXGraphTest {
    private static final File graphFile = new File("src/test/resources/GraphData/exampleGraph.json");
    GXGraph exampleGraph;

    @Before
    public void setUp() throws Exception {
        exampleGraph = new GXGraph(graphFile);
    }

    @Test
    public void testGXGraphConstructor() {
        try {
            exampleGraph = new GXGraph(graphFile);
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
    public void testNumVerticesEdgesEmptyGraph(){
        GXGraph test = new GXGraph();
        assertEquals(0, test.numVertices());
        assertEquals(0, test.numEdges());
    }


   

    @After
    public void tearDown() throws Exception {
        exampleGraph = null;
    }
}