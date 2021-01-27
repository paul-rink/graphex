package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.*;
import graphex2021.model.GXEdge;
import graphex2021.model.GXGraph;
import graphex2021.model.Observer;
import graphex2021.model.Subject;
import javafx.collections.ObservableList;
import javafx.scene.Node;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedHashSet;


public class GraphView extends SmartGraphPanel implements Observer {

    private static final SmartStaticPlacementStrategy STRAT = new SmartStaticPlacementStrategy();

    //TODO check best Filepath separator
    private static final File STYLESHEET = new File( "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "graphex2021"
            + File.separator + "smartgraph.css");
    private static final File PROPERTIES = new File("src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "graphex2021"
            + File.separator + "smartgraph.properties");

    public GraphView() throws FileNotFoundException {
        super(new GraphAdapter() , new SmartGraphProperties(new FileInputStream(PROPERTIES)),
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
        super.update();
        styleEdges();
        placeVertices();
    }

    /**
     * Will display every edge according to its state (unmarked, marked, blocked) different.
     */
    private void styleEdges() {
        for (Node node : this.getChildren()) {
            if (node instanceof SmartGraphEdgeLine) {
                SmartGraphEdgeLine edgeNode = (SmartGraphEdgeLine) node;
                GXEdge edge = (GXEdge) edgeNode.getUnderlyingEdge();
                //call this first because every marked edge is blocked as well //TODO maybe change this?
                if (edge.isBlocked() && !edge.isMarked()) {
                    edgeNode.setStyleClass("blockedEdge");
                } else if (edge.isMarked()) {
                    edgeNode.setStyleClass("markedEdge");
                }
            }
        }


    }

    @Override
    public void init() {
        super.init();

    }

    private void placeVertices() {
        //TODO unessecary to loop here and in styleEdges
        Collection<SmartGraphVertex<String>> vertices = new LinkedHashSet<>();
        for (Node node : this.getChildren()) {
            if (node instanceof SmartGraphVertexNode) {
                SmartGraphVertexNode vert = (SmartGraphVertexNode) node;
                vertices.add(vert);
            }
        }
        STRAT.place(super.widthProperty().doubleValue(), super.heightProperty().doubleValue(),
                super.theGraph, vertices);
    }

}
