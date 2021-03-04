package graphex2021.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DijkstraTest {
    private static final File GRAPH_ID_SWITCH_FILE = new File("src/test/resources/GraphData/testGraphDijkstraIDSwitch.json");
    private static final File GRAPH_UNCONNECTED = new File("src/test/resources/GraphData/test-unconnected-graph.json");
    Dijkstra d;
    GXGraph g;
    List<Step> steps;
    List<Step> userSteps;
    int step;
    DisplayModel dp;


    @Before
    public void setUp() {
        try {
            g = new GXGraph(GRAPH_ID_SWITCH_FILE);
        } catch (WrongFileFormatException e) {
            e.printStackTrace();
        }
        d = new Dijkstra();
        steps = new ArrayList<>();
        userSteps = new LinkedList<>();
        step = 0;
        try {
            dp = new DisplayModel(g, Algo.DIJKSTRA);
        } catch (WrongFileFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * This tests some edge cases of dijkstra, where the distance has to be chosen on id of vertex / edge, because
     * distances are the same. Compare image "testGraphDijkstraIDSwitch.jpg".
     */
    @Test
    public void testDijkstraIdSwitch() {

        steps = d.getSequence(g);
        //1

        Assert.assertEquals("Vertex: ", 1, steps.get(step).getSelectedVertex().getId());
        Assert.assertEquals("Edge: ", 1, steps.get(step).getSelectedEdge().getId());
        step++;
        //2

        Assert.assertEquals("Vertex: ", 2, steps.get(step).getSelectedVertex().getId());
        Assert.assertEquals("Edge: ", 0, steps.get(step).getSelectedEdge().getId());
        step++;
        //3

        Assert.assertEquals("Vertex: ", 3, steps.get(step).getSelectedVertex().getId());
        Assert.assertEquals("Edge: ", 2, steps.get(step).getSelectedEdge().getId());
        step++;
        //4

        Assert.assertEquals("Vertex: ", 4, steps.get(step).getSelectedVertex().getId());
        Assert.assertEquals("Edge: ", 4, steps.get(step).getSelectedEdge().getId());
        step++;
        //5

        Assert.assertEquals("Vertex: ", 5, steps.get(step).getSelectedVertex().getId());
        Assert.assertEquals("Edge: ", 5, steps.get(step).getSelectedEdge().getId());
        step++;

    }

    @Test
    public void testIsRevealed() {
        Assert.assertFalse(d.isRevealed());
    }

    @Test
    public void testHasStartingVertex() {
        Assert.assertTrue(d.hasStartingVertex());
    }

    @Test
    public void testHasEndingVertex() {
        Assert.assertTrue(d.hasEndingVertex());
    }

    /**
     * This tests if the distance to final vertex is correct.
     */
    @Test
    public void testCorrectLength() {
        steps = d.getSequence(g);
        Assert.assertFalse(d.isCorrectDistance(g.getEndingVertex(), 3));
        Assert.assertTrue(d.isCorrectDistance(g.getEndingVertex(), 4));
    }

    /**
     * Dijkstra should perform normal steps until there's no further vertex to reach. Distance to ending vertex is
     * infinity then.
     */
    @Test
    public void testUnconnectedGraph() throws WrongFileFormatException {
        g = new GXGraph(GRAPH_UNCONNECTED);
        steps = d.getSequence(g);


        assertEquals(2, steps.size());
        //1

        Assert.assertEquals("Vertex: ", 2, steps.get(step).getSelectedVertex().getId());
        Assert.assertEquals("Edge: ", 2, steps.get(step).getSelectedEdge().getId());
        step++;
        //2

        Assert.assertEquals("Vertex: ", 3, steps.get(step).getSelectedVertex().getId());
        Assert.assertEquals("Edge: ", 3, steps.get(step).getSelectedEdge().getId());
        step++;

        assertTrue(d.isCorrectDistance(g.getEndingVertex(), -1));
    }

    @After
    public void tearDown() throws Exception {


    }
}