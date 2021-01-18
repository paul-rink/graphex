package graphex2021.model;

import java.util.*;

/**
 * This class simulates the Dijksta-Algorithm.
 * TODO generic in Dijkstra (and other classes..)? Better defining in GXGraph?
 * TODO Singleton? - Maybe because of possibility for multi-window support not..
 * TODO what should happen with help info, if shortest path is found, but Dijkstra would continue?
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
@SuppressWarnings("checkstyle:WhitespaceAround")
public class Dijkstra implements Algorithm {
    private GXGraph g;
    /**
     * Size of the {@code graph}, i.e. the number of {@link GXVertex} in the graph.
     */
    private int size;
    private Collection<GXVertex> vertices;
    private GXVertex start;
    /**
     * Array that holds distances for all {@link GXVertex}s respectively as distance to given
     * {@code start}-vertex. {@code -1} is for {@code infinity}. Only positive values for distances.
     */
    private int[] dist;
    /**
     * Array of {@link GXVertex} where every index of the array refers to the id of a {@link GXVertex} and the element
     * represents the predecessor of the vertex with this id.
     */
    private GXVertex[] prev;
    /**
     * This comparator is used for comparing two vertices by their current distance to the start vertex.
     */
    private Comparator<GXVertex> vertexDistanceComparator;
    /**
     * This queue will hold all unmarked vertices, prioritized by their distance to the start vertex.
     */
    private PriorityQueue<GXVertex> unmarked;
    private List<Step> steps;

    /**
     * Creates a new Dijkstra instance for a given {@link GXGraph}.
     * @param g is the {@link GXGraph} the algorithm should be executed at.
     */
    public Dijkstra(GXGraph g) {
        this.g = g;
        this.vertices = (Collection) g.vertices();
        this.size = vertices.size();
        this.start = g.getStartingVertex();
        this.dist = new int[size];
        this.prev = new GXVertex[size];
        this.vertexDistanceComparator = Comparator.comparingInt(v -> dist[v.getId()]);
        this.unmarked = new PriorityQueue<>(vertexDistanceComparator);
        this.steps = new LinkedList<>();
        createSteps();
    }

    @Override
    public List<Step> getSequence(GXGraph g) {
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

    /**
     * Initialize Dijkstra: Set distances of all vertices to infinity and add to queue except for starting vertex.
     * Starting vertex gets distance 0.
     */
    private void init() {
        for (GXVertex v : vertices) {
            if (v != start) {
                int vId = v.getId();
                dist[vId] = -1;             //set all distances to infinity (-1)
                prev[vId] = null;           //set all predecessor undefined
            }
            unmarked.add(v);
        }
        dist[start.getId()] = 0;            //set distance to starting vertex to 0
    }

    /**
     * Generates a list of {@link Step}s where each entry is one step of Dijkstra algorithm, consisting of a
     * {@link GXVertex} and {@link GXEdge} chosen by Dijkstra in the step.
     * @return list of {@link Step}s according to execution order of Dijkstra.
     */
    private void createSteps() {
        init();
        while (!unmarked.isEmpty()) {
            GXVertex v = unmarked.remove();     //this is the net chosen vertex

            //create a step with this vertex, but don't create a step for starting vertex
            if (!v.equals(start)) { steps.add(extractStep(v)); }

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
     * TODO maybe method in Graph with getEdge(v1,v2)
     * Searches for a given {@link GXVertex} that was the next chosen vertex by the algorithm for the {@link GXEdge}
     * that belongs to it. This said, this edge is the next part of the shortest path calculated by the algorithm.
     * The edge is identified by the given vertex and a second vertex that is already marked.
     *
     * @param v is the chosen {@link GXVertex} of the algorithm in one step.
     * @return a {@link Step} containing the chosen {@code vertex} and related edge.
     */
    private Step extractStep(GXVertex v) {
        Step step = null;
        Collection<GXVertex> neighbors = g.getNeighbors(v);
        for (GXVertex u : neighbors) {
            if (!unmarked.contains(u)) {     //if neighbor u is already marked, the correct vertex is found
                try {
                    step = new Step(v, g.getEdge(v, u));
                } catch (ElementNotInGraphException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return step;
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
            if (dist[u.getId()] == -1 || alternativeDist < dist[u.getId()]) {
                dist[u.getId()] = alternativeDist;

                //because the distance of u changed, insert it again to queue to update its priority
                unmarked.remove(u);
                unmarked.add(u);

                prev[u.getId()] = selectedVertex;
            }
        } catch (ElementNotInGraphException e) {
            e.printStackTrace();
        }
    }

}
