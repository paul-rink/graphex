package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.*;
import graphex2021.model.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


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

    public GraphView() throws FileNotFoundException {
        super(new GraphAdapter(), new SmartGraphProperties(new FileInputStream(PROPERTIES)),
                STRAT, STYLESHEET.toURI());
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
    }

    private void styleVertex(SmartGraphVertexNode vertex) {
        GXVertex gxVertex = (GXVertex) vertex.getUnderlyingVertex();

        if (gxVertex.isMarked()) {
            vertex.setStyleClass("markedVertex");
        } else if (!gxVertex.isMarked()) {
            vertex.setStyleClass("vertex");
        }
        //TODO rethink the order here
        if (gxVertex.isHint()) {
            vertex.setStyleClass("hintVertex");
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
        double sceneHeight = getSceneHeight();
        double sceneWidth = getSceneWidth();
        //STRAT.place(sceneWidth, sceneHeight, super.theGraph, vertices);
        //TODO somehow need to get the real size of the pane, so that will
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


    }

    /**
     * method that returns the width of the scene the graphView is in in pixels
     * @return the width of the scene in pixels
     */
    public double getSceneWidth() {
        return this.getScene().getWidth();
    }

    /**
     * method that returns the height of the scene the graphView is in in pixels
     * @return the height of the scene in pixels
     */
    public double getSceneHeight() {
        return this.getScene().getHeight();
    }
}
