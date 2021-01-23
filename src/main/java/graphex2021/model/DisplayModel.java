package graphex2021.model;

import java.util.Collection;
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
    private GXGraph visibleGraph;

    DisplayModel() { }

    public GXEdge nexStep() {
        return null;
    }

    public void markEdge(GXEdge edge) throws ElementNotInGraphException {
        //check if edge is blocked because of circle -> create alert
        if (edge.isBlocked()) return; //TODO implement Alert
        //get unmarked vertex of edge and mark both in graph
        GXVertex nextVertex = edge.getNextVertex();
        edge.mark(true);
        //get distance to already marked vertex of marked edge
        int curDist = graph.opposite(nextVertex, edge).getCurrentDistance();
        nextVertex.mark(curDist, edge);
        //make a new step with nextVertex and edge
        userSteps.add(new Step(nextVertex, edge));
        //block edges that are part of a circle
        graph.blockCircles(nextVertex);
        //make all edges from nextVertex visible just as the corresponding vertices
        makeIncidentsVisible(nextVertex);
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

    private void updateVisibleGraph() {

    }

    private Step getLastUserStep() { return null; }

    private void removeLastUserStep() { }

    /**
     * Sets all edges including its vertices visible that contain the given {@code vertex} and add those new visible
     * edges and vertices to the {@code visibleGraph}.
     *
     * @param vertex is the vertex for that the neighborhood is set visible.
     */
    private void makeIncidentsVisible(GXVertex vertex) throws ElementNotInGraphException {
        //checking all the adjacent edges
        for (GXEdge edge : graph.incidentEdges(vertex)) {
            if (!edge.isVisible()) {
                edge.setVisible(true);
                visibleGraph.insertEdge(edge);
            }
            //All  the vertices at the end of these edges need to be visible
            GXVertex opposite = graph.opposite(vertex, edge);
            if (!opposite.isVisible()) {
                opposite.setVisible(true);
                visibleGraph.insertVertex(opposite);
            }
        }
    }

}
