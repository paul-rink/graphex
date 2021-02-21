package graphex2021.model;
import org.junit.*;


import static org.junit.Assert.*;

public class GXVertexTest {
    private static GXVertex testVertex;
    private static GXPosition testPosition;
    private static GXVertex secondVertex;
    private static GXEdge prevEdge;

    @Before
    public void setup() {
        //create a Vertex with expected values
        testPosition = new GXPosition(3,5);
        testVertex = new GXVertex("A", 5,testPosition);
        secondVertex = new GXVertex("B", 3, new GXPosition(2,3));
        prevEdge = new GXEdge(testVertex, secondVertex, "4", 4, 1);
    }

    @After
    public void cleanUp() {
        testPosition = null;
        testVertex = null;
        secondVertex = null;
        prevEdge = null;
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
    public void testDefaultDistance() {
        assertEquals(0, testVertex.getCurrentDistance());
    }

    @Test
    public void testMark() {
        testVertex.mark(3, prevEdge);
        assertTrue(checkMark());
    }

    @Test (expected = NullPointerException.class)
    public void testMarkNull() {
        testVertex.mark(3, null);
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

    @Test
    public void testSetCurrentDistance() {
        testVertex.setCurrentDistance(1);
        assertEquals(1, testVertex.getCurrentDistance());
    }

    @Test
    public void testGetId() {
        assertEquals(5, testVertex.getId());
    }

    @Test
    public void testGetPosition() {
        assertTrue(testPosition.equals(testVertex.getPosition()));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(testVertex.equals(null));
    }

    @Test
    public void testEquals() {
        assertTrue(testVertex.equals(new GXVertex("A", 5, new GXPosition(3,3))));
    }

    @Test
    public void testDefaultType() {
        GXVertexType testType = testVertex.getStartOrEnd();
        assertEquals(GXVertexType.NORMAL, testType);
    }
    @Test
    public void testSetType() {
        testVertex.setType(GXVertexType.STARTING);
        assertEquals(GXVertexType.STARTING, testVertex.getStartOrEnd());
    }

    @Test //TODO what is the expected behaviour here?
    public void testSetTypeNull() {
        testVertex.setType(null);
        //assertEquals(GXVertexType.NORMAL, testVertex.getStartOrEnd());
        assertEquals(null, testVertex.getStartOrEnd());
    }

    @Test
    public void testDefaultIsHint() {
        assertFalse(testVertex.isHint());
    }

    @Test
    public void testSetHint() {
        testVertex.setHint(true);
        assertTrue(testVertex.isHint());
    }
}
