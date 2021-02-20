package graphex2021.model;

import java.io.File;
import java.net.URISyntaxException;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class controls all logic behind the GUI. If something in the view is happening, the
 * {@link graphex2021.controller.Controller} will tell the DisplayModel. Here everything will be calculated what should
 * happen next. Then all observers are notified about the changes on the underlying model. Then its up to the observers
 * to handle the changes.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class DisplayModel extends Subject {
    /**
     * This value can be used as initial distance for vertices, like the starting vertex.
     */
    public static final int INIT_DISTANCE = 0;
    private static final String EXAMPLEGRAPH = "resources" + File.separator + "graphex2021" + File.separator
            + "GraphData" + File.separator + "Templates" + File.separator + "Vorlage_(Karlsruhe).json";

    private LinkedList<Step> userSteps;
    private LinkedList<Step> algoSteps;
    private Algorithm algo;
    private GXGraph graph;
    private GXGraph visibleGraph;

    /**
     * Standard constructor fpr DisplayModel. This will init a model with a standard graph {@code EXAMPLEGRAPH}.
     * @throws WrongFileFormatException in case the default graph file does not match the correct format
     */
    public DisplayModel() throws WrongFileFormatException {
        File jarPath;
        try {
            jarPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            throw new WrongFileFormatException(e.getMessage());
        }
        loadGraph(new File(jarPath, EXAMPLEGRAPH));

    }

    /**
     * Constructor for DisplayModel for a given {@link GXGraph}.
     * @param graph is the graph the DisplayModel should be init for.
     */
    public DisplayModel(GXGraph graph) {
        this.graph = graph;
        loadGraph();
    }

    /**
     * Will init a new DisplayModel for a given file that stores information to a graph. <br>
     *     See {@link GraphParser} for more detail.
     * @param inputFile is the input file that contains the graph data.
     * @throws WrongFileFormatException in case the file does not match the correct format
     */
    public DisplayModel(File inputFile) throws WrongFileFormatException {
        loadGraph(inputFile);
    }

    private void loadGraph(File inputFile) throws WrongFileFormatException {
        this.graph = new GXGraph(inputFile);
        loadGraph();
    }


    private void loadGraph() {
        this.visibleGraph = new GXGraph();
        initialVisibleGraph();
        this.algo = new Dijkstra();
        algoSteps = algo.getSequence(graph);
        //mark starting vertex from the beginning and update distances for incidents, if algo request a starting vertex
        if (algo.hasStartingVertex()) {
            graph.getStartingVertex().mark();
            try {
                updateCurrentDistancesForIncidents(graph.getStartingVertex());
            } catch (ElementNotInGraphException e) {
                e.printStackTrace();
            }
        }

        this.userSteps = new LinkedList<>();

        notifyObservers();

    }

    /**
     * Highlights the next correct edge and vertex, that should be marked. If a mistake has been made before,
     * the last correctly marked vertex and edge will be highlighted.
     *
     */
    public void nexStep() {
        Iterator<Step> iter = userSteps.iterator();
        Step hintStep = algoSteps.getFirst();
        for (Step step : algoSteps) {
            hintStep = step;
            if (!iter.hasNext() ) {
                step.getSelectedEdge().setHint(true);
                step.getSelectedVertex().setHint(true);
                break;
            } else if (!iter.next().equals(step)) {
                step.getSelectedEdge().setHint(true);
                step.getSelectedVertex().setHint(true);
                break;
            }
        }
        notifyObservers();

        // Reset the the components so that the they are not marked as hints after next selection.
        hintStep.getSelectedVertex().setHint(false);
        hintStep.getSelectedEdge().setHint(false);
    }

    /**
     * This will check, if steps performed by the user do correspond to the steps that the algorithm would perform.
     * @return {@code true} if all steps do equal the steps by the algo in the correct order, {@code false} otherwise
     */
    public boolean checkCorrect() {
        Iterator<Step> iter = userSteps.iterator();

        for (Step step : algoSteps) {
            if (!iter.hasNext() ) {
                return true;
            } else if (!iter.next().equals(step)) {
                return false;
            }
        }
        return false;
    }

    /**
     * Checks if the user found the shortest path. Therefore a path to ending vertex must exist, i.e. the ending vertex
     * is marked and the the distance from user and the distance in vertex does match shortest distance.
     * @param distance is the input distance from user
     * @return {@code true} if user steps do match algo steps, {@code false} otherwise.
     */
    public boolean checkFoundShortestPath(int distance) {
        GXVertex endingVertex = graph.getEndingVertex();
        boolean correctDistance = algo.isCorrectDistance(endingVertex, distance);
        boolean shortestPath = algo.isCorrectDistance(endingVertex, endingVertex.getCurrentDistance());
        return correctDistance && shortestPath && endingVertex.isMarked();
    }

    /**
     * This will mark an {@link GXEdge} if it is not blocked already. When an edge is marked, the unmarked
     * {@link GXVertex} will be marked as well and all distances that are influenced by this vertex are updated. Also
     * a new userstep is generated, containing the marked edge and vertex. Circles are blocked and all incident
     * edges and vertices of the new marked vertex are set visible.
     * @param edge is the marked edge.
     * @throws ElementNotInGraphException in case there was an edge ir vertex that is not part of the graph
     * @throws EdgeCompletesACircleException if the marked edge would complete a circle
     */
    public void markEdge(GXEdge edge) throws ElementNotInGraphException, EdgeCompletesACircleException {
        //check if edge is blocked because of circle -> create alert
        if (edge.isBlocked()) throw new EdgeCompletesACircleException("Edge can not be marked, because its blocked");
        //get unmarked vertex of edge and mark both in graph
        GXVertex nextVertex = edge.getNextVertex();
        //this shouldn't happen: an edge can only be marked if exact one vertex of it is marked
        if (nextVertex == null) return;
        edge.mark();
        //get distance to already marked vertex of marked edge
        int curDist = graph.opposite(nextVertex, edge).getCurrentDistance();
        nextVertex.mark(curDist, edge);
        //update current distance for other incident vertices
        updateCurrentDistancesForIncidents(nextVertex);
        //make a new step with nextVertex and edge
        userSteps.add(new Step(nextVertex, edge));
        //block edges that are part of a circle
        graph.blockCircles(nextVertex);
        //make all edges from nextVertex visible just as the corresponding vertices
        makeIncidentsVisible(nextVertex);
        this.notifyObservers();
    }

    /**
     * This method is used for marking a vertex. <br>
     *     <b>Note.</b> Not implemented / needed right now.
     * @param vertex is the marked vertex
     */
    @Deprecated
    public void markVertex(GXVertex vertex) {
        vertex.mark();
    }

    /**
     * Returns all visible vertices and edges as a {@link GXGraph}.
     * @return the graph containing all visible edges and vertices.
     */
    public GXGraph getState() {
        return this.visibleGraph;
    }

    /**
     * method that gets called when a user requests to undo the last step he did
     * the method unmarks the last selected edge/vertex and updates the visibility of all edges and vertices
     * accordingly
     */
    public void undo() throws ElementNotInGraphException {
        if (userSteps.size() > 0) {
            Step lastStep = getLastUserStep();
            GXEdge lastEdge = lastStep.getSelectedEdge();
            GXVertex lastVertex = lastStep.getSelectedVertex();
            removeLastUserStep();
            makeIncidentsInvisible(lastVertex);
            lastVertex.unmark();
            lastEdge.unmark();
            graph.unblock(lastVertex);
            updateCurrentDistance(lastVertex);
            this.notifyObservers();
        }
    }

    /**
     * Resets the state of the current graph to the state of from the beginning, before any userSteps were made.
     */
    public void reset() {
        //Making all edges unmarked and invisible
        for (GXEdge edge : graph.edges()) {
            edge.unmark();
            edge.setVisible(false);
            edge.setBlocked(false);
        }
        //Making all vertices unmarked and invisible
        for (GXVertex vertex : graph.vertices()) {
            vertex.unmark();
            vertex.setVisible(false);
        }
        //Creating new visibileGraph that will then have the starting and end vertex be visible.
        this.visibleGraph = new GXGraph();
        this.userSteps = new LinkedList<>();
        initialVisibleGraph();
        notifyObservers();
    }

    /**
     * Highlights all edges that are on the path from start to the given vertex.
     * @param vertex is the vertex, the path should be highlighted for.
     */
    public void highlightShortestPathTo(GXVertex vertex) {
        LinkedList<GXEdge> highlightedEdges = new LinkedList<>();
        GXEdge edge = vertex.getPrevious();
        GXVertex cur = vertex;
        while (edge != null) {
            edge.setHighlighted(true);
            highlightedEdges.add(edge);
            try {
                //next edge
                cur = graph.opposite(cur, edge);
                edge = cur.getPrevious();
            } catch (ElementNotInGraphException e) {
                e.printStackTrace();
            }
        }
        notifyObservers();

        //reset vertex property, that they are no longer highlighted for further steps
        for (GXEdge e: highlightedEdges) {
            e.setHighlighted(false);
        }
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
                //because of insertEdge behaviour you first have to check, if opposite edge is already visible
                GXVertex opposite = graph.opposite(vertex, edge);
                if (!opposite.isVisible()) {
                    opposite.setVisible(true);
                    visibleGraph.insertVertex(opposite);
                }
                edge.setVisible(true);
                visibleGraph.insertEdge(edge);
            }
        }
    }

    private void makeIncidentsInvisible(GXVertex vertex) throws ElementNotInGraphException {
        //check all adjacent edges if vertex on the other side is marked it remains visible
        //otherwise the edge and the vertex at the other end should be invisible and removed
        //from the visible graph
        for (GXEdge edge : visibleGraph.incidentEdges(vertex)) {
            try {
                GXVertex otherVertex = edge.getNextVertex();
                //need this check because getNextVertex can return null
                if (otherVertex != null) {
                    edge.setVisible(false);
                    visibleGraph.removeEdge(edge);

                    //additionally needs to check whether this vertex is also connected to another visible edge
                    //this would mean the vertex stays visible
                    boolean stayVisible = false;
                    if (!otherVertex.equals(graph.getStartingVertex())
                            && !otherVertex.equals(graph.getEndingVertex())) {

                        for (GXEdge otherEdge : visibleGraph.incidentEdges(otherVertex)) {
                            if (otherEdge.isVisible()) {
                                stayVisible = true;
                                break;
                            }
                        }

                        if (!stayVisible) {
                            otherVertex.setVisible(false);
                            visibleGraph.removeVertex(otherVertex);
                        }
                    }
                }


            } catch (IllegalArgumentException e) {
                //this means that the other side is marked and this edge needs to stay visible
            }
        }



    }

    /**
     * Updates current distances for all incident vertices of a specific (marked) vertex. When a vertex is marked,
     * check all its incident vertices for one of the following cases: <br>
     *     - vertex is marked: do nothing <br>
     *     - vertex in unmarked: if current distance > new distance -> update, else do nothing
     * @param markedVertex is the vertex that is marked
     */
    private void updateCurrentDistancesForIncidents(GXVertex markedVertex) throws ElementNotInGraphException {
        for (GXEdge edge : graph.incidentEdges(markedVertex)) {
            GXVertex incident = graph.opposite(markedVertex, edge);
            int oldDist = incident.getCurrentDistance();
            int uptDist = markedVertex.getCurrentDistance() + edge.getWeight();
            if (!incident.isMarked() && (oldDist == Dijkstra.INFINITY_DIST
                    || oldDist == INIT_DISTANCE || oldDist > uptDist)) {
                incident.setCurrentDistance(uptDist);
            }
        }
    }

    /**
     * Updates the current distance for a specific vertex. The distance is calculated by the shortest connection from
     * start to this vertex. The lowest distance of all incident vertices + edge weight is chosen as new distance. <br>
     *     Distance for <b>starting vertex</b> is always set to 0.
     * @param vertex is the vertex you want to update its distance.
     * @throws ElementNotInGraphException if a vertex or edge was not part of the graph
     */
    private void updateCurrentDistance(GXVertex vertex) throws ElementNotInGraphException {
        if (vertex.equals(graph.getStartingVertex())) {
            vertex.setCurrentDistance(INIT_DISTANCE);
        } else {
            for (GXEdge edge : graph.incidentEdges(vertex)) {
                int oldDist = vertex.getCurrentDistance();
                GXVertex incident = graph.opposite(vertex, edge);
                int newDist = incident.getCurrentDistance() + edge.getWeight();
                //if resulting distance from previous (marked) vertex is better, update
                if (incident.isMarked() && (newDist < oldDist || oldDist == Dijkstra.INFINITY_DIST)) {
                    vertex.setCurrentDistance(newDist);
                }
            }
        }
    }

    private void initialVisibleGraph() {
        final GXVertex start = graph.getStartingVertex();
        start.mark();
        start.setVisible(true);
        start.setCurrentDistance(INIT_DISTANCE);
        visibleGraph.insertVertex(start);
        try {
            visibleGraph.setStartingVertex(start);
            updateCurrentDistancesForIncidents(start);
            for (GXEdge edge : graph.incidentEdges(start)) {
                GXVertex toIns = edge.getNextVertex();
                //shouldn't never be the case, at beginning only start is marked and no other vertex
                if (toIns == null) continue;
                //Setting the initial edge visible and the vertex at the other end
                toIns.setVisible(true);
                edge.setVisible(true);
                //inserting the vertex and edge into the visible graph
                visibleGraph.insertVertex(toIns);
                visibleGraph.insertEdge(edge);
            }
        } catch (ElementNotInGraphException eni) {
            eni.printStackTrace();
        }


        final GXVertex end =  graph.getEndingVertex();
        end.setVisible(true);
        try {
            visibleGraph.insertVertex(end);
            visibleGraph.setEndingVertex(end);
        } catch (ElementNotInGraphException eni) {
            eni.printStackTrace();
        }
    }

    /**
     * Returns all vertices the underlying {@link GXGraph} consists of.
     * @return vertices the graph consist of.
     */
    public Collection<GXVertex> getAllVertices() {
        return this.graph.vertices();
    }

}
