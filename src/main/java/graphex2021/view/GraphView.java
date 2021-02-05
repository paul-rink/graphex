package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.*;
import graphex2021.model.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.control.Tooltip;
import javax.tools.Tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedHashSet;


public class GraphView extends SmartGraphPanel implements Observer {

    private static final SmartStaticPlacementStrategy STRAT = new SmartStaticPlacementStrategy();

    //TODO check best Filepath separator
    private static final File STYLESHEET = new File("src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "graphex2021"
            + File.separator + "smartgraph.css");
    private static final File PROPERTIES = new File("src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "graphex2021"
            + File.separator + "smartgraph.properties");

    private static final File MOVABLE_PROPERTIES =  new File("src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "graphex2021"
            + File.separator + "smartgraphmove.properties");



    private ChangeListener listener;

    public GraphView() throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(PROPERTIES)),
                STRAT, STYLESHEET.toURI());
    }

    public GraphView(boolean moveAble) throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(MOVABLE_PROPERTIES)), STRAT, STYLESHEET.toURI());
    }

    //TODO how to get this load
    public GraphView(SmartPlacementStrategy strategy) throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(PROPERTIES)),
                strategy, STYLESHEET.toURI());
    }

    @Override
    public void doUpdate(Subject s) {
        GXGraph visible = (GXGraph) s.getState();
        GraphAdapter underlyingGraph = (GraphAdapter) super.theGraph;
        underlyingGraph.setGXGraph(visible);
        this.update();
    }

    @Override
    public void update() {
        //TODO possible fix for vertices now being placed correctly immediately.
        // Not sure what all the implications of this change are.
        if (this.getScene() == null) {
            throw new IllegalStateException("You must call this method after the instance was added to a scene.");
        }

        super.updateNodes();
        iterChildren();
    }

    /**
     * Will display every edge according to its state (unmarked, marked, blocked) different.
     */
    private void styleEdge(SmartGraphEdgeLine edge) {
        GXEdge gxEdge = (GXEdge) edge.getUnderlyingEdge();
        //call this first because every marked edge is blocked as well //TODO maybe change this?
        if (gxEdge.isBlocked() && !gxEdge.isMarked()) {
            edge.setStyleClass("blockedEdge");
        } else if (gxEdge.isMarked()) {
            edge.setStyleClass("markedEdge");
        } else if (!gxEdge.isMarked()) {
            edge.setStyleClass("edge");
        }
        if (gxEdge.isHint()) {
            edge.setStyleClass("hintEdge");
        }
        if (gxEdge.isHighlighted()) {
            edge.setStyleClass("highlightedEdge");
        }
        showDistanceTooltip(edge);
    }

    private void styleVertex(SmartGraphVertexNode vertex) {
        GXVertex gxVertex = (GXVertex) vertex.getUnderlyingVertex();

        if (gxVertex.isMarked()) {
            vertex.setStyleClass("markedVertex");
            showDistanceTooltip(vertex, true);
        } else if (!gxVertex.isMarked()) {
            vertex.setStyleClass("vertex");
            showDistanceTooltip(vertex, false);
        }
        //TODO rethink the order here
        if (gxVertex.isHint()) {
            vertex.setStyleClass("hintVertex");
        }

        if (gxVertex.getStartOrEnd() == GXVertexType.STARTING) {
            vertex.setStyleClass("startingVertex");
        } else if (gxVertex.getStartOrEnd() == GXVertexType.ENDING) {
            vertex.setStyleClass("endingVertex");
        }


    }


    @Override
    public void init() {
        STRAT.setSizes(this.getWidth(), this.getHeight(), this.getMinWidth(), this.getMinHeight());
        super.init();
        graphViewSizeListener();
    }

    private void placeVertices() {
        Collection<SmartGraphVertexNode<String>> vertices = new LinkedHashSet<>();
        for (Node node : this.getChildren()) {
            if (node.toString().startsWith("Circle")) {
                vertices.add((SmartGraphVertexNode<String>) node);
            }
        }
        placeVertices(vertices);
    }

    private void placeVertices(Collection<SmartGraphVertexNode<String>> vertices) {
        // start reacting correctly to changes below min size
        Pane parent = (Pane) this.getParent();
        STRAT.place(parent.getWidth(), parent.getHeight(), super.theGraph, vertices);

    }

    private void iterChildren() {
        Collection<SmartGraphVertexNode<String>> vertices = new LinkedHashSet<>();
        for (Node node : this.getChildren()) {
            if (node.toString().startsWith("Line")) {
                styleEdge((SmartGraphEdgeLine) node);
            } else if (node.toString().startsWith("Circle")) {
                styleVertex((SmartGraphVertexNode) node);
                vertices.add((SmartGraphVertexNode<String>) node);
            }
        }
        placeVertices(vertices);
    }

    private void graphViewSizeListener() {
        ChangeListener<Number> listener = ((observable, oldValue, newValue) -> this.placeVertices());

        this.getScene().widthProperty().addListener(listener);
        this.getScene().heightProperty().addListener(listener);
        this.listener = listener;
    }

    public void removeListener() {
        this.getScene().widthProperty().removeListener(listener);
        this.getScene().heightProperty().removeListener(listener);
    }


    /**
     * Enables or disables tooltip for a vertex that contains its current distance to the start.
     * @param v is the vertex
     * @param show is {@code true} if tooltip should be displayed, {@code false} otherwise.
     */
    private void showDistanceTooltip(SmartGraphVertexNode v, boolean show) {
        GXVertex vertex = (GXVertex) v.getUnderlyingVertex();
        Tooltip t = new Tooltip("Distanz nach " + vertex.element() + " = " + vertex.getCurrentDistance());
        if (show) {
            Tooltip.install(v, t);
        } else {
            Tooltip.uninstall(v, t);
        }
    }

    /**
     * Tooltip for an edge that will display the resulting distance to the next vertex. This feature is only available
     * for unmarked and unblocked edges and with exact 1 marked vertex.
     * @param e is the hovered edge
     */
    private void showDistanceTooltip(SmartGraphEdgeLine e) {
        GXEdge gxEdge = (GXEdge) e.getUnderlyingEdge();
        GXVertex unmarkedVertex = gxEdge.getNextVertex();
        Tooltip t = new Tooltip();
        if (gxEdge.getNextDistance() != GXEdge.INVALID_DISTANCE) {
            t.setText("Distanz nach " + unmarkedVertex.element()
                    + " über " + gxEdge.opposite(unmarkedVertex).element() + " = " + gxEdge.getNextDistance());
            Tooltip.install(e, t);
        } else {
            Tooltip.uninstall(e, t);
        }
    }

    /**
     * method that returns the width of the pane the graphView is in in pixels
     * @return the width of the pane in pixels
     */
    public double getPaneWidth() {
        return ((Pane) this.getParent()).getHeight();
    }

    /**
     * method that returns the height of the pane the graphView is in in pixels
     * @return the height of the pane in pixels
     */
    public double getPaneHeight() {
        return ((Pane) this.getParent()).getHeight();
    }
}
