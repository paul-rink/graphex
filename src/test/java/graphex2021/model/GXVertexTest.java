package graphex2021.model;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class GXVertexTest {
    private static GXVertex testVertex;
    private static GXVertex secondVertex;
    private static GXEdge prevEdge;
    @Before
    public void setup() {
        //create a Vertex with expected values
        testVertex = new GXVertex("A", 5,new GXPosition(3,5));
        secondVertex = new GXVertex("B", 3, new GXPosition(2,3));
        prevEdge = new GXEdge(testVertex, secondVertex, "4", 4, 1);
    }

    @After
    public void cleanUp() {
        testVertex = null;
    }


    @Test
    public void testGetElement() {
        assertEquals("A",testVertex.element());
    }

    @Test
    public void testDefaultVisible() {
        assertFalse(testVertex.isVisible());
    }

    @Test
    public void testSetVisible() {
        testVertex.setVisible(true);
        assertTrue(testVertex.isVisible());
    }

    @Test
    public void testDefaultIsMarked() {
        assertFalse(testVertex.isMarked());
    }

    @Test
    public void testMark() {
        testVertex.mark(3, prevEdge);
        assertTrue(checkMark());
    }

    private boolean checkMark() {
        boolean result = true;
        if(!testVertex.isMarked()) {
            result = false;
        }
        if(testVertex.getCurrentDistance()!=7) {
            result = false;
        }
        if(!prevEdge.equals(testVertex.getPrevious())) {
            result = false;
        }
        return result;
    }

    @Test
    public void testUnMark() {
        testVertex.mark(3, prevEdge);
        testVertex.unmark();
        assertTrue(checkUnmark());
    }

    private boolean checkUnmark() {
        boolean result = true;
        if(testVertex.isMarked()) {
            result = false;
        }
        if(testVertex.getPrevious()!= null) {
            result = false;
        }
        if(testVertex.getCurrentDistance()!= -1) {
            result = false;
        }
        return result;
    }

}
