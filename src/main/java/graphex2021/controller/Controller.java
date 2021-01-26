package graphex2021.controller;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeLine;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import graphex2021.model.Algorithm;
import graphex2021.model.DisplayModel;
import graphex2021.view.GraphView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import static com.brunomnsilva.smartgraph.graphview.UtilitiesJavaFX.pick;

public class Controller {

    /**
     * The {@link DisplayModel}, this controller sets the actions for.
     */
    private DisplayModel displayModel;

    @FXML
    private GraphView graphView;

    /**
     * Create a new Controller, where the {@link DisplayModel} is newly created
     * by using the standard {@link graphex2021.model.GXGraph}
     */
    public Controller() {
        this.displayModel = new DisplayModel();
    }

    /**
     * When program is launched this can be called to notify the model that graphview is about to be initialized.
     */
    public void initGraphView() {
        displayModel.register(graphView);
        displayModel.notifyObservers();
        graphView.init();
        graphView.update();
        setActions();
    }

    /**
     * Tells the model that user interaction with graph should be enabled and the user is about to perform edge/vertex
     * selections (for a specific algorithm).
     */
    public void onStartPressed() {

    }

    /**
     * When user presses finish button, it has to be checked, if end vertex was reached and the distance is correct.
     * Then the user will get a feedback.
     */
    public void onFinishedPressed() {

    }

    /**
     * Initialize the table where user steps (according to algorithm) are displayed.
     */
    public void iniTableView() {

    }

    public void setActions() {
        //TODO WIP
        graphView.setOnMouseEntered((MouseEvent mouseEvent) -> {
            Node node = pick(graphView, mouseEvent.getSceneX(), mouseEvent.getSceneY());
            if (node instanceof SmartGraphVertexNode) {
                SmartGraphVertexNode edge = (SmartGraphVertexNode) node;
                edge.setOnMouseEntered(e -> onHoverEdge((SmartGraphVertexNode) e.getSource()));
            }
        } );


        /*
        for (Node child : graphView.getChildren()) {
            if (child instanceof SmartGraphVertexNode) {
                SmartGraphVertexNode edge = (SmartGraphVertexNode) child;
                edge.setOnMouseEntered(e -> onHoverEdge((SmartGraphVertexNode) e.getSource()));
                edge.setOnMouseExited(e -> onLeaveEdge((SmartGraphVertexNode) e.getSource()));
                edge.setOnMouseClicked(e -> onHoverEdge((SmartGraphVertexNode) e.getSource()));
            }
        }
        */



    }

    public void onHoverEdge(SmartGraphVertexNode e) {
        e.setStyleClass("testClass");
        System.out.println("Rein");

    }

    public void onLeaveEdge(SmartGraphVertexNode e) {
        System.out.println("Raus");

    }

    /**
     * Is called when an edge is selected.
     * @param e is the edge the user selected.
     */
    public void onSelectEdge(Edge e) {

    }

    /**
     * Is called when the user selects a vertex.
     * @param v is the selected vertex.
     */
    public void onSelectVertex(Vertex v) {

    }

    /**
     * When the user requests a hint, the next step according to the selected algorithm should be shown.
     */
    public void hintRequest() {

    }

    /**
     * Checks, if the user input corresponds to the steps the selected algorithm would perform.
     */
    public void onCheck() {

    }

    /**
     * Will give the user the ability to load a new graph via a json file.
     */
    public void onLoadGraph() {

    }

    /**
     * When this is called, a random Graph will be created an load in the view.
     */
    public void onGenerateRandom() {

    }

    /**
     * This is called, when the user wants to revert a step, i.e. unmark the last selected edge or vertex and
     * everything that comes along with this.
     */
    public void onUndoPressed() {

    }

    /**
     * Will update the model for the specific algorithm that is selected.
     * @param algo is the algorithm that is selected to be performed at the graph in the view.
     */
    public void onAlgorithmSelect(Algorithm algo) {

    }

    /**
     * Will show some information on the selected algorithm in the view.
     */
    public void onDisplayAlgorithmExplanation() {

    }

    /**
     * Will show some information on interaction options in the view.
     */
    public void onDisplayInteractionHelp() {

    }

    /**
     * Called on the reset button being pressed
     */
    public void onResetPressed() {

    }
}
