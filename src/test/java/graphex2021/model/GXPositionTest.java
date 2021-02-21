package graphex2021.model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GXPositionTest {
    private static GXPosition testPosition;
    private static int defaultPosX;
    private static int defaultPosY;


    @Before
    public void setUp() {
        defaultPosX = 1;
        defaultPosY = 3;
    }
    private void constructPosition(int x, int y) {
        try {
            testPosition = new GXPosition(x, y);
        } catch (NonValidCoordinatesException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        defaultPosX = 0;
        defaultPosY = 0;
        testPosition = null;
    }

    @Test (expected = NonValidCoordinatesException.class)
    public void testConstructorNegativeX() throws NonValidCoordinatesException {
        testPosition = new GXPosition(-1, 0);
    }

    @Test (expected = NonValidCoordinatesException.class)
    public void testConstructorNegativeY() throws NonValidCoordinatesException {
        testPosition = new GXPosition(1, -1);
    }

    @Test (expected = NonValidCoordinatesException.class)
    public void testConstructortooBigX() throws NonValidCoordinatesException {
        testPosition = new GXPosition(1001, 3);
    }

    @Test (expected = NonValidCoordinatesException.class)
    public void testConstructortooBigY() throws NonValidCoordinatesException {
        testPosition = new GXPosition(999, 1001);
    }

    @Test
    public void testSetPositionValidInt() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        int newX = 4;
        int newY = 6;
        testPosition.setPosition(newX, newY);
        int[] positions = testPosition.getPosition();
        assertEquals(newX,positions[0]);
        assertEquals(newY, positions[1]);
    }
    @Test (expected = NonValidCoordinatesException.class)
    public void testSetPositionNegativeXInt() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        int newX = -4;
        int newY = 6;
        testPosition.setPosition(newX, newY);
    }
    @Test (expected = NonValidCoordinatesException.class)
    public void testSetPositionNegativeYInt() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        int newX = 4;
        int newY = -6;
        testPosition.setPosition(newX, newY);
    }
    @Test (expected = NonValidCoordinatesException.class)
    public void testSetPositionLargeXInt() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        int newX = 1001;
        int newY = 10;
        testPosition.setPosition(newX, newY);
    }
    @Test (expected = NonValidCoordinatesException.class)
    public void testSetPositionLargeYInt() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        int newX = 4;
        int newY = 1001;
        testPosition.setPosition(newX, newY);
    }

    @Test
    public void testSetPositionValidDouble() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        double newX = 0.0;
        double newY = 999.9;
        testPosition.setPosition(newX, newY);
        int[] positions = testPosition.getPosition();
        assertEquals(0,positions[0]);
        assertEquals(1000, positions[1]);
    }
    @Test (expected = NonValidCoordinatesException.class)
    public void testSetPositionLargeXDouble() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        double newX = 1000.5;
        double newY = 999.0;
        testPosition.setPosition(newX, newY);
    }
    @Test (expected = NonValidCoordinatesException.class)
    public void testSetPositionLargeYDouble() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        double newX = 999.5;
        double newY = 1000.5;
        testPosition.setPosition(newX, newY);
    }
    @Test (expected = NonValidCoordinatesException.class)
    public void testSetPositionNegativeXDouble() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        double newX = -1.0;
        double newY = 999.9;
        testPosition.setPosition(newX, newY);
    }
    @Test (expected = NonValidCoordinatesException.class)
    public void testSetPositionNegativeYDouble() throws NonValidCoordinatesException{
        constructPosition(defaultPosX, defaultPosY);
        double newX = 1.0;
        double newY = -999.9;
        testPosition.setPosition(newX, newY);
    }
}
