package graphex2021.model;

import java.util.List;

/**
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class Dijkstra implements Algorithm {
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
}
