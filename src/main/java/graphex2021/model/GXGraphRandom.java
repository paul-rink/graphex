package graphex2021.model;

import java.util.*;

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

    public static final int MIN_EDGE_WEIGHT = 1;

    /**
     * Choose p between {@link GXGraphRandom#MIN_EDGE_PROBABILITY} and {@link GXGraphRandom#MAX_EDGE_PROBABILITY}
     */
    public static final int MIN_EDGE_PROBABILITY = 1;

    /**
     * maximum tries for searching for a position
     */
    private static final int MAX_TRIES = 10000;

    private static int radiusSqr;

    /**
     * Holds all generated positions.
     */
    private static LinkedList<GXPosition> positionSet = new LinkedList<>();

    /**
     * Counter for edge ids
     */
    private int edgeIdCounter = 0;

    /**
     * contains all edges that have been added by the rndTreeGenerator already
     */
    private Set<GXEdge> edgesFromTree = new HashSet<>();

    /**
     * Generates a random {@link GXGraph}.
     * @param numVertices is the number of vertices the graph should have. Max {@link GXGraphRandom#MAX_NUMBER_VERTICES}
     * @param maxWeight is the maximum weight an edge can have. Range is always 0...maxWeight
     * @param p is the probability in % an edge will be chosen for the graph
     * @param isolatedAllowed if {@code false} isolated are not allowed and will be randomly connected
     *                        to the rest of the graph, if {@code true} isolated vertices are allowed.
     */
    public GXGraphRandom(int numVertices, int maxWeight, int p, boolean isolatedAllowed, boolean avoidClustering) {
        super();
        if (numVertices > MAX_NUMBER_VERTICES || numVertices < MIN_NUMBER_VERTICES) {
            throw new IllegalArgumentException("Maximum amount of vertices = " + MAX_NUMBER_VERTICES);
        }
        //very magic try to find a good pushing factor or make it possible to get it from user input
        int radius = GXPosition.POSITION_RANGE / 5;
        radiusSqr = radius * radius;
        generateVertices(numVertices, avoidClustering);
        setStartingAndEndingVertex();
        if (!isolatedAllowed) generateRndTree(maxWeight);
        generateEdges(maxWeight, p);
    }

    /**
     * Generates random vertices and inserts them to the graph.
     * @param num number if vertices
     */
    private void generateVertices(int num, boolean avoidClustering) {
        //array of all possible vertex labels, that are accessed via counter
        VertexLabel[] labels = VertexLabel.values();
        //list of all created vertices
        LinkedList<GXVertex> newVertices = new LinkedList<>();
        //counter that will represent id of a vertex
        int counter = 0;
        while (counter < num) {
            GXPosition rndPosition = null;
            if (avoidClustering) {
                rndPosition = generatePushingPositions(MAX_TRIES);
                //if not avoid clustering is chosen, or if generatePushingPositions still didn't found fitting position
            } if (rndPosition == null) {
                int x = new Random().nextInt(GXPosition.POSITION_RANGE);
                int y = new Random().nextInt(GXPosition.POSITION_RANGE);
                rndPosition = new GXPosition(x, y);
            }
            GXVertex newVertex = new GXVertex(labels[counter].toString(), counter, rndPosition);
            insertVertex(newVertex);
            counter++;
        }
    }

    /**
     * sets starting and ending vertex random
     */
    private void setStartingAndEndingVertex() {
        ArrayList<GXVertex> vertices = new ArrayList<>();
        vertices.addAll(vertices());
        int max = vertices.size();
        int s = 0;
        int z = 0;
        //choose starting and ending vertex random and check that they are not the same
        while(s == z) {
            s = new Random().nextInt(max);
            z = new Random().nextInt(max);
        }
        setStartingVertex(vertices.get(s));
        setEndingVertex(vertices.get(z));
    }

    /**
     * Generates pseudo random edges. with probability p (in %) an edge between two vertices is chosen. The edges
     * are chosen the way, that no duplicates are generated. Only edges from tree have to be checked.
     * @param maxWeight is maximum weight for an edge
     * @param p probability to choose an edge (in %) 0-100
     */
    private void generateEdges(int maxWeight, int p) {
        LinkedList<GXVertex> uncheckedVertices = new LinkedList<>();
        uncheckedVertices.addAll(vertices());
        GXVertex current = uncheckedVertices.removeFirst();
        //because non-directed graph, 1st vertex to all other, then 2nd vertex to all other etc
        while (!uncheckedVertices.isEmpty()) {
            for (GXVertex vertex : uncheckedVertices) {
                // +1 for including the bound
                int rnd = new Random().nextInt(MAX_EDGE_PROBABILITY + 1);
                //example: p=60 (%) then "roll a dice" between 0..100, if value <=60 accept it
                if (rnd <= p) {
                    int weight = new Random().nextInt(maxWeight) + 1;
                    GXEdge edge = new GXEdge(current, vertex, Integer.toString(weight), weight, edgeIdCounter);
                    //check that edge is not already part of tree, by
                    if (!treeContains(edge)) {
                        try {
                            insertEdge(edge);
                            edgeIdCounter++;
                            //shouldn't happen
                        } catch (ElementNotInGraphException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            //go to next vertex
            current = uncheckedVertices.removeFirst();
        }
    }

    /**
     * Generates a random tree for given vertices. This makes sure that resulting graph is connected.
     * @param maxWeight is the maximum weight of an edge.
     */
    private void generateRndTree(int maxWeight) {
        //list of remaining isolates
        ArrayList<GXVertex> isolates = new ArrayList<>();
        isolates.addAll(vertices());
        //list of all vertices that are connected with each other
        ArrayList<GXVertex> connected = new ArrayList<>();
        //choose a random first vertex
        GXVertex first = isolates.remove(new Random().nextInt(isolates.size()));
        connected.add(first);
        while (!isolates.isEmpty()) {
            //choose a random vertex from already connected vertices
            int cur = new Random().nextInt(connected.size());
            GXVertex current = connected.get(cur);
            //choose a random vertex from isolates and connect it to current
            int con = new Random().nextInt(isolates.size());
            GXVertex next = isolates.remove(con);
            //choose a weight
            int weight = new Random().nextInt(maxWeight) + 1;
            //create new edge
            GXEdge newEdge = new GXEdge(current, next, Integer.toString(weight), weight, edgeIdCounter);
            edgeIdCounter++;
            //insert edge and add "next" vertex to connected
            try {
                insertEdge(newEdge);
                edgesFromTree.add(newEdge);
                //shouldn't happen
            } catch (ElementNotInGraphException e) {
                e.printStackTrace();
            }
            connected.add(next);
        }
    }

    /**
     * Checks if an edge was already part if the tree.
     * @param newEdge is the edge you want to check if its already part of the tree
     * @return {@code true} if edge is already part if tree, {@code false} otherwise.
     */
    private boolean treeContains(GXEdge newEdge) {
        if (edgesFromTree.isEmpty()) return false;
        for (GXEdge edge : edgesFromTree) {
            if (newEdge.equals(edge)) return true;
        }
        return false;
    }

    /**
     * Generates a position that is at least {@code radius} away from any other position in
     * {@link GXGraphRandom#positionSet}.
     * @param maxTries is maximum tries for finding such a posuition
     * @return new random position or {@code null} if no position could be found within given tries.
     */
    private GXPosition generatePushingPositions(int maxTries) {
        int n = maxTries;
        GXPosition rndPosition;
        do {
            int rndX = randomCoordinateGenerator();
            int rndY = randomCoordinateGenerator();
            rndPosition = new GXPosition(rndX, rndY);
            n--;
        } while (conflicts(rndPosition) && n > 0);
        positionSet.add(rndPosition);
        return rndPosition;
    }

    /**
     * Checks if the given position is within the radius of another already existing position.
     * @param rndPosition is the position to check
     * @return {@code true} if the condition works, {@code false} otherwise
     */
    private boolean conflicts(GXPosition rndPosition) {
        for (GXPosition pos : positionSet) {
            if (calcSquDistance(rndPosition, pos) < radiusSqr) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the distance of two positions via squared euclidean distance.
     * @param p1 pos1
     * @param p2 pos2
     * @return squared euclidean distance
     */
    private int calcSquDistance(GXPosition p1, GXPosition p2) {
        int x1 = p1.getPosition()[0];
        int y1 = p1.getPosition()[1];
        int x2 = p2.getPosition()[0];
        int y2 = p2.getPosition()[1];
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 -y1);
    }

    private int randomCoordinateGenerator() {
        // margin to border of window 1% of total window
        int margin = GXPosition.POSITION_RANGE / 100;
        return new Random().nextInt(GXPosition.POSITION_RANGE - 2 * margin) + margin;
    }


}
