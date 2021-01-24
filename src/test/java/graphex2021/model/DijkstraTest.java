package graphex2021.model;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class DijkstraTest {
    Dijkstra d;
    GXGraph g;
    List<Step> steps;


    @Before
    public void setUp() throws Exception {
        g = new GXGraph();
        GXVertex v1 = new GXVertex("0", 0, null);
        GXVertex v2 = new GXVertex("1", 1, null);
        GXVertex v3 = new GXVertex("2", 2, null);
        GXVertex v4 = new GXVertex("3", 3, null);
        GXVertex v5 = new GXVertex("4", 4, null);
        GXEdge e1 = new GXEdge(v1, v2, "0", 3, 0);
        GXEdge e2 = new GXEdge(v1, v4, "1", 1, 1);
        GXEdge e3 = new GXEdge(v1, v3, "2", 9, 2);
        GXEdge e4 = new GXEdge(v2, v3, "3", 5, 3);
        GXEdge e5 = new GXEdge(v2, v4, "4", 1, 4);
        GXEdge e6 = new GXEdge(v2, v5, "5", 1, 5);
        GXEdge e7 = new GXEdge(v4, v5, "6", 4, 6);
        g.insertVertex(v1);
        g.insertVertex(v2);
        g.insertVertex(v3);
        g.insertVertex(v4);
        g.insertVertex(v5);
        g.insertEdge(e1);
        g.insertEdge(e2);
        g.insertEdge(e3);
        g.insertEdge(e4);
        g.insertEdge(e5);
        g.insertEdge(e6);
        g.insertEdge(e7);
        g.setStartingVertex(v1);
        d = new Dijkstra(g);
    }

    @Test
    public void testDijkstraSequence() {
        steps = d.getSequence(g);
        Step step1 = steps.get(0);
        Step step2 = steps.get(1);
        Step step3 = steps.get(2);
        Step step4 = steps.get(3);
        assertEquals(3,step1.getSelectedVertex().getId());
        assertEquals(1,step2.getSelectedVertex().getId());
        assertEquals(4,step3.getSelectedVertex().getId());
        assertEquals(2,step4.getSelectedVertex().getId());
    }

    @After
    public void tearDown() throws Exception {
    }
}