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
            System.out.print("Test1");
            Assert.assertEquals("invalid number of vertices", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(GXGraphRandom.MIN_NUMBER_VERTICES - 1, 10, 10, true, true);
        } catch (IllegalArgumentException e) {
            System.out.print("Test2");
            Assert.assertEquals("invalid number of vertices", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(10, GXGraphRandom.MAX_EDGE_WEIGHT + 1, 10, true, true);
        } catch (IllegalArgumentException e) {
            System.out.print("Test3");
            Assert.assertEquals("Invalid edge weights", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(10, GXGraphRandom.MIN_EDGE_WEIGHT - 1, 10, true, true);
        } catch (IllegalArgumentException e) {
            System.out.print("Test4");
            Assert.assertEquals("Invalid edge weights", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(10, 10, GXGraphRandom.MIN_EDGE_PROBABILITY - 1, true, true);
        } catch (IllegalArgumentException e) {
            System.out.print("Test5");
            Assert.assertEquals("invalid probability", e.getMessage());
        }
        try {
            graph = new GXGraphRandom(10, 10, GXGraphRandom.MAX_EDGE_PROBABILITY + 1, true, true);
        } catch (IllegalArgumentException e) {
            System.out.print("Test6");
            Assert.assertEquals("invalid probability", e.getMessage());
        }
    }

    @Test
    public void testGenerateTotalConnectedRndGraph() {
        graph = new GXGraphRandom(5, 10, GXGraphRandom.MAX_EDGE_PROBABILITY, true, true);
        Assert.assertEquals(5, graph.vertices().size());
        Assert.assertEquals(10, graph.edges().size());
        for (GXEdge edge : graph.edges()) {
            Assert.assertTrue(edge.getWeight() <= 10);
        }

        graph = new GXGraphRandom(26, 10, GXGraphRandom.MAX_EDGE_PROBABILITY, true, true);
        Assert.assertEquals(26, graph.vertices().size());
        Assert.assertEquals(325, graph.edges().size());
        for (GXEdge edge : graph.edges()) {
            Assert.assertTrue(edge.getWeight() <= 10);
        }
    }

    @Test
    public void testMinConnectedRndGraph() {
        graph = new GXGraphRandom(GXGraphRandom.MAX_NUMBER_VERTICES, GXGraphRandom.MAX_EDGE_WEIGHT, GXGraphRandom.MIN_EDGE_PROBABILITY, true, true);
        Assert.assertEquals(GXGraphRandom.MAX_NUMBER_VERTICES, graph.vertices().size());
    }

    @Test
    public void testMinVertices() {
        //totally connected because of p
        graph = new GXGraphRandom(GXGraphRandom.MIN_NUMBER_VERTICES, GXGraphRandom.MAX_EDGE_WEIGHT, GXGraphRandom.MAX_EDGE_PROBABILITY, true, true);
        Assert.assertEquals(GXGraphRandom.MIN_NUMBER_VERTICES, graph.vertices().size());
        Assert.assertEquals(1, graph.edges().size());
        Assert.assertEquals(GXGraphRandom.MIN_NUMBER_VERTICES, graph.vertices().size());

        //2 vertices are connected, because isolates are not allowed
        graph = new GXGraphRandom(GXGraphRandom.MIN_NUMBER_VERTICES, GXGraphRandom.MAX_EDGE_WEIGHT, GXGraphRandom.MIN_EDGE_PROBABILITY, false, true);
        Assert.assertEquals(GXGraphRandom.MIN_NUMBER_VERTICES, graph.vertices().size());
        Assert.assertEquals(1, graph.edges().size());
        Assert.assertEquals(GXGraphRandom.MIN_NUMBER_VERTICES, graph.vertices().size());
    }




}
