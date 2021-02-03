package graphex2021.model;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GXGraphRandom extends GXGraph {
    /**
     * is the maximal number of vertices a random graph can exist of depending on Labels existing for vertices.
     */
    public static final int MAX_NUMBER_VERTICES = VertexLabel.values().length;
    /**
     * is the maximal number of vertices a random graph can exist of depending on Labels existing for vertices.
     */
    public static final int MIN_NUMBER_VERTICES = 1;

    public GXGraphRandom(int numVertices, float p) {
        super();
        if (numVertices > MAX_NUMBER_VERTICES || numVertices < MIN_NUMBER_VERTICES) {
            throw new IllegalArgumentException("Maximum amount of vertices = " + MAX_NUMBER_VERTICES);
        }
        generateVertices(numVertices);
    }

    /**
     * Generates random vertices and inserts them to the graph.
     * @param num number if vertices
     * @return list of generated vertices
     */
    private void generateVertices(int num) {
        //array of all possible vertex labels, that are accessed via counter
        VertexLabel[] labels = VertexLabel.values();
        //list of all created vertices
        LinkedList<GXVertex> newVertices = new LinkedList<>();
        //counter that will represent id of a vertex
        int counter = 0;
        while (counter < num) {
            int rndX = new Random().nextInt(GXPosition.POSITION_RANGE);
            int rndY = new Random().nextInt(GXPosition.POSITION_RANGE);
            GXPosition rndPosition = new GXPosition(rndX, rndY);
            GXVertex newVertex = new GXVertex(labels[counter].toString(), counter, rndPosition);
            insertVertex(newVertex);
            counter++;
        }
    }
}
