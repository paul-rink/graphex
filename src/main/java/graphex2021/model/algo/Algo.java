package graphex2021.model.algo;

/**
 * Contains a simplified representation of every algo that could be used by the application. Be aware, you need to
 * implement the specific {@link Algorithm} first.
 */
public enum Algo {
    /**
     * See {@link Dijkstra}
     */
    DIJKSTRA(new Dijkstra()),
    /**
     * See {@link DijkstraVisible}
     */
    DIJKSTRA_VISIBLE(new DijkstraVisible());

    private final Algorithm algo;
    private final String displayName;
    private final String description;

    Algo(Algorithm algo) {
        this.algo = algo;
        this.displayName = algo.algoName();
        this.description = algo.algoDescription();
    }

    /**
     * Returns the implementation of the related algo.
     * @return the specific implementation
     */
    public Algorithm getUnderlyingAlgo() {
        return this.algo;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDescription() { return this.description; }
}
