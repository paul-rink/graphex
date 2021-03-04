package graphex2021.model.algo;

import graphex2021.model.GXGraph;

/**
 * Version of {@link Dijkstra} where the whole {@link GXGraph} is visible from beginning.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 04.03.2021
 */
public class DijkstraVisible extends Dijkstra {

    DijkstraVisible() {
        super();
    }

    public boolean isRevealed() {
        return true;
    }
}
