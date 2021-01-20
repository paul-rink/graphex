package graphex2021.model;

import java.util.List;

/**
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class DisplayModel extends Subject {
    private List<Step> userSteps;
    private List<Step> algoSteps;
    private Algorithm algo;
    private GXGraph graph;

    DisplayModel() { }

    public GXEdge nexStep() {
        return null;
    }

    public void markEdge(GXEdge edge) {
        //check if edge is blocked because of circle -> create alert
        if (edge.isBlocked()) return; //TODO implement Alert
        //get unmarked vertex of edge and mark both in graph
        GXVertex nextVertex = edge.getNextVertex();
        graph.mark(edge, nextVertex);
        //make a new step with nextVertex and edge
        userSteps.add(new Step(nextVertex, edge));
        //block edges that are part of a circle
        graph.blockCircles(nextVertex);
        //make all edges from nextVertex visible just as the corresponding vertices
        graph.makeIncidentsVisible(nextVertex);
        //TODO implement observers
        this.notifyObservers();
    }

    public void markVertex(GXVertex vertex) { }

    public GXGraph getState() { return this.graph; }

    public void undo() { }

    private void makeVisible(GXEdge edge) { }

    private void makeVisible(GXVertex vertex) { }

    private GXVertex getStartVertex() { return null; }

    private GXVertex getFinalVertex() { return null; }

    private void updateVisibleGraph() { }

    private Step getLastUserStep() { return null; }

    private void removeLastUserStep() { }
}
