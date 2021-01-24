package graphex2021.model;

import java.util.*;

/**
 * This class simulates the Dijksta-Algorithm.
 * TODO what should happen with help info, if shortest path is found, but Dijkstra would continue?
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class Dijkstra implements Algorithm {
    private final GXGraph g;
    private final Collection<GXVertex> vertices;
    private final GXVertex start;
    /**
     * Array that holds distances for all {@link GXVertex}s respectively as distance to given
     * {@code start}-vertex. {@code -1} is for {@code infinity}. Only positive values for distances.
     */
    private final int[] dist;
    /**
     * Array of {@link GXVertex} where every index of the array refers to the id of a {@link GXVertex} and the element
     * represents the predecessor of the vertex with this id.
     */
    private final GXVertex[] prev;
    /**
     * This queue will hold all unmarked vertices, prioritized by their distance to the start vertex.
     */
    private final PriorityQueue<GXVertex> unmarked;
    private final List<Step> steps;

    /**
     * Creates a new Dijkstra instance for a given {@link GXGraph}.
     * @param g is the {@link GXGraph} the algorithm should be executed at.
     */
    public Dijkstra(GXGraph g) {
        this.g = g;
        this.vertices = g.vertices();
        this.start = g.getStartingVertex();

        int size = vertices.size();
        this.dist = new int[size];
        this.prev = new GXVertex[size];

         //This comparator is used for comparing two vertices by their current distance to the start vertex.
        Comparator<GXVertex> vertexDistanceComparator = (v, u) -> {
            //case u and v are unvisited and therefore distance infinity (-1)
            if (dist[v.getId()] == dist[u.getId()]) return 0;
            //case v is unvisited and therefore distance infinity (-1), u is better than v
            else if (dist[v.getId()] == -1) return 1;
            //case u is unvisited and therefore distance infinity (-1), v is better than u
            else if (dist[u.getId()] == -1) return -1;
            //both are visited, then calc difference of dist, v -> less, then v -> choose first
            else return dist[v.getId()] - dist[u.getId()];
        };
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
            if (!v.equals(start)) {
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
