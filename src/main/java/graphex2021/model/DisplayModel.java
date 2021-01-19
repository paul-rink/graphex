package graphex2021.model;

import java.util.List;

/**
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class DisplayModel {
    private List<Step> userSteps;
    private List<Step> algoSteps;
    private Algorithm algo;
    private GXGraph graph;

    DisplayModel() { }

    public GXEdge nexStep() {
        return null;
    }

    public void markEdge(GXEdge edge) { }

    public void markVertex(GXVertex vertex) { }

    public GXGraph getState() { return null; }

    public void undo() { }

    private void makeVisible(GXEdge edge) { }

    private void makeVisible(GXVertex vertex) { }

    private GXVertex getStartVertex() { return null; }

    private GXVertex getFinalVertex() { return null; }

    private void updateVisibleGraph() { }

    private Step getLastUserStep() { return null; }

    private void removeLastUserStep() { }
}
