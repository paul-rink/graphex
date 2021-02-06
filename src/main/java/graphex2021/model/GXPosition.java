package graphex2021.model;

/**
 * Stores the position that a vertex can be in.
 * 2 dimensional position stored with x and y values
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GXPosition {

    /**
     * is the range coordinates of a GXPosition should be chosen. Range: 0 to {@link GXPosition#POSITION_RANGE}
     */
    public static final int POSITION_RANGE = 1000;

    /**
     * x and y coordinates of the position
     */
    private int xPosition;
    private int yPosition;


    /**
     * Creates a new position consisting of x and y coordinates
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public GXPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    /**
     * Returns array containing the x value and thge y value of the position
     *
     * @return array of length 2. First entry is xPosition, second is yPosition
     */
    public int[] getPosition() {
        return new int[]{xPosition, yPosition};
    }
}
