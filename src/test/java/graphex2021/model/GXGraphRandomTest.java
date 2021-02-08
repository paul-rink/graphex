package graphex2021.model;

import org.junit.*;

/**
 * Some test classes for generating a random {@link GXGraph}.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 03.02.2021
 */
public class GXGraphRandomTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testGenerateVertices() {
        GXGraph graph = new GXGraphRandom(5, 10, 0, true, true);
        Assert.assertEquals(5, graph.vertices().size());
    }

    @Test
    public void testGenerateEdges1() {
        GXGraph graph = new GXGraphRandom(2, 10, 100, true, true);
        Assert.assertEquals(1, graph.edges().size());
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
