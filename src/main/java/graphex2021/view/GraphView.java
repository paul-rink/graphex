package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.*;
import graphex2021.model.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.control.Tooltip;

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

    private boolean isMoveable = false;




    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;

    public GraphView() throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(PROPERTIES)),
                STRAT, STYLESHEET.toURI());
        this.isMoveable = false;
    }

    public GraphView(boolean isMoveable) throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(MOVABLE_PROPERTIES)), STRAT, STYLESHEET.toURI());
        this.isMoveable = isMoveable;
    }

    //TODO how to get this load
    public GraphView(SmartPlacementStrategy strategy) throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(PROPERTIES)),
                strategy, STYLESHEET.toURI());
        this.isMoveable = false;
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
        showVertexTooltip(edge);
    }

    private void styleVertex(SmartGraphVertexNode vertex) {
        GXVertex gxVertex = (GXVertex) vertex.getUnderlyingVertex();

        if (gxVertex.isMarked()) {
            vertex.setStyleClass("markedVertex");
            showVertexTooltip(vertex, TooltipType.DISTANCE);
        } else if (!gxVertex.isMarked()) {
            vertex.setStyleClass("vertex");
            showVertexTooltip(vertex, TooltipType.ID);
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
        // Pane parent = (Pane) this.getParent();
        STRAT.place(this.getWidth(), this.getHeight(), super.theGraph, vertices);

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
        ChangeListener<Number> widthListener = ((observable, oldValue, newValue) -> {
            this.setWidth(newValue.doubleValue());
            this.placeVertices();
        }
        );
        ChangeListener<Number> heightListener = ((observable, oldValue, newValue) -> {
            this.setHeight(newValue.doubleValue());
            this.placeVertices();
        }
        );
        this.getScene().widthProperty().addListener(widthListener);
        this.getScene().heightProperty().addListener(heightListener);
        this.widthListener = widthListener;
        this.heightListener = heightListener;
    }

    public void removeListener() {
        this.getScene().widthProperty().removeListener(widthListener);
        this.getScene().heightProperty().removeListener(heightListener);
    }



    /**
     * Enables or disables tooltip for a vertex that contains its current distance to the start.
     * @param v is the vertex
     * @param type is the {@link TooltipType} that should be displayed, default is no tooltip
     */
    private void showVertexTooltip(SmartGraphVertexNode v, TooltipType type) {
        GXVertex vertex = (GXVertex) v.getUnderlyingVertex();
        Tooltip t = new Tooltip();
        switch (type) {
            case DISTANCE:
                t = new Tooltip("Distanz nach " + vertex.element() + " = " + vertex.getCurrentDistance());
                Tooltip.install(v, t);
                break;
            case ID:
                t = new Tooltip("ID: " + vertex.getId());
                Tooltip.install(v, t);
                break;
            default: Tooltip.uninstall(v, t);
        }
    }

    /**
     * Tooltip for an edge that will display the resulting distance to the next vertex. This feature is only available
     * for unmarked and unblocked edges and with exact 1 marked vertex.
     * @param e is the hovered edge
     */
    private void showVertexTooltip(SmartGraphEdgeLine e) {
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


    public double calcRelativeY(SmartGraphVertexNode smartVertex) {
        double correction = STRAT.getCorrection();
        Pane parent = (Pane) this.getParent().getParent();
        double relY;
        if (parent.getWidth() > this.getMinWidth() && parent.getHeight() > this.getMinHeight()) {
            if (correction > 1) {
                relY = (smartVertex.getPositionCenterY() * 1000) / (correction * parent.getHeight());
            } else {
                relY = (smartVertex.getPositionCenterY() * 1000) / parent.getHeight();
            }
        } else {
            relY = (smartVertex.getPositionCenterY() / this.getMinHeight()) * 1000.;
        }
        return relY;
    }

    public double calcRelativeX(SmartGraphVertexNode smartVertex) {
        double correction = STRAT.getCorrection();
        Pane parent = (Pane) this.getParent().getParent();
        double relX;
        if (parent.getWidth() > this.getMinWidth() && parent.getWidth() > this.getMinWidth()) {
            if (correction < 1) {
                relX = (smartVertex.getPositionCenterX() * 1000. * correction) / (parent.getWidth());
            } else {
                relX = (smartVertex.getPositionCenterX() * 1000.) / parent.getWidth();
            }
        } else {
            relX = (smartVertex.getPositionCenterX() / this.getMinWidth()) * 1000.;
        }
        return relX;
    }

    /**
     * Saves the current coordinates of the vertex in the pane in the underlyingVertex.
     *
     * @param smartVertex of which the position should be saved.
     */
    public void setMovedCoordinates(SmartGraphVertexNode smartVertex) {
        GXVertex vert = (GXVertex) smartVertex.getUnderlyingVertex();
        vert.getPosition().setPosition(calcRelativeX(smartVertex), calcRelativeY(smartVertex));
    }

    /**
     * Returns whether the vertices are moveable in this graph view
     *
     * @return whether vertices are moveable
     */
    public boolean isMoveable() {
        return isMoveable;
    }

    public enum TooltipType {
        /**
         * Tooltip will contain information to distances.
         */
        DISTANCE,
        /**
         * Tooltip should display ID of element
         */
        ID
    }
}
