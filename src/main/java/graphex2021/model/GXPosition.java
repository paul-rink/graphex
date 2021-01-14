package graphex2021.model;

/**
 * Stores the position that a vertex can be in.
 * 2 dimensional position stored with x and y values
 *
 * @author Dominik
 * @version 0.1
 */
public class GXPosition {

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
