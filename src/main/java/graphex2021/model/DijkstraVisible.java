package graphex2021.model;

/**
 * @author kmarq
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
