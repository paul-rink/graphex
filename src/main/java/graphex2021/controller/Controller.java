package graphex2021.controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import graphex2021.model.*;
import graphex2021.view.GXTableView;
import graphex2021.view.GraphView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

public class Controller {

    private static final String UNLOCKPASSWORD = "Algorithmus";

    /**
     * The {@link DisplayModel}, this controller sets the actions for.
     */
    private DisplayModel displayModel;

    private GXTableView gxTable;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GraphView graphView;

    @FXML
    private MenuItem check;

    @FXML
    private MenuItem tip;

    @FXML
    private Button finish;

    /**
     * Create a new Controller, where the {@link DisplayModel} is newly created
     * by using the standard {@link graphex2021.model.GXGraph}
     */
    public Controller() {
        try {
            this.displayModel = new DisplayModel();
        } catch (WrongFileFormatException e) {
            //TODO handle this error by displaying a message box
            e.printStackTrace();
        }

        this.gxTable = new GXTableView();
    }

    public void init() {
        initGraphView();
        initTableView();
        displayModel.notifyObservers();
    }

    /**
     * When program is launched this can be called to notify the model that graphview is about to be initialized.
     */
    public void initGraphView() {
        displayModel.register(graphView);
        graphView.setAutomaticLayout(false);
        graphView.init();
    }

    /**
     * Tells the model that user interaction with graph should be enabled and the user is about to perform edge/vertex
     * selections (for a specific algorithm).
     */
    public void onStartPressed() {
        //TODO reenable the button if it ist possible to finish.
        setActions();
        finish.setText("Beenden");
        finish.setDisable(true);

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
    public void initTableView() {
        gxTable.init(displayModel.getAllVertices());
        displayModel.register(gxTable);
    }

    public void showTable() {
        Stage tableStage = new Stage();
        tableStage.setTitle("Tabelle");
        tableStage.setScene(new Scene(new VBox(gxTable)));
        tableStage.show();


    }

    public void setActions() {
        graphView.setEdgeDoubleClickAction(e -> onSelectEdge((SmartGraphEdge) e));
        graphView.setVertexDoubleClickAction(v -> onSelectVertex((SmartGraphVertex) v));
        //TODO WIP

        for (Node node : graphView.getChildren()) {
           if (node instanceof SmartGraphVertexNode) {
               SmartGraphVertexNode vert = (SmartGraphVertexNode) node;
               vert.setOnMouseEntered(s-> onHoverEdge((SmartGraphVertexNode) s.getSource()));
               vert.setOnMouseExited(s -> onLeaveEdge((SmartGraphVertexNode) s.getSource()));
           }
        }
    }

    public void onHoverEdge(SmartGraphVertexNode e) {
        e.setStyleClass("testClass");
    }

    public void onLeaveEdge(SmartGraphVertexNode e) {
        e.setStyleClass("vertex");
    }

    /**
     * Is called when an edge is selected.
     * @param e is the edge the user selected.
     */
    public void onSelectEdge(SmartGraphEdge e) {
        try {
            displayModel.markEdge((GXEdge) e.getUnderlyingEdge());
        } catch (ElementNotInGraphException elementNotInGraphException) {
            new ElementNotInGraphAlert().show();
        } catch (EdgeCompletesACircleException edgeCompletesACircleException) {
            new EdgeCompletesACircleAlert().show();
        }
    }

    /**
     * Is called when the user selects a vertex.
     * @param v is the selected vertex.
     */
    public void onSelectVertex(SmartGraphVertex v) {
        new VertexDoubleClickAlert().show();
    }

    /**
     * When the user requests a hint, the next step according to the selected algorithm should be shown.
     */
    public void hintRequest() {
        displayModel.nexStep();
    }

    /**
     * Checks, if the user input corresponds to the steps the selected algorithm would perform.
     */
    public void onCheck() {
        Alert check = new Alert(Alert.AlertType.INFORMATION);
        check.setTitle("Check");
        if (displayModel.checkCorrect()) {
            check.setHeaderText("Bisher ist alles richtig.");
        } else {
            check.setHeaderText("Da hat sich leider ein Fehler eingeschlichen");
        }
        check.showAndWait();
    }



    /**
     * Will give the user the ability to load a new graph via a json file.
     */
    public void onLoadGraph() {
        //TODO make it so that if the choosing is canceled they are not unregisterd
        //unregistering the graphView and table from the Displaymodel, since they will not be needed.
        //Also so they won't updated everytime
        displayModel.unregister(graphView);
        displayModel.unregister(gxTable);

        Stage browserStage = new Stage();
        browserStage.setTitle("FileBrowser");
        browserStage.setScene(new Scene(new VBox()));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Graph auswaehlen");
        File file = fileChooser.showOpenDialog(browserStage);


        try {
            this.displayModel = new DisplayModel(file);
        } catch (WrongFileFormatException e) {
            e.printStackTrace();//TODO handle this
        }
        /*
        TODO for now the new graphView is just loaded by exchanging the graph, which really limits the possibilities.
        TODO either change by correctly removing and adding to the pane or propably best to reload controller
        // The Pane that graphView is part of (In this case boder pane)
        Pane parent = (Pane) graphView.getParent();

        // Removing the graphView so that later a graphView with other properties can be added.
        parent.getChildren().remove(graphView);

        double height = graphView.getHeight();
        double width = graphView.getWidth() ;
        */
        //
        /*
        try {
            // TODO propably needs to be done like this, so that properites can be changed as well.
            this.graphView = new GraphView();

            /*
            graphView.setPrefSize(width, height);
            // Adding the new graphView to the pane
            parent.getChildren().add(graphView);
            parent.layout();
            */
        /*
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
        this.gxTable = new GXTableView();

        displayModel.register(graphView);
        initTableView();
        displayModel.notifyObservers();
        /*
        init();
        displayModel.notifyObservers();
         */

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
        try {
            displayModel.undo();
        } catch (ElementNotInGraphException e) {
            e.printStackTrace();
        }
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
        displayModel.reset();
    }

    public void unlockHints() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setHeaderText("Gib das Passwort ein um die"
                + " Hilfefunktionen freizuschalten");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (UNLOCKPASSWORD.equals(result.get())) {
                check.setDisable(false);
                tip.setDisable(false);
            } else if ("Test".equals(result.get())) {
                displayCoordinates();
            } else {
                new Alert(Alert.AlertType.ERROR, "Das war das falsche Passwort.").showAndWait();
            }
        }
    }

    private void displayCoordinates() {
        for (Node vertex : graphView.getChildren())  {
            if (vertex.toString().contains("Circle")) {
                SmartGraphVertexNode vert = (SmartGraphVertexNode) vertex;
                vert.setOnMousePressed((MouseEvent mouseEvent) -> {
                    if (mouseEvent.getButton().equals(MouseButton.MIDDLE)) {
                        double x = vert.getPositionCenterX() / graphView.getWidth();
                        double y = vert.getPositionCenterY() / graphView.getHeight();
                        System.out.println(vert.getUnderlyingVertex().element().toString() + " x = " + x + " , y = " + y + " Style:  " + vertex.getStyleClass());

                    }
                });
            }
        }
    }

    private void bindAspectRation() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        double ratio = stage.getWidth() / stage.getHeight();
        stage.minWidthProperty().bind(stage.heightProperty().multiply(ratio));
        stage.minHeightProperty().bind(stage.widthProperty().divide(ratio));

    }
}
