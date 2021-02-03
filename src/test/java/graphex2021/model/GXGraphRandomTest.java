package graphex2021.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kmarq
 * @version 1.0 03.02.2021
 */
public class GXGraphRandomTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testGenerateVertices() {
        GXGraph graph = new GXGraphRandom(5, 10, 0, true);
        Assert.assertEquals(5, graph.vertices().size());
    }

    @Test
    public void testGenerateEdges1() {
        GXGraph graph = new GXGraphRandom(2, 10, 100, true);
        Assert.assertEquals(1, graph.edges().size());
    }

    @Test
    public void testGenerateEdges2() {
        GXGraph graph = new GXGraphRandom(4, 10, 60, true);
        Assert.assertEquals(4, graph.vertices());
    }

    @Test
    public void testGenerateEdges3() {
        GXGraph graph = new GXGraphRandom(2, 10, 100, true);
        Assert.assertEquals(2, graph.vertices());
    }
}
