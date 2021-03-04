package graphex2021.model.algo;

import graphex2021.model.*;

import java.util.*;

/**
 * This class simulates the Dijkstra-Algorithm. <br>
 * <b> Important notes. </b>
 * The algorithm works only for positive edge weights >0 correctly. If for a next step multiple vertices share the
 * same distance, the vertex with the lowest id will be chosen. The same holds in case multiple edges to a vertex
 * will result in the same distance. The edge with the smallest ID is chosen. <br>
 *     Unreachable / unvisited vertices have infinity distance, represented by {@link Dijkstra#INFINITY_DIST}
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class Dijkstra implements Algorithm {
    private GXGraph g;
    private Collection<GXVertex> vertices;
    private GXVertex start;
    /**
     * Array that holds distances for all {@link GXVertex}s respectively as distance to given
     * {@code start}-vertex. {@link GXEdge#INVALID_DISTANCE} is for {@code infinity}.
     * Only positive values for distances.
     */
    private int[] dist;
    /**
     * Array of {@link GXVertex} where every index of the array refers to the id of a {@link GXVertex} and the element
     * represents the predecessor of the vertex with this id.
     */
    private GXVertex[] prev;
    /**
     * This queue will hold all unmarked vertices, prioritized by their distance to the start vertex.
     */
    private final PriorityQueue<GXVertex> unmarked;
    private LinkedList<Step> steps;

    /**
     * This is a placeholder for telling the algorithm that distance to a vertex is infinity, i.e. not known, or the
     * vertex is unreachable at all.
     */
    public static final int INFINITY_DIST = -1;

    /**
     * Creates a new Dijkstra instance for a given {@link GXGraph}. Initializes the PriorityQueue for distance
     * comparison.
     */
    public Dijkstra() {
         //This comparator is used for comparing two vertices by their current distance to the start vertex.
        Comparator<GXVertex> vertexDistanceComparator = (v, u) -> {
            //case u and v are unvisited and therefore distance infinity (-1) or have same distance then choose
            //the one with lower id
            if (dist[v.getId()] == dist[u.getId()]) return (v.getId() - u.getId());
            //case v is unvisited and therefore distance infinity (-1), u is better than v
            else if (dist[v.getId()] == INFINITY_DIST) return 1;
            //case u is unvisited and therefore distance infinity (-1), v is better than u
            else if (dist[u.getId()] == INFINITY_DIST) return -1;
            //both are visited, then calc difference of dist, v -> less, then v -> choose first
            else return dist[v.getId()] - dist[u.getId()];
        };
        this.unmarked = new PriorityQueue<>(vertexDistanceComparator);
        this.steps = new LinkedList<>();
    }

    @Override
    public LinkedList<Step> getSequence(GXGraph g) {
        //reset instance of dijkstra with new given graph
        setup(g);
        //set distances to infinity and fill unmarked list
        init();
        //calculate steps by dijkstra
        try {
            createSteps();
        } catch (ElementNotInGraphException eni) {
            return null;
        }
        return steps;
    }

    @Override
    public boolean isRevealed() {
        return false;
    }

    @Override
    public boolean hasStartingVertex() {
        return true;
    }

    @Override
    public boolean hasEndingVertex() {
        return true;
    }

    @Override
    public boolean isCorrectDistance(GXVertex goal, int distance) {
        return distance == dist[goal.getId()];
    }

    /**
     * Resets the dijkstra instance for a new given graph. Sets starting vertex, arrays for dist and prev and clears
     * unmarked PQ as well steps.
     * @param g is the graph dijkstra should now calculate shortest path at.
     */
    private void setup(GXGraph g) {
        this.g = g;
        this.vertices = g.vertices();
        this.start = g.getStartingVertex();
        int size = vertices.size();
        this.dist = new int[size];
        this.prev = new GXVertex[size];
        unmarked.clear();
        this.steps = new LinkedList<>();
    }

    /**
     * Initialize Dijkstra: Set distances of all vertices to infinity and add to queue except for starting vertex.
     * Starting vertex gets distance 0.
     */
    private void init() {
        for (GXVertex v : vertices) {
            if (!v.equals(start)) {
                int vId = v.getId();
                dist[vId] = INFINITY_DIST;             //set all distances to infinity (-1)
                prev[vId] = null;           //set all predecessor undefined
            }
            unmarked.add(v);
        }
        dist[start.getId()] = 0;            //set distance to starting vertex to 0
    }

    /**
     * Generates a list of {@link Step}s where each entry is one step of Dijkstra algorithm, consisting of a
     * {@link GXVertex} and {@link GXEdge} chosen by Dijkstra in the step.
     */
    private void createSteps() throws ElementNotInGraphException {
        while (!unmarked.isEmpty()) {
            GXVertex v = unmarked.remove();     //this is the next chosen vertex

            //if the next vertex that would be marked has infinity distance this means that zhe graph is not connected
            // and that all remaining vertices are not reachable from start vertex
            if ((dist[v.getId()] == INFINITY_DIST)) {
                //all remaining vertices can be removed from the unmarked list, since the algo can stop here
                unmarked.clear();
                break;
            }

            //create a step with this vertex, but don't create a step for starting vertex
            if (!v.equals(start)) {
                steps.add(extractStep(v));
            }

            //update distances for all unmarked neighbors
            Collection<GXVertex> neighbors = g.getNeighbors(v);
            for (GXVertex neighbor : neighbors) {
                if (unmarked.contains(neighbor)) {
                    updateDistance(v, neighbor);
                }
            }
        }
    }

    /**
     * Searches for a given {@link GXVertex} that was the next chosen vertex by the algorithm for the {@link GXEdge}
     * that belongs to it. This said, this edge is the next part of the shortest path calculated by the algorithm.
     * The edge is identified by the given vertex and it's previous vertex from prev.
     *
     * @param v is the chosen {@link GXVertex} of the algorithm in one step.
     * @return a {@link Step} containing the chosen {@code vertex} and related edge.
     */
    private Step extractStep(GXVertex v) {
        GXEdge edgeToV = null;
        try {
            edgeToV = g.getEdge(v, prev[v.getId()]);
        } catch (ElementNotInGraphException e) {
            e.printStackTrace();
        }
        return new Step(v, edgeToV);
    }

    /**
     * Updates the distances for all neighbors of new selected vertex.
     * @param selectedVertex is the new selected vertex.
     * @param u is one neighbor of {@code selectedVertex}.
     */
    private void updateDistance(GXVertex selectedVertex, GXVertex u) {
        try {
            //get the edge that is connecting v and u
            GXEdge edge = g.getEdge(selectedVertex, u);
            //the resulting distance if the new path would go over v to u
            int alternativeDist = dist[selectedVertex.getId()] + edge.getWeight();
            //if u is not visited yet (dist infinity/-1) or new distance is less than old distance, update distance
            if (dist[u.getId()] == INFINITY_DIST || alternativeDist < dist[u.getId()]) {
                dist[u.getId()] = alternativeDist;

                //because the distance of u changed, insert it again to queue to update its priority
                unmarked.remove(u);
                unmarked.add(u);

                prev[u.getId()] = selectedVertex;
                //selected vertex results in a same distance to u as another vertex that is already stored in prev
                //then the algo should always pick the vertex as prev that's edge weight to u is less
            } else if (alternativeDist == dist[u.getId()] && prev[u.getId()] != null) {
                GXEdge prevEdge = g.getEdge(u, prev[u.getId()]);
                //if new found edge to u has lower id, choose this edge, else leave it as before
                if (edge.getId() < prevEdge.getId()) {
                    prev[u.getId()] = selectedVertex;
                }
            }
        } catch (ElementNotInGraphException e) {
            e.printStackTrace();
        }
    }

}
