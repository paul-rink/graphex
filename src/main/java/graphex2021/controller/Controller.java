package graphex2021.controller;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import graphex2021.model.Algorithm;

public class Controller {

    /**
     * When program is launched this can be called to notify the model that graphview is about to be initialized.
     */
    public void initGraphView() {

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
