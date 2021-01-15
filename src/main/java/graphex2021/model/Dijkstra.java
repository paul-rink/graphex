package graphex2021.model;

import java.util.List;
import java.util.PriorityQueue;

/**
 * This class simulates the Dijksta-Algorithm.
 * TODO generic in Dijkstra (and other classes..)? Better defining in GXGraph?
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class Dijkstra<V,E> implements Algorithm {
    private GXGraph<GXVertex<V>,GXEdge<E,V>> g;
    private GXVertex<V> start;
    private int[] dist;
    private GXVertex<V>[] prev; //TODO int-array with id or Vertex-list
    private PriorityQueue<GXVertex<V>> unmarked;
    private List<Step> steps;

    public Dijkstra(GXGraph g) {
    }

    @Override
    public List<Step> getSequence(GXGraph g) {
        return null;
    }

    @Override
    public boolean isRevealed() {
        return false;
    }

    @Override
    public boolean hasStartingVertex() {
        return false;
    }

    @Override
    public boolean hasEndingVertex() {
        return false;
    }

    private void calculateShortestPath() {

    }

    private void updateDistance() {

    }

}
