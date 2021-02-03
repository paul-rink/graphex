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
        GXGraph graph = new GXGraphRandom(5, 0);
        Assert.assertEquals(5, graph.vertices().size());
    }
}
