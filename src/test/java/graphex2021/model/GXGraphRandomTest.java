package graphex2021.model;

import org.junit.*;

import javax.swing.*;

import static junit.framework.TestCase.fail;

/**
 * Some test classes for generating a random {@link GXGraph}.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 03.02.2021
 */
public class GXGraphRandomTest {
    GXGraph graph;

    @Before
    public void setUp() {

    }

    @Test
    public void testGenerateInvalidRandomGraph() {
        try {
            graph = new GXGraphRandom(GXGraphRandom.MAX_NUMBER_VERTICES + 1, 10, 10, true, true);
        } catch (IllegalArgumentException e) {
            System.out.printf("Test1");
            Assert.assertEquals("invalid number of vertices", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(GXGraphRandom.MIN_NUMBER_VERTICES - 1, 10, 10, true, true);
        } catch (IllegalArgumentException e) {
            System.out.printf("Test2");
            Assert.assertEquals("invalid number of vertices", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(10, GXGraphRandom.MAX_EDGE_WEIGHT + 1, 10, true, true);
        } catch (IllegalArgumentException e) {
            System.out.printf("Test3");
            Assert.assertEquals("Invalid edge weights", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(10, GXGraphRandom.MIN_EDGE_WEIGHT - 1, 10, true, true);
        } catch (IllegalArgumentException e) {
            System.out.printf("Test4");
            Assert.assertEquals("Invalid edge weights", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(10, 10, GXGraphRandom.MIN_EDGE_PROBABILITY - 1, true, true);
        } catch (IllegalArgumentException e) {
            System.out.printf("Test5");
            Assert.assertEquals("invalid probability", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(10, 10, GXGraphRandom.MAX_EDGE_PROBABILITY + 1, true, true);
        } catch (IllegalArgumentException e) {
            System.out.printf("Test6");
            Assert.assertEquals("invalid probability", e.getMessage());
        }
    }

    @Test
    public void testGenerateTotalConnectedRndGraph() {
        graph = new GXGraphRandom(5, 10, GXGraphRandom.MAX_EDGE_PROBABILITY, true, true);
        Assert.assertEquals(5, graph.vertices().size());
        Assert.assertEquals(15, graph.edges().size());
    }
    @Ignore
    public void testGenerateEdges2() {
        GXGraph graph = new GXGraphRandom(4, 10, 60, true, true);
        Assert.assertEquals(4, graph.vertices().size());
    }

    @Test
    public void testGenerateEdges3() {
        GXGraph graph = new GXGraphRandom(2, 10, 100, true, true);
        Assert.assertEquals(2, graph.vertices().size());
    }
}
