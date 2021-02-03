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
    /**
     * Choose p between 0 and {@link GXGraphRandom#MAX_EDGE_PROBABILITY}
     */
    public static final int MAX_EDGE_PROBABILITY = 100;

    /**
     * Generates a random {@link GXGraph}.
     * @param numVertices is the number of vertices the graph should have. Max {@link GXGraphRandom#MAX_NUMBER_VERTICES}
     * @param maxWeight is the maximum weight an edge can have. Range is always 0...maxWeight
     * @param p is the probability in % an edge will be chosen for the graph
     */
    public GXGraphRandom(int numVertices, int maxWeight, int p) {
        super();
        if (numVertices > MAX_NUMBER_VERTICES || numVertices < MIN_NUMBER_VERTICES) {
            throw new IllegalArgumentException("Maximum amount of vertices = " + MAX_NUMBER_VERTICES);
        }
        generateVertices(numVertices);
        generateEdges(maxWeight, p);
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

    private void generateEdges(int maxWeight, int p) {
        LinkedList<GXVertex> uncheckedVertices = new LinkedList<>();
        uncheckedVertices.addAll(vertices());
        GXVertex current = uncheckedVertices.removeFirst();
        int idCounter = 0;
        //because non-directed graph, 1st vertex to all other, then 2nd vertex to all other etc
        while (!uncheckedVertices.isEmpty()) {
            for (GXVertex vertex : uncheckedVertices) {
                // +1 for including the bound
                int rnd = new Random().nextInt(MAX_EDGE_PROBABILITY + 1);
                //example: p=60 (%) then "roll a dice" between 0..100, if value <=60 accept it
                if (rnd <= p) {
                    int weight = new Random().nextInt(maxWeight + 1);
                    GXEdge edge = new GXEdge(current, vertex, Integer.toString(weight), weight, idCounter);
                    try {
                        insertEdge(edge);
                        //shouldn't happen
                    } catch (ElementNotInGraphException e) {
                        e.printStackTrace();
                    }
                    idCounter++;
                }
            }
            //go to next vertex
            current = uncheckedVertices.removeFirst();
        }
    }


}
