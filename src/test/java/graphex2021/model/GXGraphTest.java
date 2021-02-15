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
     * Tests the case if null is passed to the insert vertex method
     */
    @Test
    public void testInsertVerticesNulL() {
        GXVertex vertex = null;
        assertNull(exampleGraph.insertVertex(vertex));
    }

    @After
    public void tearDown() throws Exception {
        exampleGraph = null;
    }
}