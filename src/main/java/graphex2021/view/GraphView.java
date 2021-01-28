package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.*;
import graphex2021.model.*;
import javafx.scene.Node;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedHashSet;


public class GraphView extends SmartGraphPanel implements Observer {

    private static final SmartPlacementStrategy STRAT = new SmartStaticPlacementStrategy();

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
        GraphAdapter underlyiigGraph = (GraphAdapter) super.theGraph;
        underlyiigGraph.setGXGraph(visible);
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
    }

    private void styleVertex(SmartGraphVertexNode vertex) {
        GXVertex gxVertex = (GXVertex) vertex.getUnderlyingVertex();

        if (gxVertex.isMarked()) {
            vertex.setStyleClass("markedVertex");
        } else if (!gxVertex.isMarked()) {
            vertex.setStyleClass("vertex");
        }
    }


    @Override
    public void init() {
        super.init();

    }

    private void placeVertices(Collection<SmartGraphVertexNode<String>> vertices) {
        STRAT.place(this.widthProperty().doubleValue(), this.heightProperty().doubleValue(),
                super.theGraph, vertices);
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

}
