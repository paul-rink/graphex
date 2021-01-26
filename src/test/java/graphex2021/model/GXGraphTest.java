package graphex2021.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GXGraphTest {
    private static final File graphFile = new File("src/test/resources/GraphData/exampleGraph.json");
    GXGraph g;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGXGraphConstructor() {
        try {
            g = new GXGraph(graphFile);
        } catch (ElementNotInGraphException e) {
            e.printStackTrace();
        }
        assertEquals(14,g.vertices().size());
        assertEquals(23,g.edges().size());
        assertEquals("S", g.getStartingVertex().element());
        assertEquals("Z", g.getEndingVertex().element());
    }

    @After
    public void tearDown() throws Exception {
    }
}