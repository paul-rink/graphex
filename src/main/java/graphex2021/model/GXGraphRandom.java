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
     * @param noIsolated if {@code true} isolated are not allowed and will be randomly connected to the rest of the
     *                   graph, if {@code false} isolated vertices are allowed
     */
    public GXGraphRandom(int numVertices, int maxWeight, int p, boolean noIsolated) {
        super();
        if (numVertices > MAX_NUMBER_VERTICES || numVertices < MIN_NUMBER_VERTICES) {
            throw new IllegalArgumentException("Maximum amount of vertices = " + MAX_NUMBER_VERTICES);
        }
        generateVertices(numVertices);
        setStartingAndEndingVertex();
        if (noIsolated) generateRndTree(maxWeight);
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

    /**
     * sets starting and ending vertex random
     */
    private void setStartingAndEndingVertex() {
        ArrayList<GXVertex> vertices = new ArrayList<>();
        vertices.addAll(vertices());
        int max = vertices.size();
        int s= 0;
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


}
