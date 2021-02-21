package graphex2021.model;
import org.junit.*;



import static org.junit.Assert.*;
public class GXEdgeTest {

    private static GXEdge testEdge;
    private static GXVertex testVertex1;
    private static GXPosition testVertex1Position;
    private static GXVertex testVertex2;
    private static GXPosition testVertex2Position;
    private static int testID;
    private static int testWeight;


    @Before
    public void setUp() {
        try {
            testVertex1Position = new GXPosition(1, 1);
            testVertex1 = new GXVertex("A", 1, testVertex1Position);
            testVertex2Position = new GXPosition(2, 2);
            testVertex2 = new GXVertex("B", 2, testVertex2Position);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
        testID = 1;
        testWeight = 4;
        testEdge = new GXEdge(testVertex1, testVertex2, String.valueOf(testWeight), testWeight,testID);
    }

    @After
    public void cleanUp() {
        testVertex1Position = null;
        testVertex1 = null;
        testVertex2Position = null;
        testVertex2 = null;
        testID = 0;
        testWeight = 0;
        testEdge = null;
    }

    @Test
    public void testGetElement () {
        assertEquals(String.valueOf(testWeight), testEdge.element());
    }

    @Test
    public void testGetVertices () {
        GXVertex[] edgeVertices = testEdge.vertices();
        assertTrue(checkSameVertices(edgeVertices));
    }

    private boolean checkSameVertices(GXVertex[] vertexList) {
        boolean oneChecked = false;
        boolean twoChecked = false;
        boolean[] goodVertices = new boolean[vertexList.length];
        for (int i = 0; i < goodVertices.length; i++) {
            goodVertices[i] = false;
        }
        for (int i = 0; i < vertexList.length; i++) {
            if(!oneChecked) {
                if(testVertex1.equals(vertexList[i])) {
                    oneChecked = true;
                    goodVertices[i] = true;
                }
            } else if(!twoChecked) {
                if(testVertex2.equals(vertexList[i])) {
                    oneChecked = true;
                    goodVertices[i] = true;
                }
            }
        }
        boolean check = true;
        for (int i = 0; i < goodVertices.length; i++) {
            if (!goodVertices[i]) {
                check = false;
            }
        }
        return check;
    }

    @Test
    public void testGetWeight () {
        assertEquals(testWeight, testEdge.getWeight());
    }

    @Test
    public void testDefaultBlocked () {
        assertFalse(testEdge.isBlocked());
    }

    @Test
    public void testDefaultMarked () {
        assertFalse(testEdge.isMarked());
    }

    @Test
    public void testGetNextVertexBothUnmarked () {
        assertNull(testEdge.getNextVertex());
    }

    @Test
    public void testGetNextVertexBothMarked () {
        testVertex1.mark(4, testEdge);
        testVertex2.mark(3, testEdge);
        assertNull(testEdge.getNextVertex());
    }

    @Test
    public void testGetNextVertexOneMarked () {
        testVertex1.mark(4, testEdge);
        assertEquals(testVertex2, testEdge.getNextVertex());
    }

    @Test
    public void testGetNextVertexTwoMarked () {
        testVertex2.mark(3, testEdge);
        assertEquals(testVertex1, testEdge.getNextVertex());
    }

    @Test
    public void testMarkUnMark () {
        testEdge.mark();
        boolean check1 = testEdge.isMarked() == true;
        testEdge.unmark();
        boolean check2 = testEdge.isMarked() == false;
        assertTrue(check1 && check2);
    }

    @Test
    public void testContainsValid () throws ElementNotInGraphException {
        assertTrue(testEdge.contains(testVertex1));
    }

    @Test
    public void testContainsNotContained () throws ElementNotInGraphException {
        GXPosition pos = null;
        try {
            pos = new GXPosition(4,5);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
        assertFalse(testEdge.contains(new GXVertex("C", 3, pos)));
    }

    @Test(expected = ElementNotInGraphException.class)
    public void testContainsNull () throws ElementNotInGraphException {
        testEdge.contains(null);
    }

    @Test
    public void testSetBlocked () {
        testEdge.setBlocked(true);
        boolean check1 = testEdge.isBlocked() == true;
        testEdge.setBlocked(false);
        boolean check2 = testEdge.isBlocked() == false;
        assertTrue(check1 && check2);
    }

    @Test
    public void testSetVisible () {
        testEdge.setVisible(true);
        boolean check1 = testEdge.isVisible() == true;
        testEdge.setVisible(false);
        boolean check2 = testEdge.isVisible() == false;
        assertTrue(check1 && check2);
    }

    @Test
    public void testGetID () {
        assertEquals(testID, testEdge.getId());
    }

    @Test
    public void testSetWeight () {
        int newWeight = 7;
        testEdge.setWeight(newWeight);
        assertEquals(newWeight, testEdge.getWeight());
    }

    @Test
    public void testSetHint () {
        testEdge.setHint(true);
        boolean check1 = testEdge.isHint() == true;
        testEdge.setHint(false);
        boolean check2 = testEdge.isHint() == false;
        assertTrue(check1 && check2);
    }

    @Test
    public void testSetHighlighted () {
        testEdge.setHighlighted(true);
        boolean check1 = testEdge.isHighlighted() == true;
        testEdge.setHighlighted(false);
        boolean check2 = testEdge.isHighlighted() == false;
        assertTrue(check1 && check2);
    }

    @Test
    public void testGetNextDistanceNoMark() {
        assertEquals(-1, testEdge.getNextDistance());
    }

    @Test
    public void testGetNextDistance() {
        markVertexOne();
        assertEquals(12, testEdge.getNextDistance());
    }

    @Test
    public void testGetNextDistanceMarked() {
        markVertexOne();
        testEdge.mark();
        assertEquals(-1, testEdge.getNextDistance());
    }

    @Test
    public void testGetNextDistanceBlocked() {
        markVertexOne();
        testEdge.setBlocked(true);
        assertEquals(-1, testEdge.getNextDistance());
        }

    private void markVertexOne() {
        GXPosition pos = null;
        try {
            pos = new GXPosition(3, 4);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
        GXVertex prevVertex1 = new GXVertex("C", 4, pos);
        GXEdge prevEdge = new GXEdge(prevVertex1, testVertex1, "3", 3, 4);
        testVertex1.mark(5,prevEdge);
    }

    @Test
    public void testOppositeOne() {
        assertEquals(testVertex2, testEdge.opposite(testVertex1));
    }

    @Test
    public void testOppositeTwo() {
        assertEquals(testVertex1, testEdge.opposite(testVertex2));
    }

    @Test
    public void testOppositeNull() {
        assertNull(testEdge.opposite(null));
    }

    @Test
    public void testOppositeWrongVertex() {
        GXPosition pos = null;
        try {
            pos = new GXPosition(3, 4);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
        GXVertex otherVertex = new GXVertex("C", 4, pos);
        assertNull(testEdge.opposite(otherVertex));
    }

    @Test
    public void testEqualsSame() {
        assertTrue(testEdge.equals(testEdge));
    }

    @Test
    public void testEqualsIdentical() {
        GXEdge identicalEdge = createIdenticalEdge();
        assertTrue(testEdge.equals(identicalEdge));
    }

    @Test
    public void testEqualsNotEqual() {
        GXEdge otherEdge = createOtherEdge();
        assertFalse(testEdge.equals(otherEdge));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(testEdge.equals(null));
    }

    private GXEdge createIdenticalEdge() {
        GXPosition pos1 = null;
        try {
            pos1 = new GXPosition(1,1);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
        GXVertex v1 = new GXVertex("A", 1, pos1);
        GXPosition pos2 = null;
        try {
            pos2 = new GXPosition(2,2);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
        GXVertex v2 = new GXVertex("B",2, pos2);
        int id = 1;
        int weight = 4;
        return new GXEdge(v1, v2, String.valueOf(weight), weight, id);
    }

    private GXEdge createOtherEdge() {
        GXPosition pos1 = null;
        try {
            pos1 = new GXPosition(1,1);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
        GXVertex v1 = new GXVertex("C", 3, pos1);
        GXPosition pos2 = null;
        try {
            pos2 = new GXPosition(2,2);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
        GXVertex v2 = new GXVertex("D",4, pos2);
        int id = 5;
        int weight = 3;
        return new GXEdge(v1, v2, String.valueOf(weight), weight, id);
    }
}
