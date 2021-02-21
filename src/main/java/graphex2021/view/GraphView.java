package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.*;
import graphex2021.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedHashSet;


public class GraphView extends SmartGraphPanel<String, String> implements Observer {

    private static final SmartStaticPlacementStrategy STRAT = new SmartStaticPlacementStrategy();
    private static File moveableProperties;

    private static File stylesheet;

    private static File properties;
    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;
    private boolean isMoveable = false;

    static {
        try {
            stylesheet = new File(new File(GraphView.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).getParentFile(), "resources"
                    + File.separator + "graphex2021"
                    + File.separator + "smartgraph.css");
            properties = new File(new File(GraphView.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).getParentFile(), "resources"
                    + File.separator + "graphex2021"
                    + File.separator + "smartgraph.properties");
            moveableProperties = new File(new File(GraphView.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).getParentFile(), "resources"
                    + File.separator + "graphex2021"
                    + File.separator + "smartgraphmove.properties");
        } catch (URISyntaxException e) {
            //TODO what here
            e.getMessage();
        }
    }

    /**
     * Creates new GraphView with an empty graph using the standard stylesheet and and properties.
     *
     * @throws FileNotFoundException if either properties or css are not found
     */
    public GraphView() throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(properties)),
                STRAT, stylesheet.toURI());
        this.isMoveable = false;
    }

    /**
     * Creates a GraphView with an empty graph and using the standard stylesheet and moveable vertices
     *
     * @param isMoveable really not used. If you want a GraphView where vertices aren't moveable us constructor with no
     *                   parameters. Should always be {@code true}!!
     * @throws FileNotFoundException If stylesheet or properties are not found.
     */
    public GraphView(boolean isMoveable) throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties
                (new FileInputStream(moveableProperties)), STRAT, stylesheet.toURI());
        this.isMoveable = isMoveable;
    }

    /**
     * TODO use for random graphs?
     *
     * @param strategy
     * @throws FileNotFoundException
     */
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
        Platform.runLater(this::update);

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
        //Needs to override the other Styles if it is requested as a hint.
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
        if (!this.getBackground().getImages().isEmpty()) {
            return this.getBackground().getImages().get(0).getImage().getHeight();
        }
        return this.getPrefHeight();
    }

    private double getBackgroundImageWidth() {
        if (!this.getBackground().getImages().isEmpty()) {
            return this.getBackground().getImages().get(0).getImage().getWidth();
        }
        return this.getPrefHeight();
    }

    /**
     * Places vertices according to STRAT. Will use the coordinates stored in the underlying vertices.
     * For them to be placed they are all put into a colection and then passed
     * to the {@link SmartStaticPlacementStrategy}
     */
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

    /**
     * Removes the listeners set on this instance. Needs to be called if it is replaced later, so that there are no
     * NPEs on the
     */
    public void removeListener() {
        this.getScene().widthProperty().removeListener(widthListener);
        this.getScene().heightProperty().removeListener(heightListener);
    }


    /**
     * Enables or disables tooltip for a vertex that contains its current distance to the start.
     *
     * @param v    is the vertex
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
            default:
                Tooltip.uninstall(v, t);
        }
    }

    /**
     * Tooltip for an edge that will display the resulting distance to the next vertex. This feature is only available
     * for unmarked and unblocked edges and with exact 1 marked vertex.
     *
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

    /**
     * Will create a context menu showing the current distance to this vertex.
     *
     * @param v Vertex for which the distance will be displayed.
     * @param x x-position the vertex will be displayed in
     * @param y y-position the vertex will be displayed in
     */
    public void showVertexDistance(SmartGraphVertexNode v, double x, double y) {
        GXVertex vertex = (GXVertex) v.getUnderlyingVertex();
        ContextMenu context = new ContextMenu();
        MenuItem item = new MenuItem();
        item.setText("Distanz nach " + vertex.element() + " = " + vertex.getCurrentDistance());
        context.getItems().add(item);
        double offsetX = this.getScene().getWindow().getX();
        double offsetY = this.getScene().getWindow().getY();
        context.show(v, x + offsetX, y + offsetY);
    }

    /**
     * Will return the relative y-position of the vertex in relation to the {@link GraphView} height.
     *
     * @param smartVertex the position is to be calculated of
     * @return the relative y position of this vertex
     */
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

    /**
     * Will return the relative x-position of the vertex in relation to the {@link GraphView} height.
     *
     * @param smartVertex the position is to be calculated of
     * @return the relative x position of this vertex
     */
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
     * Will set the coordinates of the underlying {@link GXVertex} to the positiion the {@link SmartGraphVertexNode} is
     * currently in. The update will wait until the all the other events before it are done. Will mean that it is not
     * guaranteed that it is updated immediately.
     *
     * @param smartVertex of which the position should be saved.
     */
    public void setMovedCoordinates(SmartGraphVertexNode smartVertex) {
        Platform.runLater(() -> {
            GXVertex vert = (GXVertex) smartVertex.getUnderlyingVertex();
            try {
                vert.getPosition().setPosition(calcRelativeX(smartVertex), calcRelativeY(smartVertex));
            } catch (NonValidCoordinatesException e) {
                e.printStackTrace(); //TODO is this a problem?
                }
        });
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
