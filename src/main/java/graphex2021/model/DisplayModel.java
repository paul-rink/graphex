package graphex2021.model;


import java.io.File;
import java.util.LinkedList;

/**
 * TODO JAVADOC
 *
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class DisplayModel extends Subject {
    private LinkedList<Step> userSteps;
    private LinkedList<Step> algoSteps;
    private Algorithm algo;
    private GXGraph graph;
    private GXGraph visibleGraph;

    public DisplayModel() {
        //TODO maybe better way for file Separator
        File example = new File(
                "src" + File.separator + "main" + File.separator + "resources" + File.separator + "graphex2021"
                        + File.separator + "GraphData" + File.separator + "exampleGraph.json");

        try {
            this.graph = new GXGraph(example);
        } catch (ElementNotInGraphException eni) {
            //TODO better way to handle this. Wrong exception here
        }
        this.visibleGraph = new GXGraph();
        initialVisibleGraph();

        this.algo = new Dijkstra();
        algoSteps = algo.getSequence(graph);

        this.userSteps = new LinkedList<>();

        notifyObservers();

    }

    public GXEdge nexStep() {
        return null;
    }

    public void markEdge(GXEdge edge) throws ElementNotInGraphException {
        //check if edge is blocked because of circle -> create alert
        if (edge.isBlocked()) return; //TODO implement Alert
        //get unmarked vertex of edge and mark both in graph
        GXVertex nextVertex = edge.getNextVertex();
        edge.mark();
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

    public void markVertex(GXVertex vertex) {

    }

    public GXGraph getState() {
        return this.graph;
    }

    /**
     * method that gets called when a user requests to undo the last step he did
     * the method unmarks the last selected edge/vertex and updates the visibility of all edges and vertices
     * accordingly
     */
    public void undo() throws ElementNotInGraphException {
        Step lastStep = getLastUserStep();
        GXEdge lastEdge = lastStep.getSelectedEdge();
        GXVertex lastVertex = lastStep.getSelectedVertex();
        removeLastUserStep();
        makeIncidentsInvisible(lastVertex, lastEdge);
        lastVertex.unmark();
        lastEdge.unmark();
        graph.unblock(lastVertex);
        this.notifyObservers();
    }

    private void makeVisible(GXEdge edge) { }

    private void makeVisible(GXVertex vertex) { }

    /**
     * method that returns the StartingVertex of the underlying graph
     * @return the StartingVertex of the graph
     */
    private GXVertex getStartVertex() {
        return this.graph.getStartingVertex();
    }

    /**
     * method that returns the FinalVertex of the underlying graph
     * @return the FinalVertex of the graph
     */
    private GXVertex getFinalVertex() {
        return this.graph.getEndingVertex();
    }

    private void updateVisibleGraph() {

    }

    /**
     * method to get the last step the user selected
     * @return the last step in the list of user steps
     */
    private Step getLastUserStep() {
        return userSteps.getLast();
    }

    /**
     * method that removes the last step in the list of the userSteps
     */
    private void removeLastUserStep() {
        userSteps.removeLast();
    }

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

    private void makeIncidentsInvisible(GXVertex vertex, GXEdge originalEdge) throws ElementNotInGraphException{
        //check all adjacent edges if vertex on the other side is marked it remains visible
        //otherwise the edge and the vertex at the other end should be invisible and removed
        //from the visible graph
        for(GXEdge edge : graph.incidentEdges(vertex)) {
            try {
                GXVertex otherVertex = edge.getNextVertex();
                otherVertex.setVisible(false);
                visibleGraph.removeVertex(otherVertex);
                edge.setVisible(false);
                visibleGraph.removeEdge(edge);
            } catch (IllegalArgumentException e) {
                //this means that the other side is marked and this edge needs to stay visible
            }
        }



    }

    private void initialVisibleGraph() {
        final GXVertex start = graph.getStartingVertex();
        start.mark(0, null);
        start.setVisible(true);
        visibleGraph.insertVertex(start);
        try {
            for (GXEdge edge : graph.incidentEdges(start)) {
                GXVertex toIns = edge.getNextVertex();
                //Setting the initial edge visible and the vertex at the other end
                toIns.setVisible(true);
                edge.setVisible(true);
                //inserting the vertex and edge into the visible graph
                visibleGraph.insertVertex(toIns);
                visibleGraph.insertEdge(edge);
            }
        } catch (ElementNotInGraphException eni) {
            //TODO find better way it is kind of a different error
        }


        final GXVertex end =  graph.getEndingVertex();
        end.setVisible(true);
        visibleGraph.insertVertex(end);
    }

}
