package graphex2021.model;

public enum Algo {
    DIJKSTRA(new Dijkstra(), "Dijkstra"), DIJKSTRA_VISIBLE(new DijkstraVisible(), "Dijkstra (aufgedeckt)");

    private Algorithm algo;
    private final String name;

    Algo(Algorithm algo, String name) {
        this.algo = algo;
        this.name = name;
    }

    /**
     * Returns the implementation of the related algo.
     * @return the specific implementation
     */
    public Algorithm getUnderlyingAlgo() {
        return this.algo;
    }
}
