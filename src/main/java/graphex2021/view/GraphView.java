package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.*;
import graphex2021.model.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.control.Tooltip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedHashSet;


public class GraphView extends SmartGraphPanel implements Observer {

    private static final SmartStaticPlacementStrategy STRAT = new SmartStaticPlacementStrategy();

    //TODO check best Filepath separator
    private static File stylesheet;

    private static File properties;

    static {
        try {
            stylesheet = new File(new File(GraphView.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile(), "resources"
                    + File.separator + "graphex2021"
                    + File.separator + "smartgraph.css");
            properties = new File(new File(GraphView.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile(), "resources"
                            + File.separator + "graphex2021"
                            + File.separator + "smartgraph.properties");
            moveableProperties = new File(new File(GraphView.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile(), "resources"
                    + File.separator + "graphex2021"
                    + File.separator + "smartgraphmove.properties");
        } catch (URISyntaxException e) {
            //TODO what here
            e.getMessage();
        }
    }


    private static File moveableProperties;
    private boolean isMoveable = false;

    private ChangeListener listener;

    private static File print(File s) {
        System.out.println(s);
        return s;
    }


    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;


    public GraphView() throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(print(properties))),
                STRAT, stylesheet.toURI());
        this.isMoveable = false;
    }

    public GraphView(boolean isMoveable) throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(moveableProperties)), STRAT, stylesheet.toURI());
        this.isMoveable = isMoveable;
    }

    //TODO how to get this load
    public GraphView(SmartPlacementStrategy strategy) throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(properties)),
                strategy, stylesheet.toURI());
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
        double prefWidth = getBackgroundImageWidth();
        double prefHeight = getBackgroundImageHeight();
        STRAT.setSizes(prefWidth, prefHeight, this.getMinWidth(), this.getMinHeight());
        super.init();
        graphViewSizeListener();
    }

    private double getBackgroundImageHeight() {
        if(!this.getBackground().getImages().isEmpty()) {
            return this.getBackground().getImages().get(0).getImage().getHeight();
        }
        return this.getPrefHeight();
    }

    private double getBackgroundImageWidth() {
        if(!this.getBackground().getImages().isEmpty()) {
            return this.getBackground().getImages().get(0).getImage().getWidth();
        }
        return this.getPrefHeight();
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
         Pane parent = (Pane) this.getParent().getParent();
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
            t.setText("[ID: " + gxEdge.getId() + "] Distanz nach " + unmarkedVertex.element()
                    + " über " + gxEdge.opposite(unmarkedVertex).element() + " = " + gxEdge.getNextDistance());
            Tooltip.install(e, t);
        } else {
            Tooltip.uninstall(e, t);
        }
    }

    public void showVertexDistance(SmartGraphVertexNode v, double x, double y) {
        GXVertex vertex = (GXVertex) v.getUnderlyingVertex();
        Label label = new Label("Distanz nach " + vertex.element() + " = " + vertex.getCurrentDistance());
        ContextMenu context = new ContextMenu();
        MenuItem item = new MenuItem();
        item.setText("Distanz nach " + vertex.element() + " = " + vertex.getCurrentDistance());
        context.getItems().add(item);
        double offsetX = this.getScene().getWindow().getX();
        double offsetY = this.getScene().getWindow().getY();
        context.show(v, x + offsetX, y + offsetY);
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

    public double calcRelativeY(SmartGraphVertexNode smartVertex) {
        double correction = STRAT.getCorrection();
        double relY;
        if (this.getWidth() > this.getMinWidth() && this.getHeight() > this.getMinHeight()) {
            if (correction > 1) {
                relY = (smartVertex.getPositionCenterY() * 1000) / (correction * this.getHeight());
            } else {
                relY = (smartVertex.getPositionCenterY() * 1000) / this.getHeight();
            }
        } else {
            relY = (smartVertex.getPositionCenterY() / this.getMinHeight()) * 1000.;
        }
        return relY;
    }

    public double calcRelativeX(SmartGraphVertexNode smartVertex) {
        double correction = STRAT.getCorrection();
        double relX;
        if (this.getWidth() > this.getMinWidth() && this.getWidth() > this.getMinWidth()) {
            if (correction < 1) {
                relX = (smartVertex.getPositionCenterX() * 1000. * correction) / (this.getWidth());
            } else {
                relX = (smartVertex.getPositionCenterX() * 1000.) / this.getWidth();
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
