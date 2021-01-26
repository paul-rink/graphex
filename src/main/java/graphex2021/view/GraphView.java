package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.*;
import graphex2021.model.GXEdge;
import graphex2021.model.GXGraph;
import graphex2021.model.Observer;
import graphex2021.model.Subject;
import javafx.scene.Node;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;


public class GraphView extends SmartGraphPanel<String, String> implements Observer {

    //TODO check best Filepath separator
    private static final File STYLESHEET = new File( "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "graphex2021"
            + File.separator + "smartgraph.css");
    private static final File PROPERTIES = new File("src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "graphex2021"
            + File.separator + "smartgraph.properties");

    public GraphView() throws FileNotFoundException {
        super(new GraphAdapter() , new SmartGraphProperties(new FileInputStream(PROPERTIES)),
                new SmartCircularSortedPlacementStrategy(), STYLESHEET.toURI());
    }

    @Override
    public void doUpdate(Subject s) {
        GXGraph visible = (GXGraph) s.getState();
        GraphAdapter underlyinigGraph = (GraphAdapter) super.theGraph;
        underlyinigGraph.setGXGraph(visible);
        this.update();
    }

    @Override
    public void update() {
        super.update();
        styleMarkedEdges();
    }

    private void styleMarkedEdges() {
        for (Node node : this.getChildren()) {
            if (node instanceof SmartGraphEdgeLine) {
                SmartGraphEdgeLine edgeNode = (SmartGraphEdgeLine) node;
                if (((GXEdge) edgeNode.getUnderlyingEdge()).isMarked()) {
                    edgeNode.setStyleClass("markedEdge");
                }
            }
        }
    }
}
